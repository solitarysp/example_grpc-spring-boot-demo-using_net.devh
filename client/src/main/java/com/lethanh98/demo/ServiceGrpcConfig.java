package com.lethanh98.demo;

import com.lethanh98.demo.query.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceGrpcConfig {

  @Autowired
  ManagedChannel managedChannel;

  @Bean
  public UserServiceGrpc.UserServiceFutureStub userServiceFutureStub() {
    return UserServiceGrpc.newFutureStub(managedChannel);
  }

  @Bean
  public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub() {
    return UserServiceGrpc.newBlockingStub(managedChannel);
  }
}
