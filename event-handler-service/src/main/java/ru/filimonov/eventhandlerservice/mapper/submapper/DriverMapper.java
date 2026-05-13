package ru.filimonov.eventhandlerservice.mapper.submapper;

import model.dto.DriverData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.filimonov.eventhandlerservice.dto.response.subdto.DriverResponse;
import ru.filimonov.eventhandlerservice.entity.Driver;

@Mapper(componentModel = ComponentModel.SPRING)
public interface DriverMapper {
  Driver toDriverEntity(DriverData driverData);

  DriverResponse toDriverResponse(Driver driver);
}
