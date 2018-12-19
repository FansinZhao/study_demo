package com.fansin.serviceloader;

import com.fansin.serviceloadercopy.IMyService;
import com.mysql.jdbc.Driver;

import java.util.ServiceLoader;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class ServiceLoaderMain {

    public static void main(String[] args) {

        /*
         * 类似spring
         * 1可以加载任何制定的普通类,接口,抽象类的子类.
         * 2必须含有无参构造函数
         * 3必须放在META-INF/services目录下
         *
         * */

        ServiceLoader<IService> serviceLoader = ServiceLoader.load(IService.class);
        for (IService service : serviceLoader) {
            service.execute();
        }
        System.out.println("----------------使用ServiceLoader.loadInstalled()\n如果已经加载,则不会再加载!");
        ServiceLoader<IService> serviceLoader0 = ServiceLoader.loadInstalled(IService.class);
        for (IService service : serviceLoader0) {
            service.execute();
        }

        System.out.println("---------------使用 ServiceLoader.load(class,classloader)");
        //使用类加载器
        ServiceLoader<IMyService> serviceLoader1 = ServiceLoader.load(IMyService.class,
                                                                      IMyService.class.getClassLoader());
        for (IMyService service : serviceLoader1) {
            service.printMsg();
        }
        //加载jar包中的类
        System.out.println("-----------------加载mysql.jar包类");
        ServiceLoader<Driver> serviceLoader2 = ServiceLoader.load(Driver.class);
        for (Driver service : serviceLoader2) {
            service.hashCode();
        }
        System.out.println("没有找到???");

        System.out.println("----------------加载抽象类");
        ServiceLoader<AbstractFactory> serviceLoader3 = ServiceLoader.load(AbstractFactory.class);
        for (AbstractFactory service : serviceLoader3) {
            service.create();
        }

        System.out.println("----------------加载普通类");
        ServiceLoader<Object> serviceLoader4 = ServiceLoader.load(Object.class);
        for (Object service : serviceLoader4) {
            ((TaskService) service).execute();
        }
    }
}
