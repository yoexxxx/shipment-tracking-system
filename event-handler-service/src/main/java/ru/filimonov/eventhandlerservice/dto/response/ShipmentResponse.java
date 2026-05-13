package ru.filimonov.eventhandlerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import model.enums.ShipmentStatus;
import ru.filimonov.eventhandlerservice.dto.response.subdto.CargoResponse;
import ru.filimonov.eventhandlerservice.dto.response.subdto.DriverResponse;
import ru.filimonov.eventhandlerservice.dto.response.subdto.RouteResponse;
import ru.filimonov.eventhandlerservice.dto.response.subdto.VehicleResponse;

import java.time.LocalDateTime;

@Schema(description = "Server-response with complete information about shipment")
public record ShipmentResponse(
    @Schema(description = "EntityId", example = "1")
    Long id,

    @Schema(description = "ShipmentId", example = "123id")
    String shipmentId,

    @Schema(description = "CreatedAt timestamp", example = "2026-05-08'T'12:45")
    LocalDateTime createdAt,

    @Schema(description = "Shipment Status", example = "IN_TRANSIT")
    ShipmentStatus shipmentStatus,

    @Schema(description = "DTO with complete information about Cargo")
    CargoResponse cargo,

    @Schema(description = "DTO with complete information about Driver")
    DriverResponse driver,

    @Schema(description = "DTO with complete information about Route")
    RouteResponse route,

    @Schema(description = "DTO with complete information about Vehicle")
    VehicleResponse vehicle
) {
}
