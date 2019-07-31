package com.kedian.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description:
 * @date 2019/7/31
 * <p>
 * * 1. ReadWriteLock : 读写锁
 * *
 * * 写写/读写 需要“互斥”
 * * 读读 不需要互斥
 * *
 */
public class TestReadWriteLock {

    public static void main(String[] args) {
        ReadWriteLockDemo rw = new ReadWriteLockDemo();
        //写
        new Thread(() -> {
            rw.set((int) (Math.random() * 101));
        }, "Write1").start();
        new Thread(() -> {
            rw.set((int) (Math.random() * 101 + 100));
        }, "Write2").start();
        //读
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                rw.get();
            }, "Read").start();
        }
    }

}

class ReadWriteLockDemo {
    private int number = 0;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //读
    public void get() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " : " + number);
        } finally {
            lock.readLock().unlock();
        }
    }

    //写
    public void set(int number) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName());
            this.number = number;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
