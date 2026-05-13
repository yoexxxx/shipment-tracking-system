package ru.filimonov.eventhandlerservice.dto.response.subdto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Nested DTO with complete information about Cargo")
public record CargoResponse(
    @Schema(description = "EntityId", example = "2")
    Long id,

    @Schema(description = "CargoId", example = "123id")
    Long cargoId,

    @Schema(description = "Cargo Description", example = "Smartphone Google Pixel 10")
    String cargoDescription,

    @Schema(description = "Owner Company Name", example = "TradeUp")
    String ownerCompanyName,

    @Schema(description = "Weight value of cargo", example = "10")
    Integer weight,

    @Schema(description = "Number Of Packages", example = "150")
    Integer numberOfPackages,

    @Schema(description = "Origin Country", example = "USA")
    String originCountry,

    @Schema(description = "Destination Country", example = "Italy")
    String destinationCountry,

    @Schema(description = "Unique Cargo Tracking Number", example = "15466")
    Long cargoTrackingNumber
) {
}
