package com.fansin.reflect.dynamic;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by zhaofeng on 17-4-4.
 */
public class MyMethodInterceptor implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        //生成目标类子类,不能对final类做代理
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //使用字节码生成代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("代理前期工作.....");
//        System.out.println(method.invoke(o,objects));
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("代理后期工作.....");

        return result;
    }
}
