package src.main.java.com.example.rpc_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import src.main.java.com.example.rpc_demo.Server.NettyServer;

@SpringBootApplication
public class RpcServer {

	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);

		new NettyServer(8899).run();
	}

}













