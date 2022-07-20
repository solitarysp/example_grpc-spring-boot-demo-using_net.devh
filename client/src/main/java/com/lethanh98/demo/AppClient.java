package com.lethanh98.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class AppClient {

  public static void main(String[] args) {
    SpringApplication.run(AppClient.class, args);
  }
}
