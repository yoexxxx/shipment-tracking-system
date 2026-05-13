package ru.filimonov.eventhandlerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.filimonov.eventhandlerservice.dto.response.ShipmentResponse;
import ru.filimonov.eventhandlerservice.service.ShipmentService;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@Validated
@RequiredArgsConstructor
@Tag(name = "Shipment Management", description = "Operations with shipments")
public class ShipmentController {
  private final ShipmentService shipmentService;

  @Operation(
      summary = "Get shipment information",
      description = "Obtaining complete information about the shipment"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Shipment information successfully received"
      ),
      @ApiResponse(
          responseCode = "400", description = "Null or Blank variable for request",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Shipment information not found",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/{shipmentId}")
  public ResponseEntity<ShipmentResponse> getShipment(
      @PathVariable
      @NotBlank(message = "shipmentId cannot be blank!")
      String shipmentId) {
    return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
  }

  @Operation(
      summary = "Get all shipments information",
      description = "Obtaining complete information about all available shipment"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "All Shipments information successfully received"
      ),
      @ApiResponse(
          responseCode = "204", description = "No available shipments"
      )
  })
  @GetMapping("/all")
  public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
    List<ShipmentResponse> allShipments = shipmentService.getAllShipments();
    return allShipments.isEmpty() ? ResponseEntity.noContent().build()
                                  : ResponseEntity.ok(allShipments);
  }

  @Operation(
      summary = "Delete shipment information",
      description = "Delete all information about the current shipment"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Shipment information successfully deleted"
      ),
      @ApiResponse(
          responseCode = "400", description = "Null or Blank variable for request",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Shipment information for deletion not found",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  @DeleteMapping("/{shipmentId}")
  public ResponseEntity<Void> deleteShipment(
      @PathVariable
      @NotBlank(message = "shipmentId cannot be blank!")
      String shipmentId) {
    return shipmentService.deleteShipmentById(shipmentId)
                          ? ResponseEntity.noContent().build()
                          : ResponseEntity.notFound().build();
  }
}
