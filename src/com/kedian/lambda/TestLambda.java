package com.kedian.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.lambda
 * @Description:
 * @date 2019/7/23
 * * 一、Lambda 表达式的基础语法：Java8中引入了一个新的操作符 "->" 该操作符称为箭头操作符或 Lambda 操作符
 * * 						    箭头操作符将 Lambda 表达式拆分成两部分：
 * *
 * * 左侧：Lambda 表达式的参数列表
 * * 右侧：Lambda 表达式中所需执行的功能， 即 Lambda 体
 * *
 * * 语法格式一：无参数，无返回值
 * * 		() -> System.out.println("Hello Lambda!");
 * *
 * * 语法格式二：有一个参数，并且无返回值
 * * 		(x) -> System.out.println(x)
 * *
 * * 语法格式三：若只有一个参数，小括号可以省略不写
 * * 		x -> System.out.println(x)
 * *
 * * 语法格式四：有两个以上的参数，有返回值，并且 Lambda 体中有多条语句
 * *		Comparator<Integer> com = (x, y) -> {
 * *			System.out.println("函数式接口");
 * *			return Integer.compare(x, y);
 * *		};
 * *
 * * 语法格式五：若 Lambda 体中只有一条语句， return 和 大括号都可以省略不写
 * * 		Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
 * *
 * * 语法格式六：Lambda 表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出，数据类型，即“类型推断”
 * * 		(Integer x, Integer y) -> Integer.compare(x, y);
 * *
 * * 上联：左右遇一括号省
 * * 下联：左侧推断类型省
 * * 横批：能省则省
 * *
 * * 二、Lambda 表达式需要“函数式接口”的支持
 * * 函数式接口：接口中只有一个抽象方法的接口，称为函数式接口。 可以使用注解 @FunctionalInterface 修饰
 * * 			 可以检查是否是函数式接口
 */
public class TestLambda {

    List<Employee> emps = Arrays.asList(
            new Employee(101, "张三", 18, 9999.99),
            new Employee(102, "李四", 59, 6666.66),
            new Employee(103, "王五", 28, 3333.33),
            new Employee(104, "赵六", 8, 7777.77),
            new Employee(105, "田七", 38, 5555.55)
    );

    //需求：获取公司中年龄小于 35 的员工信息
    //方式1.使用匿名内部类
    @Test
    public void test1() {
        List<Employee> list = filterEmploy(emps, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getAge() > 35;
            }
        });

        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    //方式2.使用lambda表达式
    @Test
    public void test2() {
        List<Employee> list = filterEmploy(emps, (e) -> e.getAge() > 35);
        list.forEach(System.out::println);
    }

    //方式3.使用Stream
    @Test
    public void test3() {
        emps.stream()
                .filter((e) -> e.getAge() > 35)
                .forEach(System.out::println);
        System.out.println("------------------------");
        emps.stream()
                .map(Employee::getName)
                .limit(3)
                .sorted()
                .forEach(System.out::println);
    }

    //策略设计模式
    public List<Employee> filterEmploy(List<Employee> emps, MyPredicate<Employee> mp) {
        List<Employee> list = new ArrayList<>();
        for (Employee emp : emps) {
            if (mp.test(emp)) {
                list.add(emp);
            }
        }
        return list;
    }

    //语法格式二：有一个参数，并且无返回值
//    语法格式三：若只有一个参数，小括号可以省略不写
    @Test
    public void test4(){
        Consumer<String> consumer=(x)-> System.out.println(x);
        consumer.accept("西南交通大学威武");

        Consumer<String> con=x-> System.out.println(x);
        con.accept("西南交通大学威武");
    }

    //语法格式四：有两个以上的参数，有返回值，并且 Lambda 体中有多条语句,Lambda 体需要{}
    @Test
    public void test5(){
        Comparator<Integer> com=(x, y)->{
            System.out.println("函数式接口");
            return Integer.compare(x,y);
        };
        System.out.println(com.compare(2,5 ));
    }

    //语法格式五：若 Lambda 体中只有一条语句， return 和 大括号都可以省略不写
    @Test
    public void test6(){
        Comparator<Integer> com=(x, y)-> Integer.compare(x,y);
        System.out.println(com.compare(2,5 ));
    }


    //语法格式六：Lambda 表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出，数据类型，即“类型推断”
    @Test
    public void test7(){
        Integer num=operation(100,(x)->x*x );
        System.out.println(num);

        Integer num2=operation(100,(Integer x)->x+x );
        System.out.println(num2);
    }

    public Integer operation(Integer num, MyFun fun){
        return fun.getValue(num);
    }
}
