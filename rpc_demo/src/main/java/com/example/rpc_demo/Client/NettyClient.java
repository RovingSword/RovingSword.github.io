package src.main.java.com.example.rpc_demo.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.com.example.rpc_demo.Codec.NettyKryoDecoder;
import src.main.java.com.example.rpc_demo.Codec.NettyKryoEncoder;
import src.main.java.com.example.rpc_demo.Entity.RpcRequest;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;
import src.main.java.com.example.rpc_demo.Serialize.KryoSerializer;

public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final String host;
    private final int port;
    private static final Bootstrap b;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

static{
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .handler(new ChannelInitializer<SocketChannel>() {
@Override
protected void initChannel(SocketChannel ch) throws Exception {
        /**
         * 自定义序列化编解码器
         */
        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
        ch.pipeline().addLast(new NettyClientHandler());
        }
        });
        }

/**
 *
 * @param rpcRequest 消息体
 * @return rpcResponse
 */
public RpcResponse sendMessage(RpcRequest rpcRequest){
        try {
        ChannelFuture f = b.connect(host, port).sync();
        logger.info("client connect {}", host + ":" + port);
        Channel futureChannel = f.channel();
        logger.info("sending message...");
        if(futureChannel != null){
        futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
        if(future.isSuccess()) {
        logger.info("client send message: [{}]", rpcRequest.toString());
        } else {
        logger.error("Send msg failed: " + future.cause());
        }
        });
        // 阻塞等待
        futureChannel.closeFuture().sync();
        //
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        return futureChannel.attr(key).get();
        }
        } catch (InterruptedException e) {
        logger.error("exception when connect server:", e);
        }

        return null;
        }

}









