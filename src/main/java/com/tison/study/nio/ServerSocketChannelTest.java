package com.tison.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Tison
 * @date 2017/03/07
 * @since 1.0
 */
public class ServerSocketChannelTest {
    private static final int PORT = 7777;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(PORT));
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server start to listen port:" + PORT);

        while(true){
            int ready = selector.select();
            if(ready == 0){
                continue;
            }
            Set<SelectionKey> keys =   selector.selectedKeys();
            Iterator<SelectionKey> itor = keys.iterator();
            while(itor.hasNext()){
                SelectionKey key = itor.next();
                itor.remove();
                handleSelection(key);
            }
        }
    }

    private static void handleSelection(SelectionKey selectionKey) throws IOException {
        if(!selectionKey.isValid()){
            return;
        }
        if(selectionKey.isAcceptable()){
            ServerSocketChannel ch = (ServerSocketChannel) selectionKey.channel();
            SocketChannel sc = ch.accept();
            InetSocketAddress addr = (InetSocketAddress) sc.getRemoteAddress();
            System.out.println("A client connect from :" + addr.getHostString() + ":" + addr.getPort());
            sc.configureBlocking(false);
            sc.register(selectionKey.selector(),SelectionKey.OP_READ);
        }else if(selectionKey.isReadable()){
            SocketChannel ch = (SocketChannel) selectionKey.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int byteRead = ch.read(readBuffer);
            if(byteRead > 0){
                readBuffer.flip();
                byte buffer [] = new byte [readBuffer.remaining()] ;
                readBuffer.get(buffer);
                System.out.println(new String(buffer));
            }else if(byteRead < 0){
                InetSocketAddress addr = (InetSocketAddress) ch.getRemoteAddress();
                System.out.println("The client disconnect :" + addr.getHostString() + ":" + addr.getPort());
                selectionKey.cancel();
                ch.close();
            }else{
                return ;
            }

        }

    }
}
