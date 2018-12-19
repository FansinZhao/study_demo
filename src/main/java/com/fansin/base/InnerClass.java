package com.fansin.base;

import java.util.RandomAccess;

/**
 * Created by zhaofeng on 17-4-12.
 */
public class InnerClass {

    public static void main(String[] args) {
        InnerClass inner = new InnerClass();
        //非静态内部类,可以多个实例
        InnerClass1 inner10 = inner.new InnerClass1();
        InnerClass1 inner11 = inner.new InnerClass1();
        InnerClass1 inner12 = inner.new InnerClass1();
        //静态内部类 不依赖对象实例
        InnerClassStatic innerStatic = new InnerClassStatic();
        System.out.println("-----静态内部类------\n" + innerStatic.name);
        //方法内部类
        inner.function();
        System.out.println("---------------");

        /*
         * 匿名内部类
         * 1 使用new定义,用完即销毁,不能重复使用
         * 2 不能使用static变量和方法
         * 3 不能有构造方法
         * 4 只能是一个类或接口
         * 5 不能有抽象方法
         * */
        InnerClass inner1 = new InnerClass() {

            private int age;

            //        private static String name;//
            public void function() {
                System.out.println(sex);
            }
        };
        inner1.function();

    }

    private static String sex  = "男";
    private        int    age  = 0;
    private        String name = "OuterClass-name";

    /*
     * 第一种 非静态内部类 必须依赖实例
     * 1 只能依赖外部类的实例
     * 2 可以继承一个父类,实现多个接口
     * 3 内部类可以有构造方法
     * 4 内部类可以方便访问外部类所有属性(静态/非静态),以及修改
     *
     * */
    //非静态
    class InnerClass1 extends Object implements Runnable, RandomAccess {

        public String name = "InneClass-name";

        public InnerClass1() {
            System.out.println(age);
            System.out.println("内部类属性 " + name);
            InnerClass.this.name = "被内部类修改";
            System.out.println("外部类属性 " + InnerClass.this.name);
            System.out.println("外部类静态属性 " + sex);

        }

        @Override
        public void run() {

        }
    }

    /**
     * 第二种 静态内部类
     * 1 不依赖实例对象
     * 2 具有非静态内部类一切属性,
     * 3 可以定义静态变量
     */
    static class InnerClassStatic {

        private static String name = "新增内部静态类静态属性";
    }

    /**
     * 第三种 方法内部类
     * 1 存活在方法的作用域中,生命周期只能在方法中
     * 2 可以访问方法的临时变量,外部类变量
     * 3 在构造方法中,可以访问非final,在普通方法中只能访问final变量
     */
    public void function() {
        final int num = 123;
        int num1 = 11111;

        class InnerFunc {

            private String inneerName = "方法内部类变量";

            public InnerFunc() {
                System.out.println("非final方法变量 " + num1);
                fun();
            }

            public void fun() {
                //                name = "方法块之外的外部类属性";
                System.out.println("方法块之外的外部类属性>>" + name);
                System.out.println("final方法变量 " + num);
                System.out.println(inneerName);
            }
        }
        //定义之后使用
        InnerFunc innerFunc = new InnerFunc();
//        num = 0;
//        innerFunc.fun();
    }

}
