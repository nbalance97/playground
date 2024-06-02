package com.example.javastudy.nio;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class BufferTest {

    @Test
    public void bufferTest() {
        // buffer의 크기를 50으로 설정하여 버퍼를 생성한다
        var buffer = ByteBuffer.allocate(50);

        // buffer에 데이터를 write
        buffer.put((byte) 1);

        // buffer의 mode를 write -> read로 변경
        var readBuffer = buffer.flip();

        var readByte = readBuffer.get();
        System.out.println(readByte);
    }
}
