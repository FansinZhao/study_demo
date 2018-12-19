package com.fansin.concurrent;

import java.util.concurrent.*;

/**
 * Created by zhaofeng on 17-5-2.
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrapRunnable(command));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrapRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(wrapCallable(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return super.submit(wrapRunnable(task), result);
    }

    /**
     * 自定义一个异常
     */
    private Exception clientTrace() {
        return new Exception("Client stack trace! ");
    }

    /*捕获异常*/
    private Runnable wrapRunnable(final Runnable task) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    new Exception("Exception in thread \"" + Thread.currentThread().getName() + "\"",
                                  e).printStackTrace();
//                    throw e;
                }
            }
        };
    }

    private Callable wrapCallable(final Callable callable) {
        return new Callable() {

            @Override
            public Object call() throws Exception {
                try {
                    return callable.call();
                } catch (Exception e) {
                    new Exception("Exception in thread \"" + Thread.currentThread().getName() + "\"",
                                  e).printStackTrace();
//                    throw e;
                    return null;
                }
            }
        };
    }
}
