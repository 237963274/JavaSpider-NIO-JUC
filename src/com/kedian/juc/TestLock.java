package com.kedian.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description:
 * @date 2019/7/31
 * * 一、用于解决多线程安全问题的方式：
 * *
 * * synchronized:隐式锁
 * * 1. 同步代码块
 * *
 * * 2. 同步方法
 * *
 * * jdk 1.5 后：
 * * 3. 同步锁 Lock
 * * 注意：是一个显示锁，需要通过 lock() 方法上锁，必须通过 unlock() 方法进行释放锁
 */
public class TestLock {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(ticket, "1号窗口").start();
        new Thread(ticket, "2号窗口").start();
        new Thread(ticket, "3号窗口").start();
    }

}

class Ticket implements Runnable {
    //火车票总数
    private int tick = 100;
    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            //加锁
            lock.lock();
            try {
                if (tick > 0) {
                    Thread.sleep(100);
                    System.out.println(Thread.currentThread().getName() + " 完成售票，余票为:" + --tick);
                }
                if (tick <= 0) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放锁，放到finally中
                lock.unlock();
            }
        }
    }
}
