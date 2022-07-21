package grpcNotUseProtoBuff;

import static grpcNotUseProtoBuff.Converter.marshallerFor;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;

import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.MethodDescriptor.MethodType;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.Data;

public class StudentService {

  private static final String SERVICE_NAME = "com.lethanh.studentService";

  @Data
  @AllArgsConstructor
  public static final class CreateStudentRequest {

    String name;
  }

  @Data
  @AllArgsConstructor
  public static final class CreateStudentResponse {

    String value;
  }

  // Tao mot impl cho server impl logic
  public static abstract class StudentServiceImplBase implements BindableService {

    public abstract void create(
        StudentService.CreateStudentRequest request,
        StreamObserver<CreateStudentResponse> responseObserver);

    @Override
    public final ServerServiceDefinition bindService() {
      ServerServiceDefinition.Builder ssd = ServerServiceDefinition.builder(SERVICE_NAME);
      ssd.addMethod(CREATE_METHOD, ServerCalls.asyncUnaryCall(
          (request, responseObserver) -> create(request, responseObserver)));
      return ssd.build();
    }
  }

  // tao 1 BlockingStub cho client thuc hien goi
  public static final class StudentServiceBlockingStub extends
      io.grpc.stub.AbstractStub<StudentServiceBlockingStub> {

    public StudentServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StudentServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StudentServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StudentServiceBlockingStub(channel, callOptions);
    }


    public CreateStudentResponse createStudent(CreateStudentRequest request) {
      return blockingUnaryCall(
          getChannel(), CREATE_METHOD, getCallOptions(), request);
    }
  }

  static final MethodDescriptor<CreateStudentRequest, CreateStudentResponse> CREATE_METHOD =
      MethodDescriptor.newBuilder(
              marshallerFor(CreateStudentRequest.class),
              marshallerFor(CreateStudentResponse.class))
          .setFullMethodName(
              MethodDescriptor.generateFullMethodName(SERVICE_NAME, "Create"))
          .setType(MethodType.UNARY)
          .setSampledToLocalTracing(true)
          .build();

}
