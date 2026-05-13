package ru.filimonov.eventhandlerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.VehicleType;

@Entity
@Table(name = "vehicle_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "vehicle_id", unique = true)
  private Long vehicleId;

  @Column(name = "vehicle_registration_number", unique = true)
  private String vehicleRegistrationNumber;

  @Column(name = "vehicle_type")
  private VehicleType vehicleType;
}
