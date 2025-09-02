package com.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.proj.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class NotificationService {

  public static void main(String[] args) {
    SpringApplication.run(NotificationService.class, args);
  }
  
  @KafkaListener(topics = "notificationTopic")
  public void handledNotification(OrderPlacedEvent orderPlaceEvent) {
	  //  send out an email notification
	  log.info("Receved Notification for Order - {}", orderPlaceEvent.getOrderNumber());
  }

}