package ru.filimonov.eventhandlerservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.ShipmentData;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.filimonov.eventhandlerservice.dto.response.ShipmentResponse;
import ru.filimonov.eventhandlerservice.entity.Shipment;
import ru.filimonov.eventhandlerservice.mapper.ShipmentMapper;
import ru.filimonov.eventhandlerservice.repository.ShipmentRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentService {

  private final ShipmentMapper shipmentMapper;
  private final ShipmentRepository shipmentRepository;

  @Transactional
  public void processShipment(ShipmentData shipmentData) {
    try {
      shipmentRepository.findByShipmentId(shipmentData.shipmentId())
                        .ifPresentOrElse(shipment -> {
                          shipmentMapper.updateShipmentEntity(shipmentData, shipment);
                          shipmentRepository.save(shipment);
                          log.info("ShipmentEvent was successfully updated: shipmentEventId - [{}]",
                                   shipmentData.shipmentId());
                        }, () -> {
                          var shipment = shipmentMapper.toShipmentEntity(shipmentData);
                          shipmentRepository.save(shipment);
                          log.info("ShipmentEvent was successfully created: shipmentEventId - [{}]",
                                   shipmentData.shipmentId());
                        });
    } catch (DataIntegrityViolationException exception) {
      log.warn("""
                   Shipment processing failed due to invalid ShipmentData:
                   shipmentId - [{}];
                   error cause: [{}]
                   """,
               shipmentData.shipmentId(),
               exception.getMostSpecificCause().getMessage());
    }
  }

  @Transactional(readOnly = true)
  public ShipmentResponse getShipmentById(String shipmentId) {
    return shipmentRepository.findByShipmentId(shipmentId)
                             .map(shipmentMapper::toShipmentResponse)
                             .orElseThrow(() -> new EntityNotFoundException(
                                 String.format("Shipment Entity, with shipmentId - %s, not found!", shipmentId)
                             ));
  }

  @Transactional(readOnly = true)
  public List<ShipmentResponse> getAllShipments() {
    List<Shipment> allShipments = shipmentRepository.findAll();
    return shipmentMapper.toShipmentResponseList(allShipments);
  }

  @Transactional
  public boolean deleteShipmentById(String shipmentId) {
    return shipmentRepository.findByShipmentId(shipmentId)
                             .map(shipment -> {
                               shipmentRepository.delete(shipment);
                               log.info("ShipmentEvent deleted: shipmentEventId - [{}]", shipmentId);
                               return true;
                             }).orElse(false);
  }
}
