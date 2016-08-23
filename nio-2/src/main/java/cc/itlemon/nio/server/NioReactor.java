package cc.itlemon.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhang on 2016/8/22.
 */
public class NioReactor {

    private ReactorThread thread;

    public NioReactor() {
        try {
			thread = new ReactorThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
        new Thread(thread).start();
    }

    public void registry (SelectionKey key) {
    	this.thread.selector.wakeup();
    	this.thread.registry(key);
    }

    private final class ReactorThread extends Thread {
    	
    	private final Selector selector;
    	
    	private ReactorThread () throws IOException {
			this.selector = Selector.open();
    	}
    	
    	public void registry (SelectionKey key) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            try {
                SocketChannel sh = channel.accept();
                sh.configureBlocking(false).register(this.selector,SelectionKey.OP_READ);
                System.out.println("=>"+Thread.currentThread().getName()+" 客户端连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}

        public void run() {
            for (;;) {
                try {
                    selector.select(1000l);

                    Set<SelectionKey> keys = selector.selectedKeys();

                    Iterator<SelectionKey> ketIt = keys.iterator();

                    while (ketIt.hasNext()){

                        SelectionKey key = ketIt.next();

                        ketIt.remove();

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
