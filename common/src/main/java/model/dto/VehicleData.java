package model.dto;

import lombok.Builder;
import model.enums.VehicleType;

@Builder
public record VehicleData(
    Long vehicleId,
    String vehicleRegistrationNumber,
    VehicleType vehicleType
) {
}