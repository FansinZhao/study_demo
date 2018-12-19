package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-8.
 */
public class SychronizedDemo {

    /***
     * 1 synchronized func()
     * 2 synchronized(obj)
     * 3 synchronized(class)
     *
     */

    public static class User {

        private int id;
        private int num;

        public User() {
            this.id = 0;
            this.num = 0;
        }

        public void increNum() {
            this.num++;
        }

        public synchronized void incre() {
            this.id++;
        }

        @Override
        public String toString() {
            return this.id + " > " + this.num;
        }

    }

    public static void clazz() {
        long start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 10; i++) {//并发量越大越明显
            new Thread(new Runnable() {

                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 进入...");
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " ....离开");
                }
            }).start();
        }
        long end = System.currentTimeMillis() / 1000;
        System.out.println("未使用clazz锁" + num + "  耗时s " + (end - start));

        start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 10; i++) {//
            new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (User.class) {//可以看到进程进入/离开是成对的,所有涉及对象的都会加锁
                        System.out.println(Thread.currentThread().getName() + " 进入...");
                        try {
                            Thread.sleep(1000l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + " ....离开");
                    }
                }
            }).start();
        }
        end = System.currentTimeMillis() / 1000;
        System.out.println("使用clazz锁" + num + "  耗时s " + (end - start));
    }

    static Object object = new Object();
    static int    num    = 0;

    /***
     *
     */
    public static void obj() {
        long start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 10000; i++) {//并发量越大越明显
            new Thread(new Runnable() {

                @Override
                public void run() {
                    ++num;
                }
            }).start();
        }
        long end = System.currentTimeMillis() / 1000;
        System.out.println("未使用obj锁" + num + "  耗时s " + (end - start));

        num = 0;//初始化
        start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 10000; i++) {//并发量越大越明显
            new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (object) {
                        ++num;//借助对象,将对象作为临界资源
                    }
                }
            }).start();
        }
        end = System.currentTimeMillis() / 1000;
        System.out.println("使用obj锁" + num + "  耗时s " + (end - start));
    }

    static User user = new User();

    /**
     * 保证同一个对象中的id是顺序执行的
     */
    public static void fun() {

        Runnable run = new Runnable() {

            @Override
            public void run() {
                user.increNum();
            }
        };
        long start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 100000; i++) {//并发量越大越明显 100w时同步锁的性能明显下降
            new Thread(run).start();
        }
        long end = System.currentTimeMillis() / 1000;
        System.out.println("未加func同步锁" + user.num + "  耗时s " + (end - start));
        Runnable r = new Runnable() {//使用加锁方法必须要保证实例一直,即锁一致

            @Override
            public void run() {
                user.incre();//保证加锁对象有序正常增加
            }
        };
        start = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 100000; i++) {
            new Thread(r).start();
        }
        end = System.currentTimeMillis() / 1000;
        System.out.println("加func同步锁" + user.num + "  耗时s " + (end - start));

    }

    public static void main(String[] args) {
        // 普通方法,必须保证一个实例,也就是说new Thread(instance)必须是一个实例
        fun();
        //
        obj();
        //
        clazz();
    }

}
