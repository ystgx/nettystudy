package com.tison.study.nio;



import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Tison
 * @date 2017/03/07
 * @since 1.0
 */
public class FileChannelTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("D:\\multihashtest.txt","rw");
        FileChannel channle = file.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(56);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        int n = channle.read(bb);
        while(n!=-1){
            System.out.println(n);
            bb.flip();
            while(bb.hasRemaining()){
                byte [] arr = bb.array();
                byte  b = bb.get();
                baos.write(b);
            }
            bb.clear();
            n = channle.read(bb);
        }

        System.out.println(new String(baos.toByteArray()));
        file.close();

    }
}
