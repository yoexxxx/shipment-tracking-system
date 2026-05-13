package model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record RouteData(
    String startPoint,
    String endPoint,
    String currentPoint,
    LocalDateTime plannedArrival
) {
}