package ru.filimonov.datageneratorservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.ShipmentData;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentPublisherService {
  private final KafkaTemplate<String, ShipmentData> kafkaTemplate;

  @Retryable(value = KafkaException.class,
              maxAttempts = 4,
              backoff = @Backoff(delay = 1000, multiplier = 2))
  public void sendShipmentEvent(ShipmentData shipmentData, String kafkaTopic){
    var shipmentId = String.valueOf(shipmentData.shipmentId());
    kafkaTemplate.send(kafkaTopic, shipmentId, shipmentData);
    log.info("Kafka sent: to topic - [{}], eventId - [{}], status - [SUCCESS]", kafkaTopic, shipmentId);
  }

  public void sendAllShipmentEvents(List<ShipmentData> allShipmentEvents, String kafkaTopic){
      allShipmentEvents.forEach(shipmentData -> {
        try {
          sendShipmentEvent(shipmentData, kafkaTopic);
        }catch (Exception exception){
          log.warn("Failed to sent shipmentEvent in batch: eventId - [{}], skipping due to error:",
                   shipmentData.shipmentId(), exception);
        }
      });
  }

  @Recover
  public void recoverShipmentSend(KafkaException exception, ShipmentData shipmentData, String kafkaTopic){
    var shipmentId = String.valueOf(shipmentData.shipmentId());
    log.error("Error while sending message: to topic - [{}], shipmentId - [{}], error:",
              kafkaTopic, shipmentId, exception);
  }
}
