package model.dto;

import lombok.Builder;
import model.enums.ShipmentStatus;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record ShipmentData(
    String shipmentId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    ShipmentStatus shipmentStatus,
    CargoData cargoData,
    DriverData driverData,
    RouteData routeData,
    VehicleData vehicleData
) {
}
