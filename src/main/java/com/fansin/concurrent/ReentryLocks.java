package com.fansin.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaofeng on 17-4-10.
 */
public class ReentryLocks {

    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    public static class RenntrThread implements Runnable {

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
            for (int j = 0; j < 10000; j++) {
                lock.lock();
                try {
                    i++;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 最常用的重入锁
     */
    public static void reentryLock() throws InterruptedException {
        Thread t1 = new Thread(new RenntrThread());
        Thread t2 = new Thread(new RenntrThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("结果i=" + i);

    }

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();

    public static class DeadLock implements Runnable {

        private int lock;

        public DeadLock(int lock) {
            this.lock = lock;
        }

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

            try {
                if (lock == 1) {//先获取lock1,再获取lock2
                    lock1.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() + " 开始获取第二个锁");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 完成任务!");
                } else {
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() + " 开始获取第二个锁");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 完成任务!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //发生异常后,会立即释放锁
            } finally {//释放锁
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                    System.out.println(Thread.currentThread().getName() + " 释放锁lock1!");
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                    System.out.println(Thread.currentThread().getName() + " 释放锁lock2!");
                }
                System.out.println(Thread.currentThread().getName() + " 线程退出!");
            }

        }
    }

    public static class TryLock implements Runnable {

        private int lock;

        public TryLock(int lock) {
            this.lock = lock;
        }

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

            try {
                if (lock == 1) {//先获取lock1,再获取lock2
                    lock1.tryLock(2, TimeUnit.SECONDS);
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() + " 开始获取第二个锁");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 完成任务!");
                } else {
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() + " 开始获取第二个锁");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 完成任务!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //发生异常后,会立即释放锁
            } finally {//释放锁
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                    System.out.println(Thread.currentThread().getName() + " 释放锁lock1!");
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                    System.out.println(Thread.currentThread().getName() + " 释放锁lock2!");
                }
                System.out.println(Thread.currentThread().getName() + " 线程退出!");
            }

        }
    }

    public static void deadLock() throws InterruptedException {
        Thread t1 = new Thread(new DeadLock(1));
        Thread t2 = new Thread(new DeadLock(2));
        t1.start();
        t2.start();
        //没有中断,将是死锁,两个线程一直等待 使用jstack将会发现一个死锁
        t2.interrupt();//t2主动放弃资源竞争,并释放锁

    }

    public static void tryLock() throws InterruptedException {
        Thread t1 = new Thread(new DeadLock(1));
        Thread t2 = new Thread(new DeadLock(2));
        t1.start();
        t2.start();
        //没有中断,将是死锁,两个线程一直等待 使用jstack将会发现一个死锁
        t2.interrupt();//t2主动放弃资源竞争,并释放锁

    }

    public static void main(String[] args) throws InterruptedException {
        //重入锁
        reentryLock();
        //死锁/解锁演示
        deadLock();
    }

}
