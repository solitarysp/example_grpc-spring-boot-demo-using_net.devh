package com.lethanh98.demo;

import com.lethanh98.demo.query.grpc.GetNameUserRequest;
import com.lethanh98.demo.query.grpc.UserServiceGrpc;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNameUserClientManualConfig {

  @Autowired
  UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Autowired
  public GetNameUserClientManualConfig() {
    scheduler.schedule(() -> {
      try {
        var request = GetNameUserRequest.newBuilder()
            .build();
        var getNameUserResponse = userServiceBlockingStub.getNameUser(request);
        System.out.println("Manual config GetNameUser: " + getNameUserResponse.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, 5, TimeUnit.SECONDS);
  }
}
