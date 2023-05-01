package src.main.java.com.example.rpc_demo.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import src.main.java.com.example.rpc_demo.Codec.RpcMessageDecoder;
import src.main.java.com.example.rpc_demo.Codec.RpcMessageEncoder;
import sun.rmi.runtime.RuntimeUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyRpcServer {
    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

    public void registerService(Object service, RpcServiceProperties rpcServiceProperties){
        serviceProvider.publishService(service, rpcServiceProperties);
    }

    public void start() throws UnknownHostException {
        CustomShutdownHook.getCustomeShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                NettyRuntime.availableProcessors() * 2,
                ThreadPoolFactoryUtils.createThreadFactory("service-handler-group", false)
        );
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(serviceHandlerGroup, new NettyServerHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(host, PORT).sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("启动server错误: ", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}
















