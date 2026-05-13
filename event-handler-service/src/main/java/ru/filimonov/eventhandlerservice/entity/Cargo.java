package ru.filimonov.eventhandlerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "cargo_id", unique = true)
  private Long cargoId;

  @Column(name = "cargo_description")
  private String cargoDescription;

  @Column(name = "owner_company_name")
  private String ownerCompanyName;

  @Column(name = "weight")
  private Integer weight;

  @Column(name = "number_of_packages")
  private Integer numberOfPackages;

  @Column(name = "origin_country")
  private String originCountry;

  @Column(name = "destination_country")
  private String destinationCountry;

  @Column(name = "cargo_tracking_number", unique = true)
  private Long cargoTrackingNumber;
}
