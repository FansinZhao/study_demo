package com.fansin.uid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaofeng on 17-4-4.
 */
public class GlobalIDGenerator {

    /**
     * 16进制
     * timestamp + machine + pid + inc
     * 8          8          7     8     位
     */

    public static void main(String[] args) {
        GlobalIDGenerator generator = new GlobalIDGenerator();
        Thread t1 = new Thread(new MyThread(generator));
        Thread t2 = new Thread(new MyThread(generator));
        Thread t3 = new Thread(new MyThread(generator));
        t1.start();
        t2.start();
        t3.start();
    }

    public String getId() {
        StringBuffer sb = new StringBuffer();
        sb.append(getTimeStamp()).append(getMachine()).append(getPid()).append(getInc());
        return sb.toString();
    }

    /**
     * 时间戳
     */
    public String getTimeStamp() {
        return Long.toHexString(System.currentTimeMillis() / 1000);
    }

    /**
     * 机器名称
     */
    public String getMachine() {

        String os = System.getProperty("os.name");
        if ("linux".equalsIgnoreCase(os)) {
            try {
                Process process = Runtime.getRuntime().exec("hostname");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //这里可以多添加一些信息
                String name = reader.readLine();
//                System.out.println(name.hashCode());
                //这里可以采用md5/sha等算法
                return Integer.toHexString(name.hashCode());

            } catch (IOException e) {
                return null;
            }
        } else {
            //TODO windows
            return "";
        }

    }

    /**
     * pid
     *
     * @return
     */
    public String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
//        System.out.println(pid.hashCode());
        return Integer.toHexString(pid.hashCode());
    }

    volatile AtomicLong    al   = new AtomicLong(1000000000);
    final    ReentrantLock lock = new ReentrantLock();

    /**
     * pid
     *
     * @return
     */
    public String getInc() {

        lock.lock();
        long l = al.incrementAndGet();
        lock.unlock();
        return Long.toHexString(l);

    }

}

class MyThread implements Runnable {

    private GlobalIDGenerator generator;

    public MyThread(GlobalIDGenerator generator) {
        this.generator = generator;
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
        for (int i = 0; i < 3; i++) {
            //输出的id个位数是顺序的
            System.out.println(Thread.currentThread().getName() + "获取唯一id:" + generator.getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
