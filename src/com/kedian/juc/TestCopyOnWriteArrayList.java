package com.kedian.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuzh
 * @date 2019/7/26
 * <p>
 * * CopyOnWriteArrayList/CopyOnWriteArraySet : “写入并复制”
 * * 注意：添加操作多时，效率低，因为每次添加时都会进行复制，开销非常的大。并发迭代操作多时可以选择。
 */
public class TestCopyOnWriteArrayList {

    public static void main(String[] args) {
//        Object obj=3;
//        System.out.println(obj instanceof Integer);

        MyThread myThread = new MyThread();
        for (int i = 0; i < 5; i++) {
            new Thread(myThread).start();
        }
    }

}

class MyThread implements Runnable {
    //抛java.util.ConcurrentModificationException异常
//    private static List<String> list = Collections.synchronizedList(new ArrayList<>());

    private static CopyOnWriteArrayList list = new CopyOnWriteArrayList();

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
            //在迭代过程操作集合
            list.add("aa");
        }
    }
}
