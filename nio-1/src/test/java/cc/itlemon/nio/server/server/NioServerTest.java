package cc.itlemon.nio.server.server;

import java.io.IOException;

import cc.itlemon.nio.server.server.NioServer;

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
