package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class MyVolatile {

    public static void main(String[] args) throws InterruptedException {
        NoThread run = new NoThread();
//        VThread  run = new VThread();
        Thread thread = new Thread(run);
        thread.start();
        Thread.sleep(1000);
        run.setRunning(false);
    }

}

class NoThread implements Runnable {

    private boolean running = true;

    public void setRunning(boolean running) {
        this.running = running;
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
        System.out.println("没有使用volatile变量的线程.......开始了");

        while (running) {
//            System.out.println("运行中....");
            //使用输出,注意捕获异常
        }

        System.out.println("没有使用volatile变量的线程.......终于能结束了");
    }
}

class VThread implements Runnable {

    volatile private boolean running = true;

    public void setRunning(boolean running) {
        this.running = running;
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
        System.out.println("使用volatile变量的线程.......开始了");

        while (running) {
//            System.out.println("volatile运行中....");
        }

        System.out.println("使用volatile变量的线程.......结束了");
    }
}


