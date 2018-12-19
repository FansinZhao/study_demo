package com.fansin.collection;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by zhaofeng on 17-4-20.
 */
public class QueueDemo {

    public static void main(String[] args) {
        //1 并发链表队列
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        //2 无界,无需指定大小,异步特性:一次插入2个元素
        //3 线程安全,无锁等待,可以采用多线程并发修改,性能很好
        //4 FIFO
        concurrentLinkedQueue.add("1");
        concurrentLinkedQueue.offer("1");
        concurrentLinkedQueue.add("2");
        //不允许为null
        try {
            concurrentLinkedQueue.add(null);
        } catch (NullPointerException e) {
            System.out.println("不允许插入null");
        }
        System.out.println("element:" + concurrentLinkedQueue.element());
        System.out.println("peek:" + concurrentLinkedQueue.peek());
        System.out.println("poll:" + concurrentLinkedQueue.poll());

        System.out.println("foreach 遍历:");
        for (Object o : concurrentLinkedQueue) {
            System.out.println(o);
        }
        System.out.println("foreach 删除");
        for (Object o : concurrentLinkedQueue) {
            concurrentLinkedQueue.remove(o);
            System.out.println(o);
        }
        System.out.println("foreach删除后:" + concurrentLinkedQueue.poll());

    }

}

class DequeDemo {

    public static void main(String[] args) {
        //线性双端,不能索引,可做栈,队列,依赖equals/hashcode
        //在listdemo已使用
        System.out.println("-----------------LinkedList-----------------");
        LinkedList linkedList = new LinkedList();
        //栈 FILO先进后出
        linkedList.push("stack1");
        linkedList.push("stack2");
        linkedList.push("stack3");
        System.out.println("栈:先进后出");
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        //队列,先进先出
        System.out.println("队列,先进先出");
        linkedList.add(0, "queue1");
        linkedList.add("queue2");
        //允许插入null
        linkedList.add(null);
        linkedList.offer("queue3");
        System.out.println(linkedList.poll());
        System.out.println(linkedList.remove());
        System.out.println(linkedList.removeLast());
        //双向队列
        System.out.println("双向队列,两头都可以进或出");
        linkedList.addFirst("head1");
        linkedList.offerFirst("head2");
        linkedList.addLast("tail1");
        linkedList.offerLast("tail2");
        System.out.println(linkedList.getFirst());
        System.out.println(linkedList.getLast());
        System.out.println(linkedList.getFirst());
        System.out.println(linkedList.getLast());
        System.out.println("-----------------ArrayDeque-----------------");
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add("a");
        arrayDeque.offer("b");
        try {
            arrayDeque.offer(null);
        } catch (NullPointerException e) {
            System.out.println("不能插入null元素!");
        }
        //允许重复
        arrayDeque.addLast("c");
        arrayDeque.offerLast("c");
        arrayDeque.addFirst("0");
        arrayDeque.addFirst("1");
        arrayDeque.offerFirst("2");
        Iterator iterator = arrayDeque.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o.equals("0")) {
                System.out.println("删除0:");
                iterator.remove();//在循环中删除,只能使用这个方式
            } else {
                System.out.println(o);
            }
        }
        System.out.println("-----------------ConcurrentLinkedDeque-----------------");
        ConcurrentLinkedDeque concurrentLinkedDeque = new ConcurrentLinkedDeque();
        concurrentLinkedDeque.add("b");
        try {
            concurrentLinkedDeque.addLast(null);
        } catch (NullPointerException e) {
            System.out.println("不能插入null元素!");
        }
        concurrentLinkedDeque.addFirst("a");
        concurrentLinkedDeque.offer("c");
        concurrentLinkedDeque.offerLast("d");
        System.out.println("大小:" + concurrentLinkedDeque.size());//每次都是要遍历的,耗时
        for (Object o : concurrentLinkedDeque) {
            System.out.println("顺序输出:" + o);
        }

    }
}

class BlockingQueueDemo {

    public static void main(String[] args) {
        System.out.println("------------ArrayBlockingQueue------------");
        //阻塞,非null元素,安全,先行原则
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(2);
        //阻塞方法 put /  take
        try {
            //可以重复
            arrayBlockingQueue.put("a");
            arrayBlockingQueue.put("a");
//            System.out.println(arrayBlockingQueue.size()+"\r\n下面的阻塞了");
            //多余指定大小 重入锁-可中断+condition
//            arrayBlockingQueue.put("a");//会阻塞,等待队列空闲
            boolean result = arrayBlockingQueue.offer("a", 2, TimeUnit.SECONDS);
            System.out.println("等待2秒,如果没有空间就退出:" + result);
            System.out.println(arrayBlockingQueue.take());
            System.out.println(arrayBlockingQueue.take());
            System.out.println("等待2秒,如果没有元素就退出:" + arrayBlockingQueue.poll(2, TimeUnit.SECONDS));
            //会阻塞,等待队列不为空
//            System.out.println(arrayBlockingQueue.take());
        } catch (InterruptedException e) {
            System.out.println("1");
            e.printStackTrace();
        }

        System.out.println("------------LinkedBlockingQueue------------");
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        try {
            //可以重复
            linkedBlockingQueue.put("a");
            linkedBlockingQueue.put("a");
            System.out.println(linkedBlockingQueue.size() + "\r\n默认大小,可以存整数最大值个元素");
            //多余指定大小 重入锁-可中断+condition
            arrayBlockingQueue.put("a");//
            boolean result = linkedBlockingQueue.offer("a", 2, TimeUnit.SECONDS);
            System.out.println("等待2秒,(默认大小不会空间不足),如果没有空间就退出:" + result);
            System.out.println(linkedBlockingQueue.take());
            System.out.println(linkedBlockingQueue.take());
            System.out.println(linkedBlockingQueue.take());
            //会阻塞
//            System.out.println(linkedBlockingQueue.take());//没有了会阻塞
            System.out.println("等待2秒,如果没有元素就退出:" + linkedBlockingQueue.poll(2, TimeUnit.SECONDS));
            //会阻塞,等待队列不为空
//            System.out.println(linkedBlockingQueue.take());
        } catch (InterruptedException e) {
            System.out.println("1");
            e.printStackTrace();
        }

        System.out.println("上面两者区别,在于数量级,当数量较小时,LinkedBlockingQueue优秀,数量级大时ArrayBlockingQueue\r\n");
        System.out.println("------------SynchronousQueue------------");
        SynchronousQueue synchronousQueue = new SynchronousQueue();

        //同步队列
        ExecutorService service = Executors.newFixedThreadPool(2);//一次只有一个产品执行
//       service.execute(new Thread(new Customer(synchronousQueue)));
//       service.execute(new Thread(new Productor(synchronousQueue)));
        service.shutdown();

        System.out.println("------------LinkedTransferQueue------------");
        LinkedTransferQueue linkedTransferQueue = new LinkedTransferQueue();
//        try {
        System.out.println("trander会阻塞");
//            linkedTransferQueue.transfer("e");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ExecutorService transferService = Executors.newFixedThreadPool(4);//一次只有一个产品执行
//        transferService.execute(new Thread(new TransferCustomer(linkedTransferQueue)));
//        transferService.execute(new Thread(new TransferCustomer(linkedTransferQueue)));
//        transferService.execute(new Thread(new TransferProductor(linkedTransferQueue)));
//        transferService.execute(new Thread(new TransferProductor(linkedTransferQueue)));
        transferService.shutdown();

        System.out.println("------------LinkedBlockingDeque------------");
        LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque();
        try {
            linkedBlockingDeque.put("a");//等价putLast
            linkedBlockingDeque.putLast("b");
            linkedBlockingDeque.putLast("d");
            linkedBlockingDeque.putLast("e");
            linkedBlockingDeque.putFirst("0");
            System.out.println(linkedBlockingDeque.take());//等价takeFirst 0
            System.out.println(linkedBlockingDeque.takeFirst());//等价takeFirst a
            System.out.println(linkedBlockingDeque.takeLast());// e
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("------------PriorityBlockingQueue------------");
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
        //实现比较器
        priorityBlockingQueue.put("a");
        priorityBlockingQueue.put("0");
        priorityBlockingQueue.put("1");
        priorityBlockingQueue.put("b");
        priorityBlockingQueue.put("c");
        try {
            System.out.println("输出有序队列:");
            System.out.println(priorityBlockingQueue.take());
            System.out.println(priorityBlockingQueue.take());
            System.out.println(priorityBlockingQueue.take());
            System.out.println(priorityBlockingQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------------DelayQueue------------");
        DelayQueue delayQueue = new DelayQueue();
        delayQueue.put(new DelayedImpl("5s执行", 5));
        delayQueue.put(new DelayedImpl("4s执行", 4));
        delayQueue.put(new DelayedImpl("3s执行", 3));
        delayQueue.put(new DelayedImpl("2s执行", 2));
        delayQueue.put(new DelayedImpl("1s执行", 1));
        for (; ; ) {
            try {
                //take 阻塞
                DelayedImpl delayed = (DelayedImpl) delayQueue.take();
                //如果要终止,则要结合ExecutorService/Executors
                if (delayed == null) {
                    break;
                } else {
                    System.out.println(delayed.getTaskName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static class TransferProductor implements Runnable {

        LinkedTransferQueue linkedTransferQueue;

        public TransferProductor(LinkedTransferQueue linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
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
            while (true) {
                try {
                    int i = (new Random().nextInt(100));
                    linkedTransferQueue.transfer("消耗产品" + i);
                    System.out.println(
                            "---Transfer 生产产品 " + i + "----" + linkedTransferQueue.hasWaitingConsumer() + " " +
                            linkedTransferQueue.getWaitingConsumerCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class TransferCustomer implements Runnable {

        LinkedTransferQueue linkedTransferQueue;

        public TransferCustomer(LinkedTransferQueue linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
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
            while (true) {
                try {
                    while (true) {
                        System.out.println(linkedTransferQueue.take());
                        System.out.println("--------TransferQueue-----------");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Productor implements Runnable {

        SynchronousQueue synchronousQueue;

        public Productor(SynchronousQueue synchronousQueue) {
            this.synchronousQueue = synchronousQueue;
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
            while (true) {
                try {
                    int i = (new Random().nextInt(100));
                    synchronousQueue.put("消耗产品" + i);
                    System.out.println("---生产产品 " + i + "----");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Customer implements Runnable {

        SynchronousQueue synchronousQueue;

        public Customer(SynchronousQueue synchronousQueue) {
            this.synchronousQueue = synchronousQueue;
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
            while (true) {
                try {
                    while (true) {
                        System.out.println(synchronousQueue.take());
                        System.out.println("-------------------");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DelayedImpl implements Delayed {

        /*基准时间,所有关于时间的计算转化为与此时间偏移量*/
        private final long SECOND_ORIGIN = System.currentTimeMillis() / 1000;

        /*简单标记任务名称,可以使用线程*/
        private String taskName;

        public String getTaskName() {
            return taskName;
        }

        /*任务到期时间*/
        private long taskTime;

        public DelayedImpl(String taskName, long taskTime) {
            this.taskName = taskName;
            //计算偏移量
            this.taskTime = System.currentTimeMillis() / 1000 - SECOND_ORIGIN + taskTime;
        }

        /**
         * Returns the remaining delay associated with this object, in the
         * given time unit.
         *
         * @param unit the time unit
         * @return the remaining delay; zero or negative values indicate
         * that the delay has already elapsed
         */
        @Override
        public long getDelay(TimeUnit unit) {
            //比较任务时间偏移量距离当前时间偏移量的大小,<=0说明可以执行了
            return unit.convert(taskTime - (System.currentTimeMillis() / 1000 - SECOND_ORIGIN), TimeUnit.SECONDS);
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         * <p>
         * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
         * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
         * <tt>y.compareTo(x)</tt> throws an exception.)
         * <p>
         * <p>The implementor must also ensure that the relation is transitive:
         * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
         * <tt>x.compareTo(z)&gt;0</tt>.
         * <p>
         * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
         * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
         * all <tt>z</tt>.
         * <p>
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
         * class that implements the <tt>Comparable</tt> interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         * <p>
         * <p>In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         */
        @Override
        public int compareTo(Delayed o) {
            //用于内部使用PriorityQueue,注意不是PriorityBlockingQueue
            if (o == this) {
                return 0;
            } else {
                DelayedImpl delayed = (DelayedImpl) o;
                //比较任务偏移量
                long diff = this.getDelay(TimeUnit.SECONDS) - delayed.getDelay(TimeUnit.SECONDS);
                if (diff < 0) {
                    return -1;
                } else if (diff > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

        }
    }
}
