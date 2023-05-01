package src.main.java.com.example.rpc_demo.Serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import src.main.java.com.example.rpc_demo.Entity.RpcRequest;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer{

    /**
     * 每个线程有自己的kryo对象, 保证线程安全
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() ->{
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true); // 注册行为 ?
        kryo.setRegistrationRequired(false); // 关闭循环引用
        return kryo;

    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();

        } catch (Exception e) {
            throw new SerializeException("序列化失败");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e){
            throw new SerializeException("反序列化失败");
        }
    }
}


















