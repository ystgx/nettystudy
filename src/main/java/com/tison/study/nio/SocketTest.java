package com.tison.study.nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Tison
 * @date 2017/03/07
 * @since 1.0
 */
public class SocketTest {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1",7777));
        OutputStream output = socket.getOutputStream();
        System.out.println("连接已打开，输入要发送的内容：");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while(!"exit".equals(line)){
            output.write(line.getBytes());
            output.flush();
            line = reader.readLine();
        }
        socket.close();
    }
}
