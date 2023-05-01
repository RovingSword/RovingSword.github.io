package src.main.java.com.example.rpc_demo.Server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.com.example.rpc_demo.Codec.NettyKryoDecoder;
import src.main.java.com.example.rpc_demo.Codec.NettyKryoEncoder;
import src.main.java.com.example.rpc_demo.Entity.RpcRequest;
import src.main.java.com.example.rpc_demo.Entity.RpcResponse;
import src.main.java.com.example.rpc_demo.Serialize.KryoSerializer;

public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final int port;

    public NettyServer(int port){
        this.port = port;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        KryoSerializer kryoSerializer = new KryoSerializer();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.TCP_NODELAY, true)
             .childOption(ChannelOption.SO_KEEPALIVE, true)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                    socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                    socketChannel.pipeline().addLast(new NettyServerHandler());
                }
            });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("exception when start server: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
















