package ru.filimonov.eventhandlerservice.dto.response.subdto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Nested DTO with complete information about Driver")
public record DriverResponse(
    @Schema(description = "EntityId", example = "2")
    Long id,

    @Schema(description = "DriverId", example = "56id")
    Long driverId,

    @Schema(description = "Driver First Name", example = "David")
    String driverName,

    @Schema(description = "Driver Lastname", example = "Someone")
    String driverLastName,

    @Schema(description = "Driver License Number", example = "4567forgef")
    String driverLicenseNumber,

    @Schema(description = "Phone Number of driver", example = "+3452005775")
    String contactPhoneNumber,

    @Schema(description = "Transit Company name", example = "someLTD")
    String transitCompany
) {
}
