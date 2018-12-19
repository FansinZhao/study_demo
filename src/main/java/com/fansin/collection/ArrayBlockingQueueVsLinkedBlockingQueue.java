package com.fansin.collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ArrayBlockingQueueVsLinkedBlockingQueue {

    //队列最大容量
    public static final int Q_SIZE     = 1024;
    //生产者/消费者线程数
    public static final int THREAD_NUM = 4;

    //产品
    class Product {

        String name;

        Product(String name) {
            this.name = name;
        }
    }

    public void test(final BlockingQueue<Product> q) throws InterruptedException {
        //生产者线程
        class Producer implements Runnable {

            @Override
            public void run() {
                for (int i = 0; i < Q_SIZE * 10; i++) {
                    try {
                        q.put(new Product("Lee"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        ;
        //消费者线程
        class Consumer implements Runnable {

            @Override
            public void run() {
                for (int i = 0; i < Q_SIZE * 10; i++) {
                    try {
                        q.take();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        ;
        //创建生产者
        Thread[] arrProducerThread = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            arrProducerThread[i] = new Thread(new Producer());
        }
        //创建消费者
        Thread[] arrConsumerThread = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            arrConsumerThread[i] = new Thread(new Consumer());
        }
        //go!
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_NUM; i++) {
            arrProducerThread[i].start();
            arrConsumerThread[i].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            arrProducerThread[i].join();
            arrConsumerThread[i].join();
        }
        long t2 = System.currentTimeMillis();
        System.out.println(q.getClass().getSimpleName() + " cost : " + (t2 - t1));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().totalMemory());
        final BlockingQueue<Product> q1 = new LinkedBlockingQueue<Product>(Q_SIZE);
        new ArrayBlockingQueueVsLinkedBlockingQueue().test(q1);
//		final BlockingQueue<Product> q2 = new ArrayBlockingQueue<Product>(Q_SIZE);
//		new ArrayBlockingQueueVsLinkedBlockingQueue().test(q2);
        System.out.println(Runtime.getRuntime().totalMemory());
		/*
		187170816
		LinkedBlockingQueue cost : 8015
		620756992
		*
		187170816
		ArrayBlockingQueue cost : 3539
		537919488


187170816
LinkedBlockingQueue cost : 65
187170816


187170816
ArrayBlockingQueue cost : 75
187170816

		*
		* */
    }
}
