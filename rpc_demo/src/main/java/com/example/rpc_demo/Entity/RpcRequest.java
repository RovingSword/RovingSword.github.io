package src.main.java.com.example.rpc_demo.Entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class RpcRequest {
    private static final long serialVersionUID = 1991L;
    private String requestId;
    public String interfaceName;
    public String methodName;
    public Object[] parameters;
    public Class<?> paramTypes;
    public RpcMessageType rpcMessageType;
    public  String version;
    public String group;

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().
    }
}
















