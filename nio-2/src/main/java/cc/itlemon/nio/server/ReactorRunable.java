package cc.itlemon.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ReactorRunable implements  Runnable {
	
	private final Selector selector;
	
	public ReactorRunable() throws IOException {
		this.selector = Selector.open();
	}
	
	public void registry(SelectionKey key) {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sh = channel.accept();
            sh.configureBlocking(false).register(this.selector,SelectionKey.OP_READ);
            this.selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void run() {
		
		for(;;) {
			
            try {
            	
				selector.select(500l);
				
	            Set<SelectionKey> keys = selector.selectedKeys();
	            
	            try{
	            	
		            for(SelectionKey key : keys) {
		            	
		            	if(key.isValid() && key.isReadable()) {

		                    SocketChannel sc = (SocketChannel) key.channel();

		                    ByteBuffer buffer = ByteBuffer.allocate(12);

		                    sc.read(buffer);

		                    buffer.flip();

		                    byte [] bytes = buffer.array();

		                    String receive = new String(bytes);

		                    System.out.println("==>read bytes len ="  + bytes.length + "str = " + receive);
		                    
		                    sc.write(ByteBuffer.wrap("i got this".getBytes()));

		                }else{
		                    key.cancel();
		                }
		            }
		            
	            }finally{
	            	keys.clear();
	            	keys = null;
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}