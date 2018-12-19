package com.fansin.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaofeng
 * @date: 18-12-18 16:19
 */
public class ExchangerDemo {

    public static void main(String[] args) {

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                                                                 new ArrayBlockingQueue<>(2));

        Exchanger<String> exchanger = new Exchanger<>();

        poolExecutor.submit(new MyExchanger(exchanger, "数据A"));
        poolExecutor.submit(new MyExchanger(exchanger, "数据B"));

        poolExecutor.shutdown();
        try {
            poolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class MyExchanger implements Runnable {

        private Exchanger<String> exchanger;
        private String            msg;

        public MyExchanger(Exchanger exchanger, String msg) {
            this.exchanger = exchanger;
            this.msg = msg;
        }

        @Override
        public void run() {

            try {
                String changeMsg = exchanger.exchange(msg);
                System.out.printf("交换数据 %s(前) <> %s (后)\n", msg, changeMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
