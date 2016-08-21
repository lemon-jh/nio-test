package com.itlemon.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhang on 2016/8/21.
 */
public class NioServer {

    private String host;

    private int port;

    private ServerSocketChannel ssc;

    private Selector selector;

    public NioServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start () throws IOException {

        selector = Selector.open();//新建多路复用selector

        ssc =  ServerSocketChannel.open();// //新建channel

        ssc.configureBlocking(false);//设置非阻塞

        ssc.socket().bind(new InetSocketAddress(port),1024);// //端口、块大小

        ssc.register(selector,SelectionKey.OP_ACCEPT);

        System.out.println("==>server is start");

    }

    class Dispatcher implements Runnable {

        public void run() {

            for (;;) {

                try {

                    selector.select(1000l);

                    selector.wakeup();

                    Set<SelectionKey> keys = selector.selectedKeys();

                    Iterator<SelectionKey> ketIt = keys.iterator();

                    while (ketIt.hasNext()){
                        SelectionKey key = ketIt.next();
                        ketIt.remove();
                        //处理对应key事件
                        handler(key);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        private void handler(SelectionKey key) {
            if(key.isValid()) {
                key.readyOps();
                //System.out.println("==>" key.);
            }
        }
    }
}
