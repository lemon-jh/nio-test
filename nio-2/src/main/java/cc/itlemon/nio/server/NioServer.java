package cc.itlemon.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * Created by zhang on 2016/8/22.
 */
public class NioServer {

    private ServerSocketChannel ssc;

    private Selector selector;
    
    private ReactorRunable runable;

    public NioServer(int port,ReactorRunable runable) {
        this.runable = runable;
        try {
			selector = Selector.open();
	        ssc =  ServerSocketChannel.open();// //新建channel
	        ssc.configureBlocking(false);//设置非阻塞
	        ssc.socket().bind(new InetSocketAddress(port),1024);// //端口、块大小
	        ssc.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void start () throws IOException {
        for (;;) {
            try {
                selector.select(1000l);
                Set<SelectionKey> keys = selector.selectedKeys();
                try{
                	for (SelectionKey key : keys ) {
						if(key.isValid() && key.isAcceptable()) {
							accept(key);
						}else{
							key.cancel();
						}
					}
                }finally{
                	keys.clear();
                	keys = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void accept(SelectionKey key) {
    	runable.registry(key);
    }

}
