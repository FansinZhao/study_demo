package com.fansin.reflect.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;

/**
 * Created by zhaofeng on 17-4-3.
 */
public class MyDynamic {

    public static void main(String[] args) throws Exception {
        //1 通过反射
        reflect();
        //2 jdk 动态代理 底层还是反射那套 跟1的原理一致
        jdk();
        //cglib
        cglib();

        Long l = new Long(System.currentTimeMillis());
        System.out.println(Integer.toString(l.intValue(), 16));

    }

    public static void cglib() {
        MyMethodInterceptor myMethodInterceptor = new MyMethodInterceptor();
        Hello hello = (Hello) myMethodInterceptor.getProxy(Hello.class);
        hello.say("这是通过cglib动态代理发送的消息!");
    }

    public static void jdk() {

        IUser user = new IUserImpl();
        MyInvocationHandler invocationHandler = new MyInvocationHandler(user);
        IUser userProxy = (IUser) Proxy.newProxyInstance(user.getClass().getClassLoader(),
                                                         user.getClass().getInterfaces(), invocationHandler);
        userProxy.getName("这是通过jdk动态代理传递的变量!");
        userProxy.getAge(10);
        user.getName("这是通过对象传递的变量!");

    }

    public static void reflect()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
                   MalformedURLException, NoSuchMethodException {
        //拿到class文件
//        File  f = new File("/home/zhaofeng");
////        File  f = new File("target/classes/com.fansin.serviceloader.AbstractFactory/fansin/reflect/dynamic/");
//        System.out.println(f.toURI().toURL());
//        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{f.toURI().toURL()});
//        classLoader.loadClass("Hello");
//        MyClassloader myClassloader = new MyClassloader();
//        myClassloader.findClass("/home/zhaofeng/Hello.class");
        //使用class文件
        Class clazz = Class.forName("com.fansin.reflect.dynamic.Hello");
        Object o = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("获取方法:" + m.getName());
            m.invoke(o, "动态添加参数,fansin");
        }
    }
}
