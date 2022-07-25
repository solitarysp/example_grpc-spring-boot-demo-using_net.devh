package com.lethanh98.demo.grpc;

import com.lethanh98.demo.model.UserModel;
import com.lethanh98.demo.query.grpc.GetNameUserBidirectionalStreamingRequest;
import com.lethanh98.demo.query.grpc.GetNameUserBidirectionalStreamingResponse;
import com.lethanh98.demo.query.grpc.GetNameUserClientStreamingRequest;
import com.lethanh98.demo.query.grpc.GetNameUserClientStreamingResponse;
import com.lethanh98.demo.query.grpc.GetNameUserClientStreamingResponse.UserMsg;
import com.lethanh98.demo.query.grpc.GetNameUserServerStreamingRequest;
import com.lethanh98.demo.query.grpc.GetNameUserServerStreamingResponse;
import com.lethanh98.demo.query.grpc.GetNameUserUnaryRequest;
import com.lethanh98.demo.query.grpc.GetNameUserUnaryResponse;
import com.lethanh98.demo.query.grpc.UserServiceKindsGrpc;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.var;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserServiceKindsService extends UserServiceKindsGrpc.UserServiceKindsImplBase {

  public static final List<UserModel> USERES = Arrays.asList(
      new UserModel("1", "thanh"),
      new UserModel("2", "thanh"),
      new UserModel("3", "thanh"),
      new UserModel("4", "thanh"),
      new UserModel("5", "thanh"),
      new UserModel("6", "thanh"),
      new UserModel("7", "tuan")
  );

  // Unary : 1 request trả về 1 response
  @Override
  public void getNameUserUnary(GetNameUserUnaryRequest request,
      StreamObserver<GetNameUserUnaryResponse> responseObserver) {
    var findUserOp = USERES.stream().filter(user -> Objects.equals(user.getId(), request.getId()))
        .findFirst();
    var response = GetNameUserUnaryResponse.newBuilder();

    findUserOp.map(user -> {
      response.setId(user.getId());
      response.setName(user.getName());
      return response;
    }).map(responseBuilder -> {
      responseObserver.onNext(responseBuilder.build());
      return responseBuilder;
    }).orElseGet(() -> {
      responseObserver.onError(new RuntimeException("Not found"));
      return null;
    });

    responseObserver.onCompleted();
  }

  // Server Streaming: 1 request trả về một Stream response
  // dữ liệu sẽ được trả về dần dần, client sẽ sử lý từng dữ liệu một không cần đợi tất cả dữ liệu gửi xong mới sử lý
  @Override
  public void getNameUserServerStreaming(GetNameUserServerStreamingRequest request,
      StreamObserver<GetNameUserServerStreamingResponse> responseObserver) {
    USERES.stream()
        .filter(user -> user.getName().equals(request.getName()))
        .forEach(user -> {
          var response = GetNameUserServerStreamingResponse.newBuilder()
              .setName(user.getName())
              .setId(user.getId())
              .build();
          responseObserver.onNext(response);
        });
    responseObserver.onCompleted();

  }


  // Nhiều request trả vè 1 response.
  // Sau khi client gửi hoàn tất tất cả dữ liệu thì sẽ bắt đầu tính toán để trả về dữ liệu cho client
  @Override
  public StreamObserver<GetNameUserClientStreamingRequest> getNameUserClientStreaming(
      StreamObserver<GetNameUserClientStreamingResponse> responseObserver) {
    return new StreamObserver<GetNameUserClientStreamingRequest>() {
      List<String> ids = new ArrayList<>();

      @Override
      public void onNext(GetNameUserClientStreamingRequest value) {
        ids.add(value.getId());
      }

      @Override
      public void onError(Throwable t) {
      }

      @Override
      public void onCompleted() {
        var response = GetNameUserClientStreamingResponse.newBuilder();
        var userMsgs = USERES.stream()
            .filter(user -> ids.stream().anyMatch(s -> s.equals(user.getId())))
            .map(user -> {
              return UserMsg.newBuilder()
                  .setId(user.getId())
                  .setName(user.getName())
                  .build();

            }).collect(Collectors.toList());
        response.addAllUsers(userMsgs);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
      }
    };
  }

  @Override
  public StreamObserver<GetNameUserBidirectionalStreamingRequest> getNameUserBidirectionalStreaming(
      StreamObserver<GetNameUserBidirectionalStreamingResponse> responseObserver) {
    return new StreamObserver<GetNameUserBidirectionalStreamingRequest>() {

      @Override
      public void onNext(GetNameUserBidirectionalStreamingRequest value) {
        USERES.stream().filter(userModel -> userModel.getName().equals(value.getName()))
            .map(userModel -> GetNameUserBidirectionalStreamingResponse.newBuilder()
                .setId(userModel.getId())
                .setName(userModel.getName())
                .setNumberRequest(value.getNumberRequest())
                .build()
            ).forEach(response -> {
              responseObserver.onNext(response);
            });

      }
      @Override
      public void onError(Throwable t) {}

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }

}
