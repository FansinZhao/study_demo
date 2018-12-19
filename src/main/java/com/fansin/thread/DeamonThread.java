package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class DeamonThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    System.out.println(" i am alive");//如果没有守护线程,那么这里会一直打印
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);//表示主线程结束后,如果只有守护线程,线程将会停止
        thread.start();
        Thread.sleep(2000);
    }

}
