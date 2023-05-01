package src.main.java.com.example.rpc_demo.Client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        // see if there is a connection
        if(channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            // see whether the connection is Alive
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("channel map size: [{}]", channelMap.size());
    }
}




















