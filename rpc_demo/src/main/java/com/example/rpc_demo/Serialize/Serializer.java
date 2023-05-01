package src.main.java.com.example.rpc_demo.Serialize;

public interface Serializer {

    /**
     * 序列化
     *
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     *
     * @param bytes 字节数组
     * @param clazz 类
     * @return 反序列化的对象
     * @param <T> 泛型参数
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
