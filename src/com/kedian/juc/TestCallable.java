package com.kedian.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description:
 * @date 2019/7/31
 * * 一、创建执行线程的方式三：实现 Callable 接口。 相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
 * *
 * * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。  FutureTask 是  Future 接口的实现类
 */
public class TestCallable {

    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();

        /*//方式1：thread调用
        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Integer> future=new FutureTask<>(td);
        new Thread(future).start();*/

        //方式2：
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = executor.submit(td);

        //接收运算后的结果
        try {
            Integer sum = future.get();//FutureTask 可用于 闭锁,等前面所有线程执行完，才执行main线程
            System.out.println(sum);
            System.out.println("---------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class ThreadDemo implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 0; i <= 10000; i++) {
                sum += i;
            }
            return sum;
        }
    }
}


