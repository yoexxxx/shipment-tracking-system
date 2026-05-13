package ru.filimonov.datageneratorservice.generator;

import lombok.RequiredArgsConstructor;
import model.dto.ShipmentData;
import model.enums.ShipmentStatus;
import org.springframework.stereotype.Component;
import ru.filimonov.datageneratorservice.annotation.GenerateData;
import ru.filimonov.datageneratorservice.generator.subgenerator.CargoDataGenerator;
import ru.filimonov.datageneratorservice.generator.subgenerator.DriverDataGenerator;
import ru.filimonov.datageneratorservice.generator.subgenerator.RouteDataGenerator;
import ru.filimonov.datageneratorservice.generator.subgenerator.VehicleDataGenerator;
import utils.GeneratorUtils;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ShipmentDataGenerator {
  private final CargoDataGenerator cargoDataGenerator;
  private final DriverDataGenerator driverDataGenerator;
  private final RouteDataGenerator routeDataGenerator;
  private final VehicleDataGenerator vehicleDataGenerator;

  @GenerateData
  public ShipmentData generateNewShipmentData() {
    return ShipmentData.builder()
                       .shipmentId(GeneratorUtils.generateShipmentId())
                       .shipmentStatus(ShipmentStatus.CREATED) //note: initial status for generation
                       .createdAt(LocalDateTime.now())
                       .cargoData(cargoDataGenerator.generateCargoData())
                       .driverData(driverDataGenerator.generateDriverData())
                       .routeData(routeDataGenerator.generateRouteData())
                       .vehicleData(vehicleDataGenerator.generateVehicleData())
                       .build();
  }
}