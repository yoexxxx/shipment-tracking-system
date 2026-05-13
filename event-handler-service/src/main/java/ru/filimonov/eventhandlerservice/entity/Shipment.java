package ru.filimonov.eventhandlerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.ShipmentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "shipment_id", updatable = false, unique = true)
  private String shipmentId;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "shipment_status")
  private ShipmentStatus shipmentStatus;

  @JoinColumn(name = "cargo_id")
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Cargo cargo;

  @JoinColumn(name = "driver_id")
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Driver driver;

  @JoinColumn(name = "route_id")
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Route route;

  @JoinColumn(name = "vehicle_id")
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Vehicle vehicle;
}
