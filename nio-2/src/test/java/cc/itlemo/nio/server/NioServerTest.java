package cc.itlemo.nio.server;

import java.io.IOException;

import cc.itlemon.nio.server.NioServer;
import cc.itlemon.nio.server.ReactorRunable;

public class NioServerTest {

	public static void main(String[] args) throws Exception{
		
		ReactorRunable runable = new ReactorRunable();
		
		new Thread(runable).start();
		
		NioServer server = new NioServer(12345,runable);
		
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
