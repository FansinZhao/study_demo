package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class ThreadGroup {

    public static void main(String[] args) {
        java.lang.ThreadGroup tg = new java.lang.ThreadGroup("printgroup");
        Thread t1 = new Thread(tg, new Runnable() {

            @Override
            public void run() {

                while (true) {
                    System.out.println("I am " + Thread.currentThread().getThreadGroup().getName() +
                                       Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "T1");
        Thread t2 = new Thread(tg, new Runnable() {

            @Override
            public void run() {

                while (true) {
                    System.out.println("I am " + Thread.currentThread().getThreadGroup().getName() +
                                       Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "T2");
        t1.start();
        t2.start();
        System.out.println("group alive 线程 " + tg.activeCount());
        tg.list();
    }
}
