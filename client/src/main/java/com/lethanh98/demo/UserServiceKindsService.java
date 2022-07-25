package com.lethanh98.demo;

import com.lethanh98.demo.query.grpc.GetNameUserClientStreamingRequest;
import com.lethanh98.demo.query.grpc.GetNameUserClientStreamingResponse;
import com.lethanh98.demo.query.grpc.GetNameUserServerStreamingRequest;
import com.lethanh98.demo.query.grpc.GetNameUserUnaryRequest;
import com.lethanh98.demo.query.grpc.UserServiceKindsGrpc;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.var;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceKindsService {

  @GrpcClient("testClient")
  UserServiceKindsGrpc.UserServiceKindsBlockingStub userServiceKindsBlockingStub;

  @GrpcClient("testClient")
  UserServiceKindsGrpc.UserServiceKindsStub userServiceKindsStub;
  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Autowired
  public void getNameUserUnary() {
    var run = new Runnable() {
      @Override
      public void run() {
        try {
          var request = GetNameUserUnaryRequest.newBuilder()
              .setId("1")
              .build();
          var getNameUserResponse = userServiceKindsBlockingStub.getNameUserUnary(request);
          System.out.println("Kinds getNameUserUnary: " + getNameUserResponse.getName());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    scheduler.schedule(run, 5, TimeUnit.SECONDS);
  }

  @Autowired
  public void getNameUserServerStreaming() {
    var run = new Runnable() {
      @Override
      public void run() {
        try {
          var request = GetNameUserServerStreamingRequest.newBuilder()
              .setName("thanh")
              .build();
          var getNameUserResponse = userServiceKindsBlockingStub.getNameUserServerStreaming(
              request);
          getNameUserResponse.forEachRemaining(response -> {
            System.out.println("Kinds getNameUserServerStreaming: " + response.getId() + " Name:"
                + response.getName());
          });
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    scheduler.schedule(run, 5, TimeUnit.SECONDS);
  }

  @Autowired
  public void getNameUserClientStreaming() {
    var run = new Runnable() {
      @Override
      public void run() {
        try {
          var getNameUserClientStreamingRequest = userServiceKindsStub.getNameUserClientStreaming(
              new StreamObserver<GetNameUserClientStreamingResponse>() {
                @Override
                public void onNext(GetNameUserClientStreamingResponse value) {
                  System.out.println(
                      "Kinds getNameUserClientStreaming response: " + value.getUsersList());
                }
                @Override
                public void onError(Throwable t) {}
                @Override
                public void onCompleted() { }
              }
          );
          for (int i = 0; i <= 4; i++) {
            getNameUserClientStreamingRequest.onNext(
                GetNameUserClientStreamingRequest.newBuilder()
                    .setId(String.valueOf(i))
                    .build()
            );
          }
          System.out.println("getNameUserClientStreaming client send request Done");
          getNameUserClientStreamingRequest.onCompleted();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
        ;
    scheduler.schedule(run, 5, TimeUnit.SECONDS);
  }
}
