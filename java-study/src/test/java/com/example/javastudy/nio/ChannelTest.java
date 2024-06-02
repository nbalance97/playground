package com.example.javastudy.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class ChannelTest {

    /**
     * Channel
     */
    public void testChannel() {
        try (var channel = new RandomAccessFile("test.txt", "rw").getChannel()) {
            var buf = ByteBuffer.allocate(48);
            var readByte = channel.read(buf);

            while (readByte != -1) {
                System.out.println("Read " + readByte);
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                buf.clear();
                readByte = channel.read(buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
