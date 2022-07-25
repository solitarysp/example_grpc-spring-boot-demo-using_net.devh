package com.lethanh98.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
  @Value("${grpc.manual.address}")
  public String address;
  @Value("${grpc.manual.port}")
  public Integer port;
  @Bean
  public ManagedChannel grpcChannelConfig() {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(address, port)
        .usePlaintext()
        .build();
    return channel;
  }
}
