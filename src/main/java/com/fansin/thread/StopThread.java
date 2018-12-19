package com.fansin.thread;

/**
 * Created by zhaofeng on 17-4-7.
 */
public class StopThread {

    public static User user = new User("1", 1);//添加volatile无效

    public static class User {

        private String name;
        private int    id;

        public User() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public User(String name, int age) {
            this.name = name;
            this.id = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "[name=" + name + " id=" + id + "]";
        }
    }

    public static class WriteUser implements Runnable {

        volatile boolean isStop = false;

        public void stopMe() {
            isStop = true;
        }

        @Override
        public void run() {
            while (true) {
                if (isStop) {
                    System.out.println("终止线程!");
                    break;
                }
                synchronized (user) {//加同步锁
                    System.out.println("...");
                    int v = (int) (System.currentTimeMillis() / 1000);
                    user.setId(v);//期望id与name相同
                    try {
                        Thread.sleep(10000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setName(String.valueOf(v));
                }
                Thread.yield();
            }

        }

    }

    public static class ReadUser implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (user) {//加同步锁
//                        System.out.println("222的位数:"+"222".charAt(0));
                    if (user.getId() != Integer.parseInt(user.getName())) {
                        System.out.println(user.toString());
                    } else {
//                        System.out.println("ok");
                    }

                }
                Thread.yield();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        new Thread(new ReadUser()).start();
        while (true) {
            WriteUser writer = new WriteUser();
            Thread t = new Thread(writer);
            t.start();
            Thread.sleep(150);
//            t.stop();
            writer.stopMe();

        }

    }

}
