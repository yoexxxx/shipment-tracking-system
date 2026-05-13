package model.dto;

import lombok.Builder;

@Builder
public record DriverData(
    Long driverId,
    String driverName,
    String driverLastName,
    String driverLicenseNumber,
    String contactPhoneNumber,
    String transitCompany
) {
}
