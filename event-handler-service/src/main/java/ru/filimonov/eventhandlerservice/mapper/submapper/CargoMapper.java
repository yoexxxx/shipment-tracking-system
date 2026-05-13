package ru.filimonov.eventhandlerservice.mapper.submapper;

import model.dto.CargoData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.filimonov.eventhandlerservice.dto.response.subdto.CargoResponse;
import ru.filimonov.eventhandlerservice.entity.Cargo;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CargoMapper {
  Cargo toCargoEntity(CargoData cargoData);

  CargoResponse toCargoResponse(Cargo cargo);
}
