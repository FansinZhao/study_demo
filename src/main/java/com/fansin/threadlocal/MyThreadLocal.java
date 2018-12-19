package com.fansin.threadlocal;

/**
 * Created by zhaofeng on 17-4-3.
 */
public class MyThreadLocal {

    final ThreadLocal<Integer> local = new ThreadLocal() {

        @Override
        protected Object initialValue() {
            return new Integer(0);
        }
    };

    public Integer get() {
        System.out.println(local.hashCode());//公用一个Threadlocal/Thread
        local.set(local.get() + 1);
        return local.get();
    }

    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();
        Thread thread1 = new Thread(new MyThread(myThreadLocal));
        Thread thread2 = new Thread(new MyThread(myThreadLocal));
        Thread thread3 = new Thread(new MyThread(myThreadLocal));
        thread1.start();
        thread2.start();
        thread3.start();
    }

}

class MyThread implements Runnable {

    private MyThreadLocal myThreadLocal;

    public MyThread(MyThreadLocal myThreadLocal) {
        this.myThreadLocal = myThreadLocal;
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
        System.out.println(Thread.currentThread().getName() + "---->开始");
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "---->" + myThreadLocal.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "---->结束");
    }

}
