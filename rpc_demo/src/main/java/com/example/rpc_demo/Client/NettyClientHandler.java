package src.main.java.com.example.rpc_demo.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcResponse rpcResponse = (RpcResponse) msg;
            logger.info("client receive msg: [{}]", rpcResponse.toString());
            // 声明 Attribute 对象 -- > ?
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            // 将返回结果保存到 AttributeMap 中, which is shared by channels
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception: " + cause);
        ctx.channel().close();
    }
}
