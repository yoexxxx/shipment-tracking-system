package ru.filimonov.datageneratorservice.generator.subgenerator;

import model.dto.CargoData;
import org.springframework.stereotype.Component;
import ru.filimonov.datageneratorservice.annotation.GenerateData;
import utils.GeneratorUtils;

@Component
public class CargoDataGenerator {
  @GenerateData
  public CargoData generateCargoData(){
    return CargoData.builder()
                    .cargoId(GeneratorUtils.getRandomId())
                    .cargoDescription(GeneratorUtils.generateCargoDesc())
                    .ownerCompanyName(GeneratorUtils.generateCompanyName())
                    .weight(GeneratorUtils.getRandomInt())
                    .numberOfPackages(GeneratorUtils.getRandomInt())
                    .originCountry(GeneratorUtils.FAKER.address().country())
                    .destinationCountry(GeneratorUtils.FAKER.address().country())
                    .cargoTrackingNumber(GeneratorUtils.getRandomTrackingNumber())
                    .build();
  }
}
