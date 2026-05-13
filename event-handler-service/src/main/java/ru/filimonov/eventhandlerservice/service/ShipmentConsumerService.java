package ru.filimonov.eventhandlerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.ShipmentData;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentConsumerService {
  private final ShipmentService shipmentService;

  @KafkaListener(topics = "shipment-events", groupId = "shipment-consumer-group")
  @RetryableTopic(
      attempts = "4",
      backOff = @BackOff(delay = 1000, multiplier = 2),
      autoCreateTopics = "true",
      dltStrategy = DltStrategy.FAIL_ON_ERROR,
      exclude = {
          DataIntegrityViolationException.class
      }
  )
  public void consumeShipmentEvent(ShipmentData shipmentData,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String kafkaTopic,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionNumber,
                                   @Header(KafkaHeaders.OFFSET) long offset) {
    shipmentService.processShipment(shipmentData);
    log.info("Processed shipment event: eventId - [{}], from topic - [{}], partition number - [{}], offset - [{}]",
             shipmentData.shipmentId(), kafkaTopic, partitionNumber, offset);
  }

  @DltHandler
  public void handleDlt(ShipmentData shipmentData, @Header(KafkaHeaders.RECEIVED_TOPIC) String kafkaTopic) {
    log.info("Sent shipmentEvent to DLQ: eventId - [{}], from topic - [{}]", shipmentData.shipmentId(), kafkaTopic);
  }
}
