package com.fansin.serviceloader;

/**
 * Created by zhaofeng on 17-5-8.
 */
public interface IService<T> {

    void execute();

    T execute(String args[]);

}
