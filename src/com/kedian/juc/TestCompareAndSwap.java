package com.kedian.juc;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description: 模拟CAS算法
 * @date 2019/7/26
 * CAS（Compare-And-Swap） 算法保证数据变量的原子性
 * CAS 算法是硬件对于并发操作的支持
 * CAS 包含了三个操作数：
 * ①内存值  V
 * ②预估值  A
 * ③更新值  B
 * 当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 */
public class TestCompareAndSwap {

    public static void main(String[] args) {
        CompareAndSwap cas = new CompareAndSwap();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expectedValue = cas.get();
                boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
                System.out.println(b);
            }
            ).start();
        }
    }
}

class CompareAndSwap {
    private volatile int value;

    //获取内存的值：旧值
    public synchronized int get() {
        return value;
    }

    /**
     * 比较
     *
     * @param expectedValue 期望值
     * @param newValue      新值
     * @return 无论更新成功还是失败，都会返回旧的内存值
     */
    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            this.value = newValue;
        }
        return oldValue;
    }

    /**
     * 设值
     *
     * @param expectedValue 期望值
     * @param newValue      新值
     * @return 判断更新是否成功，如果更新成功，旧的内存值会和预估值相等
     */
    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
