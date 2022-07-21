package com.lethanh98.demo.grpc;

import grpcNotUseProtoBuff.StudentService.CreateStudentRequest;
import grpcNotUseProtoBuff.StudentService.CreateStudentResponse;
import grpcNotUseProtoBuff.StudentService.StudentServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class StudentGrpcService extends StudentServiceImplBase {

  @Override
  public void create(CreateStudentRequest request,
      StreamObserver<CreateStudentResponse> responseObserver) {
    responseObserver.onNext(new CreateStudentResponse("ok"));
    responseObserver.onCompleted();
  }
}

