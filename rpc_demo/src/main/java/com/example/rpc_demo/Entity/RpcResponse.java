package src.main.java.com.example.rpc_demo.Entity;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1992L;
    public String requestId;
    public Integer code;
    public String message;
    public T data;

    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());

    }
}










