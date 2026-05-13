package ru.filimonov.datageneratorservice.service.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.ShipmentData;
import model.enums.ShipmentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.filimonov.datageneratorservice.generator.ShipmentDataGenerator;
import ru.filimonov.datageneratorservice.service.ShipmentLifecycleService;
import ru.filimonov.datageneratorservice.service.ShipmentPublisherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentOrchestratorService {
  private final ShipmentPublisherService shipmentPublisherService;
  private final ShipmentLifecycleService shipmentLifecycleService;
  private final ShipmentDataGenerator shipmentDataGenerator;
  private final ConfigurableApplicationContext configurableApplicationContext;

  private final Map<String, ShipmentData> activeShipments = new ConcurrentHashMap<>();

  @Value("${kafka.topic}")
  private String kafkaTopic;

  @EventListener(ApplicationReadyEvent.class)
  public void initializeAndSendTestShipments() {
    log.info("Initializing 5 test shipment events and sending to: kafkaTopic - [{}]", kafkaTopic);
    IntStream.range(0, 5)
             .mapToObj(shipmentEvent -> shipmentDataGenerator.generateNewShipmentData())
             .forEach(generatedShipment -> {
               var generatedShipmentId = generatedShipment.shipmentId();
               activeShipments.put(generatedShipmentId, generatedShipment);
             });
    shipmentPublisherService.sendAllShipmentEvents(new ArrayList<>(activeShipments.values()), kafkaTopic);
    log.info("Initialize complete: [{}] - shipmentEvents was successfully send!", activeShipments.size());
  }

  @Scheduled(fixedRate = 120000, initialDelay = 120000)
  public void updateAndSendActiveShipments() {
    if (activeShipments.isEmpty()) {
      log.info("Cannot find any active shipment events, closing update!");
      shutdownApplication();
      return;
    }

    log.info("Updating [{}] - active shipment events", activeShipments.size());
    List<ShipmentData> updatedShipmentEvents = new ArrayList<>();

    activeShipments.forEach((id, currentEvent) -> {
      var updatedShipment = shipmentLifecycleService.updateCurrentShipment(currentEvent);
      refreshActiveShipmentEntry(updatedShipment);
      updatedShipmentEvents.add(updatedShipment);
    });

    shipmentPublisherService.sendAllShipmentEvents(updatedShipmentEvents, kafkaTopic);
    log.info("Update complete, [{}] - shipment events successfully sent, to topic: [{}]",
             updatedShipmentEvents.size(), kafkaTopic);
  }

  private void refreshActiveShipmentEntry(ShipmentData updatedShipment) {
    if (updatedShipment.shipmentStatus() == ShipmentStatus.DELIVERED) {
      activeShipments.remove(updatedShipment.shipmentId());
      log.info("Closed shipment: shipmentId - [{}], was successfully removed from activeShipments",
               updatedShipment.shipmentId());
    } else {
      activeShipments.put(updatedShipment.shipmentId(), updatedShipment);
      log.info("Add update for shipment: shipmentId - [{}], updatedAt - [{}]",
               updatedShipment.shipmentId(), updatedShipment.updatedAt());
    }
  }

  private void shutdownApplication(){
    log.info("All ShipmentEvents were successfully processed. Shutting down generator-service application!");
    SpringApplication.exit(configurableApplicationContext, () -> 0);
  }
}