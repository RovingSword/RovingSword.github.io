package src.main.java.com.example.rpc_demo.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import src.main.java.com.example.rpc_demo.Serialize.Serializer;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class NettyKryoDecoder extends ByteToMessageDecoder {

    private final Serializer serializer;
    private final Class<?> genericClass;

    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= BODY_LENGTH) {
            byteBuf.markReaderIndex();
            int dataLength = byteBuf.readInt();
            if(dataLength < 0 || byteBuf.readableBytes() < 0){
                log.error("data length or byteBuf readable bytes is not valid");
                return ;
            }
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                return ;
            }

            byte[] body = new byte[dataLength];
            byteBuf.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            list.add(obj);
            log.info("successfully decode ByteBuf to Object");

        }
    }
}
















