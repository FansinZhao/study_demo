package com.fansin.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaofeng on 17-5-2.
 */
public class TraceThreadPoolExecutorDemo {

    public static void main(String[] args) {

        System.out.println("未做修改的ThreadPoolExecutor,只会打印一个线程的异常");
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(6, 6, 0, TimeUnit.SECONDS, new SynchronousQueue<>());
        poolExecutor.execute(new TraceTask(1, 1));
        System.out.println("只有execute才会打印异常");
        poolExecutor.execute(new TraceTask(2, 0));
        poolExecutor.submit(new TraceTask(3, 1));
        poolExecutor.submit(new TraceTask(4, 0));
        poolExecutor.submit(new TraceCallable<Double>(5, 1));
        poolExecutor.submit(new TraceCallable<Double>(6, 0));
        poolExecutor.shutdown();

        System.out.println("能够捕获异常的线程池,可以打印3个异常线程的异常");
        TraceThreadPoolExecutor tracePoolExecutor = new TraceThreadPoolExecutor(6, 6, 0, TimeUnit.SECONDS,
                                                                                new SynchronousQueue<>());
        tracePoolExecutor.execute(new TraceTask(1, 1));
        tracePoolExecutor.execute(new TraceTask(2, 0));
        tracePoolExecutor.submit(new TraceTask(3, 1));
        tracePoolExecutor.submit(new TraceTask(4, 0));
        tracePoolExecutor.submit(new TraceCallable<Double>(5, 1));
        tracePoolExecutor.submit(new TraceCallable<Double>(6, 0));
        tracePoolExecutor.shutdown();
    }

}

class TraceTask implements Runnable {

    int a, b;

    public TraceTask(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + a + "/" + b + " run start");
        double c = a / b;//b=0 异常
        System.out.println(a + "/" + b + " run:" + c);
    }
}

class TraceCallable<E> implements Callable<E> {

    int a, b;

    public TraceCallable(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public E call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " " + a + "/" + b + " call start");
        Object obj = a / b;//b=0 异常
        System.out.println(a + "/" + b + " call:" + obj);
        return (E) obj;
    }
}
