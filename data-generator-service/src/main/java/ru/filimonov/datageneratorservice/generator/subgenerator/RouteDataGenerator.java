package ru.filimonov.datageneratorservice.generator.subgenerator;

import model.dto.RouteData;
import org.springframework.stereotype.Component;
import ru.filimonov.datageneratorservice.annotation.GenerateData;
import utils.GeneratorUtils;

import java.time.LocalDateTime;

@Component
public class RouteDataGenerator {
  @GenerateData
  public RouteData generateRouteData(){
    return RouteData.builder()
                    .startPoint(GeneratorUtils.FAKER.address().cityName())
                    .endPoint(GeneratorUtils.FAKER.address().cityName())
                    .currentPoint(GeneratorUtils.FAKER.address().cityName())
                    .plannedArrival(LocalDateTime.now().plusDays(10))
                    .build();
  }
}