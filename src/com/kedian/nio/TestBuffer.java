package com.kedian.nio;

import java.nio.ByteBuffer;

/**
 * @author wzh
 * @description
 * @create 2019-07-21 13:48
 */
public class TestBuffer {
    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

    private static void test3() {
        //分配直接缓冲区
        ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
        //判断是否是直接缓冲区
        System.out.println(buffer.isDirect());
    }

    //测试基本方法
    private static void test1() {
        String str="abcde";
        //1.分配一个指定大小的缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);

        System.out.println("--------------allocate()----------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //2.利用put()存入数据到缓冲区
        buf.put(str.getBytes());

        System.out.println("--------------put()----------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //3.切换读取模式,将position归0
        buf.flip();

        System.out.println("--------------flip()----------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //4.利用git()获取数据
        byte[] dst=new byte[buf.limit()];
        buf.get(dst);

        System.out.println("--------------get()----------------");
        System.out.println(new String(dst,0,dst.length));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //5.rewind():可重复的读(等效于flip(),都是将position归0.)
        buf.rewind();
        byte[] dst1=new byte[buf.limit()];
        buf.get(dst1);

        System.out.println("--------------rewind()----------------");
        System.out.println(new String(dst,0,dst1.length));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //6.clear()，清空缓冲区，只是将limit=capacity，positio=0，但是缓冲区中的数据依然存在，但是处于“被遗忘”状态
        buf.clear();

        System.out.println("--------------clear()----------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        System.out.println((char) buf.get());
    }

    //测试hasRemaining()和remaining()方法
    private static void test2() {
        String str="abcde";
        ByteBuffer buf=ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();
        byte[] dst=new byte[buf.limit()];
        buf.get(dst,0,2);
        System.out.println(new String(dst,0,2));
        System.out.println(buf.position());

        //mark():标记
        System.out.println("---------------mark()-----------------");
        buf.mark();
        buf.get(dst,0,2);
        System.out.println(new String(dst,0,2));
        System.out.println(buf.position());

        //reset（）：positi恢复到mark的位置
        System.out.println("---------------reset()-----------------");
        buf.reset();
        System.out.println(buf.position());

        //hasRemaining():判断缓冲区是否还有剩余数据
        System.out.println("---------------hasRemaining()-----------------");
        if (buf.hasRemaining()){
            //获取缓冲区可以操作的数量
            System.out.println(buf.remaining());
        }
    }


}
