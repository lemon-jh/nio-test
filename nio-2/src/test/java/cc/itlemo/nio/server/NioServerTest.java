package cc.itlemo.nio.server;

import com.itlemon.nio.server.NioServer;

import java.io.IOException;

public class NioServerTest {

	public static void main(String[] args) {
		NioServer server = new NioServer(12345);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
