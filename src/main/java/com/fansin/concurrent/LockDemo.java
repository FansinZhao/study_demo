package com.fansin.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by zhaofeng on 17-4-4.
 */
public class LockDemo {

    public static void main(String[] args) throws InterruptedException {
        //排他 重入锁
        reentrylock();

        //排他/共享 可重入读写锁
        reentrywriteread();

        //共享锁
        //信号量
        semaphore();
        //倒计时锁
        countDownLatch();
        //java8 新加锁
        stamplock();
    }

    static CountDownLatch latch = new CountDownLatch(10);

    public static void countDownLatch() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            service.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep((new Random().nextInt(10)) * 1000);
                        System.out.println(Thread.currentThread().getId() + "发射火箭准备完成....");
                        latch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        latch.await();
        System.out.println("火箭发射成功!");
        service.shutdown();

    }

    static final Semaphore semaphore = new Semaphore(5);

    public static void semaphore() {
        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {

            service.submit(new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " done!");//五个一组
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        service.shutdown();

    }

    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void reentrywriteread() {
        Integer i = 1;
        readWriteLock.writeLock().lock();
        System.out.println(i++);
        readWriteLock.writeLock().unlock();
        readWriteLock.readLock().lock();
        System.out.println(">>>" + i);
        readWriteLock.readLock().unlock();

    }

    static StampedLock stampedLock = new StampedLock();
    static double      x           = 0, y = 0;

    public static void stamplock() {

        //d写锁
        long stamp = stampedLock.writeLock();
        System.out.println("写锁:" + stamp);
        x += 10;
        y += 20;
//        stamp++;//报错IllegalMonitorStateException ,说明锁是有效果的
        stampedLock.unlockWrite(stamp);

        //读
        stamp = stampedLock.readLock();
        System.out.println("读锁:" + stamp);
        System.out.println(x + " " + y);
        stampedLock.unlockRead(stamp);

        //
        System.out.println(distanceFromOrigin());
        //
        moveIfAtOrigin(1, 2);
    }

    //乐观锁
    public static double distanceFromOrigin() { // A read-only method
        long stamp = stampedLock.tryOptimisticRead();//乐观锁,还没有获取读锁
        double currentX = x, currentY = y;
        if (!stampedLock.validate(stamp)) {//是否有读锁,没有,则获取读悲观锁
            stamp = stampedLock.readLock();//
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    //悲观锁
    public static void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = stampedLock.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = stampedLock.tryConvertToWriteLock(stamp);//是否有写锁,如果有则获取,没有则释放读锁.在获取写锁
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    stampedLock.unlockRead(stamp);
                    stamp = stampedLock.writeLock();
                }
            }
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    static ReentrantLock lock      = new ReentrantLock();
    static Condition     condition = lock.newCondition();

    public static void reentrylock() throws InterruptedException {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                lock.lock();
                lock.lock();//可重入
                try {
                    condition.await();
                    System.out.println("线程运行中....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    lock.unlock();
                }
            }
        });
        t.start();
        Thread.sleep(1000);
        lock.lock();
        //没有这个就会一直等待
        condition.signal();
        lock.unlock();

    }

}
