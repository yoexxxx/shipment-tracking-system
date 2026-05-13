package ru.filimonov.eventhandlerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "driver_id", unique = true)
  private Long driverId;

  @Column(name = "driver_name")
  private String driverName;

  @Column(name = "driver_last_name")
  private String driverLastName;

  @Column(name = "driver_license_number", unique = true)
  private String driverLicenseNumber;

  @Column(name = "contact_phone_number", unique = true)
  private String contactPhoneNumber;

  @Column(name = "transit_company")
  private String transitCompany;
}
