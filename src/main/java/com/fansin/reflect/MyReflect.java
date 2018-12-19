package com.fansin.reflect;

import java.lang.annotation.Annotation;

/**
 * Created by zhaofeng on 17-4-2.
 */
//@MyAnnotation()
@MyAnnotation(hello = "fansin")
public class MyReflect {

    public static void main(String[] args) throws ClassNotFoundException {

        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        System.out.println(System.getProperty("java.version"));

        //三种方式 1 Class.forname() 加载并连接 2 类.class 只是初步工作,未加载 3 实例.class 真实对象的类加载器
        Class clazz = Class.forName("com.fansin.reflect.MyReflect");
        System.out.println("类名全名:" + clazz.getName() + "/" + clazz.getCanonicalName() + "/" + clazz.getSimpleName());
        Annotation[] annotations = clazz.getDeclaredAnnotations();//获取所有的直接注解.
        for (Annotation annotation : annotations) {
            System.out.println(annotation.annotationType());
            System.out.println(((MyAnnotation) annotation).hello());
        }

    }

}
