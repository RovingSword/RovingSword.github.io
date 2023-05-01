package src.main.java.com.example.rpc_demo.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;

    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            log.info("client receive msg: [{}]", msg);
            if (msg instanceof RpcResponse) {
                RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg;
                unprocessedRequests.complete(rpcResponse);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     *
     * @param ctx ctx
     * @param evt evt
     * @throws Exception
     * @function 心跳机制相关 ?
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}























