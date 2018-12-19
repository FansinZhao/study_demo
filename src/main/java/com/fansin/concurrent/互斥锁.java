package com.fansin.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by zhaofeng on 17-5-24.
 */
public class 互斥锁 implements Lock {

    private Sync sync;

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquire(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondtion();
    }

    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 是否为排它锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
//            return super.tryAcquire(arg);
            assert arg == 1;
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());//设置当前线程获取排它锁
                return true;
            } else {
                return false;
            }
        }

        /**
         * 释放排它锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
//            return super.tryRelease(arg);
            assert arg == 1;
            if (getState() == 0) {//
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);//设置当前线程获取排它锁
            setState(0);
            return true;
        }

        /**
         * 是否为共享锁
         *
         * @param arg
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }

        /**
         * 释放共享锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }

        /**
         * 是否当前线程为独占(或者说是否已经占有)
         *
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
//            return super.isHeldExclusively();
            return getState() == 1;
        }

        Condition newCondtion() {
            return new ConditionObject();//AQS中的类
        }

        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }

    }

}
