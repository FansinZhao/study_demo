package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class JoinDemo {

    public static volatile int i = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (i = 0; i < 10000; i++) {

                }
            }
        });

        t1.start();
        //
//        t1.isAlive();
        t1.join();
        System.out.println("i=" + i);//如果不加jion,i=0
    }

}
