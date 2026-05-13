package ru.filimonov.eventhandlerservice.dto.response.subdto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Nested DTO with complete information about Route")
public record RouteResponse(
    @Schema(description = "EntityId", example = "2")
    Long id,

    @Schema(description = "Start point (city)", example = "New-York")
    String startPoint,

    @Schema(description = "End point (city)", example = "Berlin")
    String endPoint,

    @Schema(description = "Current point (city)", example = "Paris")
    String currentPoint,

    @Schema(description = "Planned Arrival timestamp", example = "2026-05-08'T'12:45")
    LocalDateTime plannedArrival
) {
}
