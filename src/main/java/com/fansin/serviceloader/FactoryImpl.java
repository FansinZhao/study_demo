package com.fansin.serviceloader;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class FactoryImpl extends AbstractFactory {

    public FactoryImpl() {
    }

    @Override
    void create() {
        System.out.println("抽象类也是可以使用serviceloader");
    }
}
