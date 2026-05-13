package ru.filimonov.eventhandlerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class EventHandlerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventHandlerServiceApplication.class, args);
  }

}
