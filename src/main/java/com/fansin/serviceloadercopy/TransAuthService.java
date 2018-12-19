package com.fansin.serviceloadercopy;

import com.fansin.serviceloader.IService;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class TransAuthService<T> implements IService<T>, IMyService {

    public TransAuthService() {
        System.out.println("重复加载会创建多个实例!!!!");
    }

    @Override
    public void execute() {
        System.out.println("TransAuthService execute().........");
    }

    @Override
    public T execute(String[] args) {
        return null;
    }

    @Override
    public void printMsg() {
        System.out.println("重复加载! TransAuthService IMyService.printMsg()...... ");
    }
}
