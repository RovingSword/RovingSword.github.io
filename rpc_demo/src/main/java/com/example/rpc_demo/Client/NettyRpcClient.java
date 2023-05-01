package src.main.java.com.example.rpc_demo.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import src.main.java.com.example.rpc_demo.Entity.RpcMessage;
import src.main.java.com.example.rpc_demo.Entity.RpcRequest;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class NettyRpcClient {
//    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future ->{
            if (future.isSuccess()) {
                log.info("Client connects to [{}] successfully", inetSocketAddress.toString());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public Object sendRpcRequest(RpcRequest rpcRequest){
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        Channel channel = getChannel(inetSocketAddress);
        if(channel.isActive()){
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.set
        }
    }
}



























