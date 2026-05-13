package ru.filimonov.eventhandlerservice.dto.response.subdto;

import io.swagger.v3.oas.annotations.media.Schema;
import model.enums.VehicleType;

@Schema(description = "Nested DTO with complete information about Vehicle")
public record VehicleResponse(
    @Schema(description = "EntityId", example = "2")
    Long id,

    @Schema(description = "VehicleId", example = "123id")
    Long vehicleId,

    @Schema(description = "Vehicle Registration Number", example = "us223-56d")
    String vehicleRegistrationNumber,

    @Schema(description = "Vehicle type", example = "TRUCK")
    VehicleType vehicleType
) {
}
