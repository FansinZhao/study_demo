package com.fansin.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class AtomVolatile {

    public static void main(String[] args) {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
//            threads[i] = new Thread(new NoAtom("thread-"+i));//无法保证原子性
            threads[i] = new Thread(new AAtom("thread-" + i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

}

class NoAtom implements Runnable {

    private String name;

    public NoAtom(String name) {
        this.name = name;
    }

    public volatile static int num;

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

        for (int i = 0; i < 100; i++) {
            num++;
        }
        System.out.println(name + " ---->> " + num);

    }
}

class AAtom implements Runnable {

    private String name;

    public AAtom(String name) {
        this.name = name;
    }

    public volatile static AtomicInteger num = new AtomicInteger(0);

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

        for (int i = 0; i < 100; i++) {
            num.incrementAndGet();
        }
        System.out.println(name + " ---->> " + num);

    }
}
