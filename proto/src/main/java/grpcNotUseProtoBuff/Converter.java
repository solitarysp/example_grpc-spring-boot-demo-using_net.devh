package grpcNotUseProtoBuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.grpc.MethodDescriptor.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Converter {

  private static final Gson gson =
      new GsonBuilder().registerTypeAdapter(byte[].class, new TypeAdapter<byte[]>() {
        @Override
        public void write(JsonWriter out, byte[] value) throws IOException {
          out.value(Base64.getEncoder().encodeToString(value));
        }

        @Override
        public byte[] read(JsonReader in) throws IOException {
          return Base64.getDecoder().decode(in.nextString());
        }
      }).create();

  public static <T> Marshaller<T> marshallerFor(Class<T> clz) {
    return new Marshaller<T>() {
      @Override
      public InputStream stream(T value) {
        return new ByteArrayInputStream(gson.toJson(value, clz).getBytes(StandardCharsets.UTF_8));
      }

      @Override
      public T parse(InputStream stream) {
        return gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), clz);
      }
    };
  }
}
