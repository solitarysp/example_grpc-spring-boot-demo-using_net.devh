package com.lethanh98.demo.grpc;

import com.lethanh98.demo.query.grpc.GetNameUserRequest;
import com.lethanh98.demo.query.grpc.GetNameUserResponse;
import com.lethanh98.demo.query.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.var;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserQueryGrpcService extends UserServiceGrpc.UserServiceImplBase {

  @Override
  public void getNameUser(GetNameUserRequest request,
      StreamObserver<GetNameUserResponse> responseObserver) {
    var response = GetNameUserResponse.newBuilder()
        .setName("Le Thanh")
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

}
