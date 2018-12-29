package com.fansin.concurrent;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author: zhaofeng
 * @date: 18-12-21 14:35
 */
@Slf4j
public class CallableFutureDemo {

    static class UnStableCallableDemo {

        public static void main(String[] args) {
            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                                                                     new ArrayBlockingQueue<>(2),
                                                                     new NamedThreadFactory("无序等待", new ThreadGroup(
                                                                             "CallableFuture"), false));
            Future<String> future1 = poolExecutor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    log.info("等待3s");
                    Thread.sleep(3000L);
                    return "3s";
                }
            });
            Future<String> future2 = poolExecutor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    log.info("等待1s");
                    Thread.sleep(3000L);
                    return "1s";
                }
            });

            try {
                String r1 = future1.get();
                log.info(r1);
                String r2 = future2.get();
                log.info(r2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    static class StableCallableDemo {

        public static void main(String[] args) {
            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                                                                     new ArrayBlockingQueue<>(2),
                                                                     new NamedThreadFactory("最新完成", new ThreadGroup(
                                                                             "CallableFuture"), false));

//            new ExecutorCompletionService<String>(poolExecutor,new ArrayBlockingQueue<>())

        }
    }
}
