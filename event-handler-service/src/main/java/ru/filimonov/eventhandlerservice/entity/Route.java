package ru.filimonov.eventhandlerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "start_point")
  private String startPoint;

  @Column(name = "end_point")
  private String endPoint;

  @Column(name = "current_point")
  private String currentPoint;

  @Column(name = "planned_arrival")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime plannedArrival;
}
