package com.kedian.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description: 生产者消费者案例：
 * @date 2019/7/31
 */
public class TestProductorAndConsumerForLock {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();

        Productor productor = new Productor(clerk);
        Consumer consumer = new Consumer(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
        new Thread(productor, "生产者c").start();
        new Thread(consumer, "消费者d").start();

    }
}

//营业员
class Clerk {
    private int product = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    //进货
    public void buy() {
        lock.lock();
        try {
            while (product >= 3) {//为了避免虚假唤醒问题，应该总是使用在循环中
                System.out.println("产品已满！");
                condition.await();
            }
            //否则进货
            System.out.println(Thread.currentThread().getName() + " :" + ++product);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //卖货
    public void sale() {
        lock.lock();
        try {
            while (product <= 0) {//为了避免虚假唤醒问题，应该总是使用在循环中
                System.out.println("缺货！");
                condition.await();
            }
            //卖货
            System.out.println(Thread.currentThread().getName() + " :" + --product);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

//生产者
class Productor implements Runnable {

    private Clerk clerk;

    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clerk.buy();
        }
    }
}

//消费者
class Consumer implements Runnable {

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            clerk.sale();
        }
    }
}
