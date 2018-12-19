package com.fansin.designpattern;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhaofeng on 17-5-15.
 */
public class CommandDemo {
    /*
    命令模式:
    将请求封装成一个对象,请求队列或记录日志,并提供撤销功能.
    应用场景:
    1 多级回退
    2 原子事务
    3 状态条
    4 导航
    5 ThreadPool
    业务场景:
    模仿一下Thread的使用

     */

    public static void main(String[] args) {
        System.out.println("------使用Executor-------");
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(new java.lang.Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " runnable:执行业务处理1");
            }
        });
        service.execute(new java.lang.Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " runnable:执行业务处理2");
            }
        });
        service.shutdown();

        CommandClient commandClient = new CommandClient();
        Command command1 = new CommandThread(new CommandReceiver());
        Command command2 = new CommandThread(new CommandReceiver());
        commandClient.execute(command1);
        commandClient.execute(command2);
    }
}

//command接口 interface Runnable

//Runnable 和 Thread 简单合体
abstract class Command {

    abstract void run();

    public void start() {

        try {
            Thread.sleep(Math.round(Math.random() * 3000));
            run();//Thread的run是根据cpu空闲时间觉定的,这里使用random模拟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//command具体实现 Thread implements Runnable
class CommandThread extends Command {

    private CommandReceiver receiver;

    public CommandThread(CommandReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        receiver.doSomething();
    }
}

//Receiver Thread中的任务
class CommandReceiver {

    public void doSomething() {
        System.out.println(Thread.currentThread().getName() + ":处理一些业务");
    }

}

//invoker 线程池,调用execute()/submit()
class CommandClient {

    private Set<Command> set = Collections.synchronizedSet(new HashSet<>());

    public void execute(Command command) {
        set.add(command);
        command.start();
    }

    public void cancel(Command command) {
        set.remove(command);
    }

}
