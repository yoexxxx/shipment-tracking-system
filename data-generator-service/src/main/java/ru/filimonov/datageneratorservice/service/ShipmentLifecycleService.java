package ru.filimonov.datageneratorservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.RouteData;
import model.dto.ShipmentData;
import model.enums.ShipmentStatus;
import org.springframework.stereotype.Service;
import utils.GeneratorUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentLifecycleService {
  private static final Map<ShipmentStatus, ShipmentStatus> shipmentLifecycleMap = Map.of(
      ShipmentStatus.CREATED, ShipmentStatus.ENTRY_TO_COUNTRY,
      ShipmentStatus.ENTRY_TO_COUNTRY, ShipmentStatus.IN_TRANSIT,
      ShipmentStatus.IN_TRANSIT, ShipmentStatus.LEFT_THE_COUNTRY,
      ShipmentStatus.LEFT_THE_COUNTRY, ShipmentStatus.DELIVERED
  );

  public ShipmentData updateCurrentShipment(ShipmentData currentShipment) {
    var nextShipmentStatus = getNextShipmentStatus(currentShipment);
    return currentShipment.toBuilder()
                          .updatedAt(LocalDateTime.now())
                          .shipmentStatus(nextShipmentStatus)
                          .routeData(updateRouteData(currentShipment, nextShipmentStatus))
                          .build();
  }

  private ShipmentStatus getNextShipmentStatus(ShipmentData currentShipment) {
    var currentShipmentStatus = currentShipment.shipmentStatus();

    return Optional.ofNullable(shipmentLifecycleMap.get(currentShipmentStatus))
                   .orElseGet(() -> {
                     log.warn("Cannot find next status for update, returning currentStatus - [{}]",
                              currentShipmentStatus);
                     return currentShipmentStatus;
                   });
  }

  private RouteData updateRouteData(ShipmentData currentShipment, ShipmentStatus nextStatus) {
    var currentRouteData = currentShipment.routeData();
    if (nextStatus != ShipmentStatus.DELIVERED) {
      return currentRouteData.toBuilder()
                             .currentPoint(GeneratorUtils.FAKER.address().cityName())
                             .build();
    } else return currentRouteData.toBuilder()
                             .currentPoint(currentShipment.routeData().endPoint())
                             .build();
  }
}
