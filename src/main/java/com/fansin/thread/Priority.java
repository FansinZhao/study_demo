package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class Priority {

    public static class HighPriority implements Runnable {

        private int count = 0;

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
                synchronized (Priority.class) {
                    count++;
                    if (count > 1000000) {
                        System.out.println("High Thread 已经完成!");
                        break;
                    }

                }
            }

        }
    }

    public static class LowPriority implements Runnable {

        private int count = 0;

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
                synchronized (Priority.class) {
                    count++;
                    if (count > 1000000) {
                        System.out.println("Low Thread 已经完成!");
                        break;
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new HighPriority());
        Thread t2 = new Thread(new LowPriority());
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.start();
        t1.start();
        //结果并不一定是high先完成

    }

}
