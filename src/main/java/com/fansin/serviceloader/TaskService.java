package com.fansin.serviceloader;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class TaskService<T> implements IService<T> {

    public TaskService() {
    }

    @Override
    public void execute() {
        System.out.println("TaskService execute()....");
    }

    @Override
    public T execute(String[] args) {
        System.out.println("TaskService execute(String[] args)....");
        return null;
    }

}
