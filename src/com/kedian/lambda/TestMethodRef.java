package com.kedian.lambda;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.lambda
 * @Description:
 * @date 2019/7/23
 *
 * * 一、方法引用：若 Lambda 体中的功能，已经有方法提供了实现，可以使用方法引用
 *  * 			  （可以将方法引用理解为 Lambda 表达式的另外一种表现形式）
 *  *
 *  * 1. 对象的引用 :: 实例方法名
 *  *
 *  * 2. 类名 :: 静态方法名
 *  *
 *  * 3. 类名 :: 实例方法名
 *  *
 *  * 注意：
 *  * 	 ①方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致！
 *  * 	 ②若Lambda 的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，格式： ClassName::MethodName
 *  *
 *  * 二、构造器引用 :构造器的参数列表，需要与函数式接口中参数列表保持一致！
 *  *
 *  * 1. 类名 :: new
 *  *
 *  * 三、数组引用
 *  *
 *  * 	类型[] :: new;
 *  *
 */
public class TestMethodRef {

}
