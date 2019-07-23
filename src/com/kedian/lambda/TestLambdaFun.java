package com.kedian.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.lambda
 * @Description:
 * @date 2019/7/23
 * <p>
 * * Java8 内置的四大核心函数式接口
 * *
 * * Consumer<T> : 消费型接口
 * * 		void accept(T t);
 * *
 * * Supplier<T> : 供给型接口
 * * 		T get();
 * *
 * * Function<T, R> : 函数型接口
 * * 		R apply(T t);
 * *
 * * Predicate<T> : 断言型接口
 * * 		boolean test(T t);
 */
public class TestLambdaFun {

    //1.Consumer<T> 消费型接口 :
    @Test
    public void test1() {
        useMoney(100, (x) -> System.out.println("使用了:" + x + "元"));
    }

    public void useMoney(double money, Consumer<Double> con) {
        con.accept(money);
    }

    //2.Supplier<T> 供给型接口 :
    @Test
    public void test2() {
        List<Integer> list = getNumList(20, () -> (int) (Math.random() * 100));
        list.forEach(System.out::println);
    }

    //需求：产生指定个数的整数，并放入集合中
    public List<Integer> getNumList(int count, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Integer num = supplier.get();
            list.add(num);
        }
        return list;
    }

    //3.Function<T,R> 函数型接口
    @Test
    public void test3() {
        String newStr = strHandler(" \t西南交通大学，电子科技 大学 ", (s) -> s.trim());
        System.out.println(newStr);
    }

    //需求：用于处理字符串
    public String strHandler(String str, Function<String, String> function) {
        return function.apply(str);
    }

    //Predicate<T> 断言型接口：
    @Test
    public void test4() {
        List<String> list = Arrays.asList("zhangsan", "lisi", "Lambda", "www", "ok");
        List<String> stringList=filterStr(list,(x)->x.length()>3 );
        stringList.forEach(System.out::println);
    }

    //需求：将满足条件的字符串，放入集合中
    public List<String> filterStr(List<String> list, Predicate<String> predicate) {
        List<String> strings = new ArrayList<>();
        for (String s : list) {
            if (predicate.test(s)) {
                strings.add(s);
            }
        }
        return strings;
    }
}
