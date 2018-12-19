package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-7.
 */
public class InteruptedThread {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    if (Thread.interrupted()) {
                        System.out.println("中断退出!");
                        break;
                    }
                    System.out.println("ok,如果没有对中断判断,将不会终止!");
                }

            }
        });
        t.start();
        Thread.sleep(1000l);
        t.interrupt();
    }

}
