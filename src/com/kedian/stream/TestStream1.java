package com.kedian.stream;

import com.kedian.lambda.Employee;
import com.kedian.lambda.Employee.Status;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.stream
 * @Description:
 * @date 2019/7/24
 * * 一、Stream API 的操作步骤：
 * *
 * * 1. 创建 Stream
 * *
 * * 2. 中间操作
 * *
 * * 3. 终止操作(终端操作)
 */
public class TestStream1 {
    List<Employee> emps = Arrays.asList(
            new Employee(102, "李四", 79, 6666.66, Status.BUSY),
            new Employee(101, "张三", 18, 9999.99, Status.FREE),
            new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
            new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
            new Employee(104, "赵六", 8, 7777.77, Status.FREE),
            new Employee(104, "赵六", 8, 7777.77, Status.FREE),
            new Employee(105, "田七", 38, 5555.55, Status.BUSY)
    );

    //1.创建流
    @Test
    public void test1() {
        //1) Collection 提供了两个方法：串行流 stream()和并行流 parallelSteam()
        List<String> list = new ArrayList<>();
        //获取顺序流
        Stream<String> stream = list.stream();
        //获取并行流
        Stream<String> parallelStream = list.parallelStream();

        //2）通过Arrays的stream()获取一个数组流
        Integer[] nums = new Integer[10];
        Stream<Integer> stream1 = Arrays.stream(nums);

        //3)通过Stream类中的静态方法of()
        Stream<Integer> stream2 = Stream.of(1, 2, 3, 4, 5);

        //4)创建无限流
        //迭代
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2).limit(10);
        stream3.forEach(System.out::println);
        //生成
        Stream<Double> stream4 = Stream.generate(Math::random).limit(10);
        stream4.forEach(System.out::println);
    }

    //2.中间操作

    /**
     * 2.1 筛选和切片
     * filter——接收 Lambda ， 从流中排除某些元素。
     * limit——截断流，使其元素不超过给定数量。
     * skip(n) —— 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
     * distinct——筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
     */
    @Test
    public void test2() {

        Stream<Employee> stream = emps.stream()
                .filter((e) -> e.getAge() < 20);
        stream.forEach(System.out::println);

        System.out.println("---------------------------------------------");
        emps.stream()
                .limit(2)
                .forEach(System.out::println);
        System.out.println("---------------------------------------------");
        emps.stream()
                .skip(2)
                .limit(2)
                .forEach(System.out::println);
        System.out.println("---------------------------------------------");
        emps.stream()
                .distinct()
                .forEach(System.out::println);
    }


    /**
     * 映射
     * map——接收 Lambda ， 将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
     * flatMap——接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test3() {
        Stream<String> stringStream = emps.stream()
                .map((e) -> e.getName());
        stringStream.forEach(System.out::println);

        System.out.println("-------------------");

        List<String> stringList = Arrays.asList("aaa", "bbb", "ccc");
        stringList.stream()
                .map((s) -> s.toUpperCase())
                .forEach(s -> System.out.println(s));

        System.out.println("---------字符串拆分成字符----------");
        Stream<Stream<Character>> stream2 = stringList.stream()
                .map(TestStream1::filterCharacter);
        stream2.forEach((sm) -> {
            sm.forEach(System.out::println);
        });

        System.out.println("--------------flatMap-------------------");
        stringList.stream()
                .flatMap(TestStream1::filterCharacter)
                .forEach(System.out::println);
    }

    public static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }

    /**
     * sorted()——自然排序
     * sorted(Comparator com)——定制排序
     */
    @Test
    public void test4() {
        emps.stream()
                .map(Employee::getAge)
                .sorted()
                .forEach(System.out::println);
        System.out.println("-------根据年龄排，年龄相同根据名字排--------");
        emps.stream()
                .sorted((x, y) -> {
                    if (x.getAge() == y.getAge()) {
                        return x.getName().compareTo(y.getName());
                    } else {
                        return Integer.compare(x.getAge(), y.getAge());
                    }
                })
                .forEach(System.out::println);
    }

    //3.终止操作

    /**
     * 3.1 归约
     * reduce(T identity, BinaryOperator) / reduce(BinaryOperator) ——可以将流中元素反复结合起来，得到一个值。
     */

    @Test
    public void test5() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer sum = list.stream()
                .reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        System.out.println("-------------------------------");
        Optional<Double> op = emps.stream()
                .map(Employee::getSalary)
                .reduce(Double::sum);
        System.out.println(op.get());
    }

    //需求：搜索名字中 “六” 出现的次数
    @Test
    public void test6() {
        Optional<Integer> sum = emps.stream()
                .map(Employee::getName)
                .flatMap(TestStream1::filterCharacter)
                .map((ch) -> {
                    if (ch.equals('六')) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .reduce(Integer::sum);
        System.out.println(sum.get());
    }

    /**
     * 收集
     * collect——将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
     */
    //结果收集转成集合
    @Test
    public void test7() {
        List<String> names = emps.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        names.forEach(System.out::println);

        System.out.println("--------常用集合--------");
        Set<String> set = emps.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
        set.forEach(System.out::println);

        System.out.println("----------------");
        HashSet<String> hashSet = emps.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(HashSet::new));
        hashSet.forEach(System.out::println);
    }

    //其他静态方法
    @Test
    public void test8() {
        //maxBy 求最大
        Optional<Double> op1 = emps.stream()
                .map(Employee::getSalary)
                .collect(Collectors.maxBy(Double::compare));
        System.out.println(op1.get());

        //minBy 求最小
        Optional<Employee> op2 = emps.stream()
                .collect(Collectors.minBy((e1, e2) -> Integer.compare(e1.getAge(), e2.getAge())));
        System.out.println(op2.get());

        //summingDouble 求和
        Double sumSalary = emps.stream()
                .collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sumSalary);

        //求平均
        Double avgAge = emps.stream()
                .collect(Collectors.averagingDouble(Employee::getAge));
        System.out.println(avgAge);

        //求数量
        Long count = emps.stream()
                .collect(Collectors.counting());
        System.out.println(count);

        //获取集合中对象某个属性，在做后序操作
        DoubleSummaryStatistics dss = emps.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(dss.getAverage());
        System.out.println(dss.getCount());
        System.out.println(dss.getMax());
        System.out.println(dss.getMin());
        System.out.println(dss.getSum());
    }

    //分组
    @Test
    public void test9() {
        Map<Status, List<Employee>> map = emps.stream()
                .collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(map);
    }

    //多级分组
    @Test
    public void test10(){
        Map<Status, Map<String, List<Employee>>> map = emps.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
                    if (e.getAge() >= 60) {
                        return "老年";
                    } else if (e.getAge() >= 35) {
                        return "中年";
                    } else {
                        return "青年";
                    }
                })));
        System.out.println(map);
    }

    //分区
    @Test
    public void test11(){
        Map<Boolean, List<Employee>> map = emps.stream()
                .collect(Collectors.partitioningBy((e) -> e.getSalary() > 5000));
        System.out.println(map);
    }

    //joing
    @Test
    public void test12(){
        //joining 第一个是中间分隔符，第二个是头部，第三个是尾部
        String str = emps.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(",", "--head--", "--tail--"));
        System.out.println(str);
    }

    //reducing
    @Test
    public void test13(){
        Optional<Double> op = emps.stream()
                .map(Employee::getSalary)
                .collect(Collectors.reducing(Double::sum));
        System.out.println(op.get());
    }
}
