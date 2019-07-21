package com.kedian.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author wzh
 * @description
 * @create 2019-07-21 21:54
 *
 *  * 一、使用 NIO 完成网络通信的三个核心：
 *  *
 *  * 1. 通道（Channel）：负责连接
 *  *
 *  * 	   java.nio.channels.Channel 接口：
 *  * 			|--SelectableChannel
 *  * 				|--SocketChannel
 *  * 				|--ServerSocketChannel
 *  * 				|--DatagramChannel
 *  *
 *  * 				|--Pipe.SinkChannel
 *  * 				|--Pipe.SourceChannel
 *  *
 *  * 2. 缓冲区（Buffer）：负责数据的存取
 *  *
 *  * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 *  *
 */
public class NonBlocingkNIO {

    @Test
    public void client()   {
        try {
            //1.获取通道
            SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
            //2.切换到非阻塞模式
            sChannel.configureBlocking(false);
            //3.分配指定大小缓冲区
            ByteBuffer buf=ByteBuffer.allocate(1024);
            //4.发送数据给客户端
            Scanner scanner=new Scanner(System.in);
            System.out.println("请输入：");
            while (scanner.hasNext()){
                String str=scanner.next();
                System.out.println(str);
                buf.put((LocalDateTime.now().toString()+"\n"+str).getBytes());
                buf.flip();
                sChannel.write(buf);
                buf.clear();
            }
            //5.关闭通道
            sChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void server()   {
        try {
            //1.获取通道
            ServerSocketChannel ssChannel=ServerSocketChannel.open();
            //2.切换到非阻塞模式
            ssChannel.configureBlocking(false);
            //3.绑定连接
            ssChannel.bind(new InetSocketAddress(9898));
            //4.获取选择器
            Selector selector = Selector.open();
            //5.将通道注册到选择器中
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            //6.轮询获取选择器上已经“准备就绪”的事件
            while (selector.select()>0){
                //7.获取当前选择器中所有注册的“选择键是已就绪的监听事件”
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    //8.获取事件
                    SelectionKey selectionKey = iterator.next();
                    //9.判断是否是准备就绪事件
                    if (selectionKey.isAcceptable()){
                        //10.若是“准备就绪”，获取客户端连接通道
                        SocketChannel sChannel = ssChannel.accept();
                        //11.切换到非阻塞模式
                        sChannel.configureBlocking(false);
                        //12.将该通道注册到选择器
                        sChannel.register(selector,SelectionKey.OP_READ);
                    }else if (selectionKey.isReadable()){
                        //13.获取当前选择器是“读就绪”状态通道
                        SocketChannel sChannel= (SocketChannel) selectionKey.channel();
                        //14.读取数据
                        ByteBuffer buf=ByteBuffer.allocate(1024);
                        int len=0;
                        while ((len=sChannel.read(buf))>0){
                            buf.flip();
                            System.out.println(new String(buf.array(),0,len));
                            buf.clear();
                        }
                    }

                    //15.取消选择键SelectionKey
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
