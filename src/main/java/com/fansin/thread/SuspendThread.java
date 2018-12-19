package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-7.
 */
public class SuspendThread {

    public static final Object object = new Object();

    public static class SupendThread implements Runnable {

        boolean suspend = false;

        public void suspend() {
            suspend = true;
        }

        public void resume() {
            suspend = false;
            synchronized (this) {
                notify();
            }
        }

        @Override
        public void run() {

            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("suspend异常中断!");
                    break;
                }
                synchronized (this) {//判断是否获取当前实例,借此实现resume/suspend,比suspend优点,没有资源占用
                    while (suspend) {
                        System.out.println(Thread.currentThread().getName() + "线程挂起");
//                Thread.currentThread().suspend();
                        try {

                            wait();
                        } catch (InterruptedException e) {
                            System.out.println("suspend中断 " + e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                    }
                    synchronized (object) {//多线程交互锁
                        System.out.println(Thread.currentThread().getName() + "suspend 线程执行");
                    }
                    Thread.yield();
                }
            }

        }
    }

    public static class ReadSuspendThread implements Runnable {

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("read 异常中断!");
                    break;
                }
                synchronized (object) {
                    System.out.println("read 获取object锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("read 中断");
                        Thread.currentThread().interrupt();
                    }
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        Runnable r1 = new SupendThread();
//        Runnable r2 = new SupendThread();
//        Thread t1 = new Thread(r1);
//        Thread t2 = new Thread(r2);
//        t1.start();
//        Thread.sleep(100);
//        t2.start();
////        t1.resume();
////        t2.resume();
//        t1.join();
//        t2.join();
        SupendThread r1 = new SupendThread();
        Thread t1 = new Thread(r1);
        t1.start();
        ReadSuspendThread r2 = new ReadSuspendThread();
        Thread t2 = new Thread(r2);
        t2.start();
        Thread.sleep(1000);
        r1.suspend();
        System.out.println("suspend t1 2s");
        Thread.sleep(2000l);
        System.out.println("resume");
        r1.resume();
        t1.interrupt();
        t2.interrupt();

    }

}
