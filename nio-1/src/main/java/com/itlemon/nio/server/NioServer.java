package com.itlemon.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhang on 2016/8/21.
 */
public class NioServer {

    private int port;

    private ServerSocketChannel ssc;

    private Selector selector;

    public NioServer(int port) {
        this.port = port;
    }

    public void start () throws IOException {

        selector = Selector.open();//新建多路复用selector

        ssc =  ServerSocketChannel.open();// //新建channel

        ssc.configureBlocking(false);//设置非阻塞

        ssc.socket().bind(new InetSocketAddress(port),1024);// //端口、块大小

        ssc.register(selector,SelectionKey.OP_ACCEPT);

        
        System.out.println("==>server is start");
        
        for (;;) {

            try {

                selector.select(1000l);

                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> ketIt = keys.iterator();

                while (ketIt.hasNext()){
                	
                    SelectionKey key = ketIt.next();
                    
                    ketIt.remove();
                    
                    if(key.isAcceptable()){
                    	accep(key);
                    }
                    
                    if(key.isWritable()){
                    	write(key);
                    }
                    
                    if(key.isReadable()){
                    	read(key);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void accep(SelectionKey key) {
    	ServerSocketChannel channel = (ServerSocketChannel)key.channel();
    	try {
			SocketChannel sc = channel.accept();
			sc.configureBlocking(false);
			Selector selector = key.selector();
			sc.register(selector, SelectionKey.OP_READ);
			System.out.println("客户端请求连接事件");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void write(SelectionKey key) {
    	ServerSocketChannel channel = (ServerSocketChannel)key.channel();
    	try {
    		
			SocketChannel sc = channel.accept();
			
			ByteBuffer buffer = ByteBuffer.allocate(1);
			
			sc.read(buffer);
			
			buffer.flip();
			
			//System.out.println(new String(buffer.get));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void read(SelectionKey key) {
    	
    }
}
