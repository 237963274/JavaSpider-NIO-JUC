package com.kedian.nio;


import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.nio
 * @Description:
 * @date 2019/7/23
 */
public class TestPipe {

    @Test
    public void test() throws IOException {
        String str = "通过单向管道发送数据";
        //1.获取管道
        Pipe pipe = Pipe.open();
        //2.将缓冲区的数据写入管道
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Pipe.SinkChannel sink = pipe.sink();
        buf.put(str.getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            sink.write(buf);
        }


        //3.读取缓冲区的数据（另外一个线程操作）
        Pipe.SourceChannel source = pipe.source();
        ByteBuffer buf1 = ByteBuffer.allocate(1024);
        int len = source.read(buf1);
        System.out.println(new String(buf1.array(), 0, len));
        source.close();
        sink.close();
    }
}
