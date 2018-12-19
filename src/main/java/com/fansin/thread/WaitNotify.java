package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-7.
 */
public class WaitNotify {

    static final Object object = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (object) {
                    if (Thread.interrupted()) {
                        System.out.println("t1中断停止...");
                    }
                    System.out.println("t1等待.....");
                    try {
//                        object.notify();//notify 可以唤醒空队列
                        object.wait();
                        System.out.println("t1继续执行....结束");
                        object.notifyAll();//通知下一个线程
                    } catch (InterruptedException e) {
                        System.out.println("t1中断异常");
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (object) {
                    if (Thread.interrupted()) {
                        System.out.println("t3中断停止...");
                    }
                    System.out.println("t3等待.....");
                    try {
                        object.wait();
                        System.out.println("t3继续执行....结束");
                        object.notifyAll();//通知下一个线程
                    } catch (InterruptedException e) {
                        System.out.println("t3中断异常");
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (object) {
                    if (Thread.interrupted()) {
                        System.out.println("t2中断停止...");
                    }
//                    object.notify();
                    object.notifyAll();//只能通知一个线程
                    System.out.println("t2通知.....");
                    try {
                        Thread.sleep(2000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2结束.....");
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

    }

}
