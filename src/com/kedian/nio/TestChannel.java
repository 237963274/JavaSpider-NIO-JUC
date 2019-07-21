package com.kedian.nio;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author wzh
 * @description
 * @create 2019-07-21 17:26
 */
public class TestChannel {

    public static void main(String[] args) throws IOException {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
        test6();
    }

    /**
     * 字符集使用：编码和解码
     */
    private static void test6() throws CharacterCodingException {
        Charset gbkCharset = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce = gbkCharset.newEncoder();
        //获取解码器
        CharsetDecoder cd = gbkCharset.newDecoder();

        CharBuffer charBuffer=CharBuffer.allocate(100);
        charBuffer.put("西南交通大学！");
        charBuffer.flip();
        //编码
        ByteBuffer byteBuffer=ce.encode(charBuffer);
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
        //解码
        byteBuffer.flip();
        CharBuffer charBuffer2=cd.decode(byteBuffer);
        System.out.println(charBuffer2.toString());

        System.out.println("---------------gbk->utf-8-------------");
        Charset utf8Charset = Charset.forName("UTF-8");
        byteBuffer.flip();
        CharBuffer charBuffer3=utf8Charset.decode(byteBuffer);
        System.out.println(charBuffer3.toString());
    }

    /**
     * 查看字符集种类
     */
    private static void test5() {
        SortedMap<String, Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> entries = map.entrySet();
        for (Map.Entry<String, Charset> entry : entries) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
    }

    /**
     * 分散读取和聚集写入
     */
    private static void test4() throws IOException {
        RandomAccessFile rafIn = new RandomAccessFile("1.txt", "rw");
        //1.获取读取通道
        FileChannel inChannel = rafIn.getChannel();
        //2.分配多个指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        //3.分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        inChannel.read(bufs);
        for (ByteBuffer buf : bufs) {
            buf.flip();
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("------------------------------------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        //4.聚集写入
        RandomAccessFile rafOut = new RandomAccessFile("2.txt", "rw");
        FileChannel outChannel = rafOut.getChannel();
        outChannel.write(bufs);
    }

    /**
     * 方式1：利用通道完成文件的复制（非直接缓冲区）
     */
    private static void test1() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        //1.获取通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            fis = new FileInputStream("1.jpg");
            fos = new FileOutputStream("2.jpg");
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            //2.分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //3.将输入的通道数据存入缓冲区
            while (inChannel.read(buf) != -1) {
                //切换读取数据模式
                buf.flip();
                //4.将缓冲区的数据写入输出通道
                outChannel.write(buf);
                //清空缓冲区
                buf.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 方式2：使用直接缓冲区完成文件的复制（内存映射文件）
     */
    private static void test2() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            /*
            StandardOpenOption.CREATE_NEW   文件不存在则写入，文件存在就抛文件存在异常
            StandardOpenOption.CREATE       文件不存在则写入，文件存在就覆盖原有文件
             */
            outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
//            outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);
            //内存映射文件
            MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
            
            //直接对缓冲区进行数据的读写操作
            byte[] dst = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dst);
            outMappedBuf.put(dst);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 方式3：通道之间的数据传输（直接缓冲区）
     */
    private static void test3() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
            //方式1：in---to---out
            //inChannel.transferTo(0,inChannel.size(),outChannel);

            //方式2：out---from---in
            outChannel.transferFrom(inChannel, 0, inChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
