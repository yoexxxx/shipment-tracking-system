package model.dto;

import lombok.Builder;

@Builder
public record CargoData(
    Long cargoId,
    String cargoDescription,
    String ownerCompanyName,
    Integer weight,
    Integer numberOfPackages,
    String originCountry,
    String destinationCountry,
    Long cargoTrackingNumber
) {
}