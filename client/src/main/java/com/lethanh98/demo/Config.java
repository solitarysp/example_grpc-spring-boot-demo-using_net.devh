package com.lethanh98.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public ManagedChannel grpcChannelConfig() {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress("server", 8081)
        .usePlaintext()
        .build();
    return channel;
  }
}
