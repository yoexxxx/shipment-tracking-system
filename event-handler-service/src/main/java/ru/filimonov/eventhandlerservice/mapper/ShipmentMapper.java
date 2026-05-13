package ru.filimonov.eventhandlerservice.mapper;

import model.dto.ShipmentData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import ru.filimonov.eventhandlerservice.dto.response.ShipmentResponse;
import ru.filimonov.eventhandlerservice.entity.Shipment;
import ru.filimonov.eventhandlerservice.mapper.submapper.CargoMapper;
import ru.filimonov.eventhandlerservice.mapper.submapper.DriverMapper;
import ru.filimonov.eventhandlerservice.mapper.submapper.RouteMapper;
import ru.filimonov.eventhandlerservice.mapper.submapper.VehicleMapper;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {
    CargoMapper.class,
    DriverMapper.class,
    RouteMapper.class,
    VehicleMapper.class
})
public interface ShipmentMapper {
  @Mapping(source = "cargoData", target = "cargo")
  @Mapping(source = "driverData", target = "driver")
  @Mapping(source = "routeData", target = "route")
  @Mapping(source = "vehicleData", target = "vehicle")
  Shipment toShipmentEntity(ShipmentData shipmentData);

  ShipmentResponse toShipmentResponse(Shipment shipment);

  void updateShipmentEntity(ShipmentData shipmentData, @MappingTarget Shipment shipmentEntity);

  List<ShipmentResponse> toShipmentResponseList(List<Shipment> shipments);
}
