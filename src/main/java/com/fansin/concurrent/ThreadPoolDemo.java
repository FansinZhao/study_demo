package com.fansin.concurrent;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaofeng
 * @date: 18-12-20 16:29
 */
@Slf4j
public class ThreadPoolDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                                                                 new ArrayBlockingQueue<>(10),
                                                                 new NamedThreadFactory("被抓的线程",
                                                                                        new ThreadGroup("内存安全线程组"),
                                                                                        false));

        poolExecutor.execute(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    if (Thread.currentThread().isInterrupted()) {
                        log.info("线程中断退出！");
                        break;
                    }

                    String time = LocalDateTime.now().toString();
                    log.info("01 步骤");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        //log
                        log.info(" Ssleep中断异常不处理 ");
                        Thread.currentThread().interrupt();
                    }
                    log.info("02 步骤");
                }
            }
        });

        poolExecutor.shutdown();

        try {
            boolean b = poolExecutor.awaitTermination(1, TimeUnit.SECONDS);
            log.info("线程池已关闭 = " + b);
            if (!b) {
                List<Runnable> runnables = poolExecutor.shutdownNow();
                log.info("未执行的 runnables.size() = " + runnables.size());
            }
        } catch (InterruptedException e) {
            //log
            e.printStackTrace();
        }

        log.info(" 线程池结束了?真的？！ ");

        while (true) {
            //空跑
        }
    }

    static class LeakedThreadPool {

        public static void main(String[] args) {

            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                                                                     new ArrayBlockingQueue<>(10),
                                                                     new NamedThreadFactory("逃逸线程",
                                                                                            new ThreadGroup("内存泄漏线程组"),
                                                                                            false));

            poolExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        System.out.println(Thread.currentThread().getName() + " " + LocalDateTime.now().toString());
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            //log
                            e.printStackTrace();

                        }
                    }
                }
            });

            poolExecutor.shutdown();

            try {
                poolExecutor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //log
                e.printStackTrace();
            }

            System.out.println(" 线程池结束了? ");

            while (true) {
                //空跑
            }

        }

    }

    static class LeakedThreadPool1 {

        public static void main(String[] args) {

            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                                                                     new ArrayBlockingQueue<>(10),
                                                                     new NamedThreadFactory("逃逸线程",
                                                                                            new ThreadGroup("内存泄漏线程组"),
                                                                                            false));

            for (int i = 0; i < 1000; i++) {

                poolExecutor.execute(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + " " + LocalDateTime.now().toString());
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            //log
                            e.printStackTrace();

                        }
                    }
                });
            }

            poolExecutor.shutdown();

            try {
                poolExecutor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //log
                e.printStackTrace();
            }

            System.out.println(" 线程池结束了? ");

        }

    }

}


