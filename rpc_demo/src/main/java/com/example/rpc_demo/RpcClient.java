package src.main.java.com.example.rpc_demo;

import org.springframework.boot.SpringApplication;
import src.main.java.com.example.rpc_demo.Client.NettyClient;
import src.main.java.com.example.rpc_demo.Entity.RpcRequest;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;

public class RpcClient {

    public static void main(String[] args) {

        SpringApplication.run(RpcClient.class, args);

        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName("interface")
                .methodName("CNM!!!").build();
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8899);
        for(int i = 0; i < 3; i ++) {
            nettyClient.sendMessage(rpcRequest);
        }
        RpcResponse rpcResponse = nettyClient.sendMessage(rpcRequest);
        System.out.println(rpcResponse.toString());
    }
}
