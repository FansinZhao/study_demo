package com.fansin.designpattern;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.TimeIntervalAspect;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhaofeng on 17-5-9.
 */
public class ProxyDemo {

    /**
     * 代理模式
     * 静态代理模式:编译阶段的代理
     * 动态代理模式:运行时阶段的代理
     * <p>
     * 应用场景:
     * 对代码中任意的方法进行监控
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("------------静态代理的弊端就是代理类与目标类的绑定,导致类膨胀------------");
        ILogin real = new UserLogin();
        ILogin proxy = new StaticProxy(real);
        proxy.login("zhaofeng", "123456");
        proxy.logout();
        System.out.println("-----------动态代理有两种:jdk/cglib--------------");
        System.out.println("-----jdk 要求目标类必须是接口实现类,利用反射生成的class是继承Proxy----------------");
        ILogin proxyClazz = (ILogin) Proxy.newProxyInstance(ILogin.class.getClassLoader(), new Class[] { ILogin.class },
                                                            new JdkDynamicHandler(real));
        proxyClazz.login("Fansin", "123456");
        proxyClazz.logout();
        System.out.println("---------------hutool工具类 本质还是jdk动态代理-------------------");
        ILogin htProxy = ProxyUtil.proxy(real, TimeIntervalAspect.class);
        htProxy.login("hutool", "123456");
        htProxy.logout();
        System.out.println("-----------------cglib 借助帮助工具Enhancer----------------------");
        UserLogin userLogin = new CglibDynamicHandler<UserLogin>().newInstance(UserLogin.class);
        userLogin.login("cglib", "123456");
        userLogin.logout();
    }
}

class CglibDynamicHandler<T> implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public T newInstance(Class<T> clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    /**
     * @param obj    目标对象
     * @param method jdk拦截方法
     * @param args   参数
     * @param proxy  cglib快速拦截代理
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        TimeInterval timeInterval = DateUtil.timer();
        Object result;
        result = proxy.invokeSuper(obj, args);
        System.out.println("timeInterval.intervalSecond() = " + timeInterval.intervalSecond());
        System.out.println("result = " + result);
        return result;
    }
}

class JdkDynamicHandler implements InvocationHandler {

    private Object object;

    public JdkDynamicHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TimeInterval timeInterval = DateUtil.timer();
        Object result = method.invoke(object, args);
        System.out.println("timeInterval.intervalSecond() = " + timeInterval.intervalSecond());
        System.out.println("result = " + result);
        return result;
    }
}

interface ILogin {

    void login(String username, String password);

    void logout();
}

class UserLogin implements ILogin {

    @Override
    public void login(String username, String password) {
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("登入");
    }

    @Override
    public void logout() {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("登出");
    }
}

/**
 * 实现接口是为了方便替换对象
 */
class StaticProxy implements ILogin {

    private ILogin target;

    public StaticProxy(ILogin target) {
        this.target = target;
    }

    @Override
    public void login(String username, String password) {
        TimeInterval timeInterval = DateUtil.timer();
        target.login(username, password);
        System.out.println("timeInterval = " + timeInterval.intervalSecond());
    }

    @Override
    public void logout() {
        TimeInterval timeInterval = DateUtil.timer();
        target.logout();
        System.out.println("timeInterval = " + timeInterval.intervalSecond());

    }
}
