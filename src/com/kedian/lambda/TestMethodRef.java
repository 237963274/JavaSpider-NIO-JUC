package com.kedian.lambda;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.lambda
 * @Description:
 * @date 2019/7/23
 * <p>
 * * 一、方法引用：若 Lambda 体中的功能，已经有方法提供了实现，可以使用方法引用
 * * 			  （可以将方法引用理解为 Lambda 表达式的另外一种表现形式）
 * *
 * * 1. 对象的引用 :: 实例方法名
 * *
 * * 2. 类名 :: 静态方法名
 * *
 * * 3. 类名 :: 实例方法名
 * *
 * * 注意：
 * * 	 ①方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致！
 * * 	 ②若Lambda 的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，格式： ClassName::MethodName
 * *
 * * 二、构造器引用 :构造器的参数列表，需要与函数式接口中参数列表保持一致！
 * *
 * * 1. 类名 :: new
 * *
 * * 三、数组引用
 * *
 * * 	类型[] :: new;
 * *
 */
public class TestMethodRef {

    //1.对象的引用::实例方法名
    @Test
    public void test1() {
        Employee emp = new Employee(101, "张三", 23, 5505.36);
        Supplier<String> sup = () -> emp.getName();
        System.out.println(sup.get());

        System.out.println("----------------------------------");

        Supplier<Integer> sup1 = emp::getAge;
        System.out.println(sup1.get());
    }

    //2.类名::静态方法名
    @Test
    public void test2() {
        BiFunction<Double, Double, Double> fun1 = (x, y) -> Math.max(x, y);
        Double maxNum1 = fun1.apply(56.3, 22.1);
        System.out.println(maxNum1);

        System.out.println("---------------------------");

        BiFunction<Double, Double, Double> fun2 = Math::max;
        Double maxNum2 = fun1.apply(56.3, 22.1);
        System.out.println(maxNum2);
        //---------------------------------------------------
        Comparator<Integer> com1 = (x, y) -> x.compareTo(y);
        System.out.println(com1.compare(22, 10));

        Comparator<Integer> com2 = Integer::compare;
        System.out.println(com1.compare(1, 10));
    }

    //3.类名::实例方法名
    @Test
    public void test3() {
        BiPredicate<String, String> bp1 = (x, y) -> x.equalsIgnoreCase(y);
        boolean test1 = bp1.test("abc", "AbC");
        System.out.println(test1);

        System.out.println("-----------------------");

        BiPredicate<String, String> bp2 = String::equalsIgnoreCase;
        boolean test2 = bp1.test("aBc", "AbC");
        System.out.println(test2);

        //-------------------------------------------------
        Function<Employee, String> fun1 = (e) -> e.show();
        System.out.println(fun1.apply(new Employee()));

        Function<Employee, String> fun2 = Employee::show;
        System.out.println(fun2.apply(new Employee()));
    }

    /*
        4.构造器引用
        类名 :: new
     */
    @Test
    public void test4(){
        Supplier<Employee> sup1=()->new Employee("张三");
        System.out.println(sup1.get());
        System.out.println("------------构造器引用---------");
        Supplier<Employee> sup2=Employee::new;
        System.out.println(sup2.get());
    }

    /*
        5.数组引用
        类型[]::new
     */
    @Test
    public void test5(){
        //创建指定大小的数组
        Function<Integer,String[]> fun1=(args)->new String[args];
        String[] arr1 = fun1.apply(10);
        System.out.println(arr1.length);

        System.out.println("------------------------------");
        Function<Integer,String[]> fun2=String[]::new;
        String[] arr2 = fun2.apply(6);
        System.out.println(arr2.length);
    }
}
