package com.fansin.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class ArrayListDemo {

    //    static ArrayList<Integer> arrayList = new ArrayList<>();
    static List<Integer> arrayList = Collections.synchronizedList(new ArrayList<Integer>());//线程安全

    public static void main(String[] args) throws InterruptedException {

        //源码 grow 加断点 扩容为1.5倍
        System.out.println("ArrayList扩容测试!");
        ArrayList<String> list = new ArrayList<>();
        int size = list.size();

        System.out.println("可以插入null");
        list.add(null);
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
            if (list.size() > size) {
                size = list.size();

            }
        }

        Runnable run = new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    arrayList.add(i);
                }
            }
        };
        Thread thread1 = new Thread(run);
        Thread thread2 = new Thread(run);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(arrayList.size());
        //三种结果 1 正常 2 小于200000 3 数组越界异常ArrayIndexOutOfBoundsException
        //使用collections工具后,实现线程同步,保证为2000000

    }

}
