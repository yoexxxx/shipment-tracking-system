package ru.filimonov.eventhandlerservice.mapper.submapper;

import model.dto.VehicleData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.filimonov.eventhandlerservice.dto.response.subdto.VehicleResponse;
import ru.filimonov.eventhandlerservice.entity.Vehicle;

@Mapper(componentModel = ComponentModel.SPRING)
public interface VehicleMapper {
  Vehicle toVehicleEntity(VehicleData vehicleData);

  VehicleResponse toVehicleResponse(Vehicle vehicle);
}
