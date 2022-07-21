package com.lethanh98.demo.grpcNotUseProtoBuff;

import grpcNotUseProtoBuff.StudentService.CreateStudentRequest;
import grpcNotUseProtoBuff.StudentService.StudentServiceBlockingStub;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNameUserClientManualNotUseProtobuffConfig {

  final StudentServiceBlockingStub studentServiceBlockingStub;
  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public GetNameUserClientManualNotUseProtobuffConfig(
      StudentServiceBlockingStub studentServiceBlockingStub) {
    this.studentServiceBlockingStub = studentServiceBlockingStub;
  }


  @Autowired
  public void studentServiceBlockingStubCreate() {
    scheduler.schedule(() -> {
      try {
        var request = new CreateStudentRequest("Thanh");
        var getNameUserResponse = studentServiceBlockingStub.createStudent(request);
        System.out.println(
            "Manual config studentServiceBlockingStubCreate: " + getNameUserResponse.getValue());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, 5, TimeUnit.SECONDS);
  }
}
