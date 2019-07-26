package com.kedian.juc;

/**
 * @author wuzh
 * @date 2019/7/26
 * <p>
 * * 一、volatile 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据可见。
 * * 					  相较于 synchronized 是一种较为轻量级的同步策略。
 * *
 * * 注意：
 * * 1. volatile 不具备“互斥性”
 * * 2. volatile 不能保证变量的“原子性”
 */
public class TestVolatile {

    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        //一个线程修改
        new Thread(td).start();

        //main线程读取
        while (true) {
            if (td.isFlag()) {
                System.out.println("---------主线程------");
                break;
            }
        }
    }
}


class ThreadDemo implements Runnable {
    //普通方式
//    private boolean flag = false;
    private volatile boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;
        System.out.println("flag=" + flag);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
