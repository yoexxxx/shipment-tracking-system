package ru.filimonov.eventhandlerservice.exceptionhandler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Server-error response")
public record ErrorResponse(
    @Schema(description = "Error status", example = "404")
    int status,

    @Schema(description = "Error type", example = "BAD_REQUEST")
    String error,

    @Schema(
        description = "Error Message",
        example = "Shipment Entity, with shipmentId - 123id, not found!"
    )
    String errorMessage,

    @Schema(description = "Error timestamp", example = "2026-05-08'T'12:45")
    LocalDateTime timestamp
) {
}
