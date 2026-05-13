package ru.filimonov.eventhandlerservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.filimonov.eventhandlerservice.entity.Shipment;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

  @EntityGraph(attributePaths = {
      "cargo",
      "driver",
      "route",
      "vehicle"
  })
  Optional<Shipment> findByShipmentId(String shipmentId);
}
