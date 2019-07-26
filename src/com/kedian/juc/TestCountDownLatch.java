package com.kedian.juc;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;

/**
 * @author wuzh
 * @date 2019/7/26
 * <p>
 * CountDownLatch ：闭锁，在完成某些运算是，只有其他所有线程的运算全部完成，当前运算才继续执行
 * 使用场景：多线程下计时，商城库存统计：各类商品分别开一个线程统计，最后一个线程汇总
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
        //创建闭锁：传参，计数器用于计数
        final CountDownLatch latch = new CountDownLatch(50);
        LatchDemo latchDemo = new LatchDemo(latch);
        long start = Instant.now().toEpochMilli();
        //开50个线程操作
        for (int i = 0; i < 50; i++) {
            new Thread(latchDemo).start();
        }
        //等待子线程执行完
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = Instant.now().toEpochMilli();
        System.out.println("耗费时间：" + (end - start));
    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5000; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        } finally {
            //-1
            latch.countDown();
        }
    }

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }
}
