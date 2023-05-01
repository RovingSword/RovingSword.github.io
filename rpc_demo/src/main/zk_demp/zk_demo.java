package src.main.zk_demp;

import io.netty.handler.logging.LogLevel;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MXBean;
import java.util.Arrays;
import java.util.List;

public class zk_demo {

    private static final Logger logger = LoggerFactory.getLogger(zk_demo.class);
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;



    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .retryPolicy(retryPolicy)
            .build();
        zkClient.start();

        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, "/node2", true);
        PathChildrenCacheListener pathChildrenCacheListener = ((curatorFramework, pathChildrenCacheEvent) -> {
            //
            PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
            System.out.println(type);

        });

        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();

//        zkClient.create().forPath("/node2");
//        zkClient.create().forPath("/node2/00001");
        zkClient.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath("/node2/00001", "mummy".getBytes());
        Stat stat = zkClient.checkExists().forPath("/node2/00001");
        if(stat != null) logger.info("结点[{}]创建成功", stat);
        else logger.error("结点创建失败");

        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath("/node2/00002");

        String mummy = Arrays.toString(zkClient.getData().forPath("/node2/00001"));
        zkClient.setData().forPath("/node2/00001", "newData".getBytes());

        List<String> children = zkClient.getChildren().forPath("/node2");
        for (String child: children)
            System.out.print(child + " ");




    }

}


















