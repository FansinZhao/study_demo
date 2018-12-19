package com.fansin.serviceloader;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class MessageService<T> implements IService<T> {

    public MessageService() {
        System.out.println("必须保留无参数构造函数!");
    }

    @Override
    public void execute() {
        //TODO 异步业务处理
        System.out.println("MessageService  execute().....");

    }

    @Override
    public T execute(String[] args) {
        System.out.println("MessageService  execute(String[] args).....");
        //TODO 同步业务处理
        return null;
    }
}
