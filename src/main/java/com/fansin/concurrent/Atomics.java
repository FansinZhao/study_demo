package com.fansin.concurrent;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by zhaofeng on 17-4-4.
 */
public class Atomics {

    public static void main(String[] args) {
        System.out.println((Integer.MAX_VALUE + "").length());
        System.out.println((Long.MAX_VALUE + "").length());
        System.out.println((Short.MAX_VALUE + "").length());
        System.out.println((Byte.MAX_VALUE + "").length());

        //一次优惠券
        atomStampReference();
    }

    static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(29, 0);

    public static void atomStampReference() {
        for (int i = 0; i < 3; i++) {
            //z最开始的时间
            final int stamp = money.getStamp();
            //充值线程
            new Thread(new Runnable() {

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
                    while (true) {//持续查询

                        while (true) {//加优惠券
                            //拿到金额
                            Integer m = money.getReference();
                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20, stamp, stamp + 1)) {
                                    System.out.println("服务端 用户满足条件,赠送优惠券.20元:剩余金额:" + (m + 20));
                                    break;
                                }
                            } else {
                                System.out.println("服务端 金额大于20,不需要充值剩余金额:" + m);
                                break;
                            }

                        }
                    }

                }
            }).start();

            //消费线程
            new Thread(new Runnable() {

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

                    for (int j = 0; j < 10; j++) {
                        while (true) {
                            int stp = money.getStamp();
                            Integer m = money.getReference();
                            if (m > 10) {
                                if (money.compareAndSet(m, m - 10, stp, stp + 1)) {
                                    System.out.println("客户端 消费10元!剩余金额:" + (m - 10));
                                    break;
                                }
                            } else {
                                System.out.println("客户端 余额不足,需要充值!剩余金额:" + m);
                                break;
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

}
