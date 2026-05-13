package ru.filimonov.datageneratorservice.generator.subgenerator;

import model.dto.DriverData;
import org.springframework.stereotype.Component;
import ru.filimonov.datageneratorservice.annotation.GenerateData;
import utils.GeneratorUtils;

@Component
public class DriverDataGenerator {
  @GenerateData
  public DriverData generateDriverData(){
    return DriverData.builder()
                     .driverId(GeneratorUtils.getRandomId())
                     .driverName(GeneratorUtils.generateName())
                     .driverLastName(GeneratorUtils.generateLastName())
                     .driverLicenseNumber(GeneratorUtils.generateLicenseNumber())
                     .contactPhoneNumber(GeneratorUtils.FAKER.phoneNumber().phoneNumber())
                     .transitCompany(GeneratorUtils.FAKER.company().name())
                     .build();
  }
}
