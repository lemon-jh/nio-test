package com.itlemon.nio.server;

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

    private Selector selector;

    private ReactorThread thread;

    public NioReactor() {
        try {
            this.selector = Selector.open();
            thread = new ReactorThread();
            thread.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registry (SelectionKey key) {

        System.out.println("==> key = " + key);

        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

        System.out.println("=> 服务注册");

        try {
            SocketChannel sh = channel.accept();
            sh.configureBlocking(false).register(this.selector,SelectionKey.OP_READ);
            //selector.wakeup();
            System.out.println("=> 服务注册");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ReactorThread extends Thread {

        public void run() {


            for (;;) {

                System.out.println("==>123");

                try {

                    selector.select(1000l);

                    Set<SelectionKey> keys = selector.selectedKeys();

                    Iterator<SelectionKey> ketIt = keys.iterator();

                    while (ketIt.hasNext()){

                        SelectionKey key = ketIt.next();

                        ketIt.remove();

                        System.out.println("==> key =" + key);

                        if(key.isValid() && key.isReadable()) {

                            SocketChannel sc = (SocketChannel) key.channel();

                            ByteBuffer buffer = ByteBuffer.allocate(12);

                            sc.read(buffer);

                            buffer.flip();

                            System.out.println("==> Remaining len = " + buffer.remaining());

                            byte [] bytes = buffer.array();

                            String receive = new String(bytes);

                            System.out.println("==>read bytes len ="  +
                                    bytes.length + "str = " + receive);

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
