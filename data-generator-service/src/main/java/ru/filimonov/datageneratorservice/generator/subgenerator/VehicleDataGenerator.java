package ru.filimonov.datageneratorservice.generator.subgenerator;

import model.dto.VehicleData;
import model.enums.VehicleType;
import org.springframework.stereotype.Component;
import ru.filimonov.datageneratorservice.annotation.GenerateData;
import utils.GeneratorUtils;

@Component
public class VehicleDataGenerator {
  @GenerateData
  public VehicleData generateVehicleData(){
    return VehicleData.builder()
                      .vehicleId(GeneratorUtils.getRandomId())
                      .vehicleRegistrationNumber(GeneratorUtils.generateRegNumber())
                      .vehicleType(VehicleType.TRUCK)
                      .build();
  }
}