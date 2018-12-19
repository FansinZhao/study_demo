package com.fansin.designpattern;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;

/**
 * Created by zhaofeng on 17-5-15.
 */
public class StateDemo {
    /*
    状态模式:
    一个对象内部有多个状态,状态之间可以相互转换,从而影响行为.
    应用场景:
    对象有多个状态,并且状态之间可能有关联.
    业务场景:
    举个线程的周期
    new,runnable,running,blocked,waiting-blocked,dead


     */

    public static void main(String[] args) {
        //多次运行,可以看到不同的结果
        for (int i = 0; i < 5; i++) {
            System.out.println("");
            StateContent content1 = new StateContent(new New());
            content1.request();

        }
    }

}

@Data
class StateContent {

    private ThreadState state;

    public StateContent(ThreadState state) {
        this.state = state;
    }

    public void request() {
        this.state.handle(this);
    }
}

interface ThreadState {

    void handle(StateContent content);
}

class New implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.print("new新建--->");
        content.setState(new RunnableState());
        content.request();
    }
}

//影响Runnable接口使用
class RunnableState implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.print("runnable就绪状态---->OS获取cpu--->");
        content.setState(new Running());
        content.request();
    }
}

class Running implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.print("running运行状态---->");
        switch (RandomUtil.randomInt(0, 5)) {
            case 0:
                System.out.print("Thread.yield()让出cpu--->");
                content.setState(new RunnableState());
                content.request();
                break;
            case 1:
                System.out.print("run()或者main()结束--->");
                content.setState(new Dead());
                content.request();
                break;
            case 2:
                System.out.print("Thread.sleep()或者t2.join()或者IO--->");
                content.setState(new Blocked());
                content.request();
                break;
            case 3:
                System.out.print("obj.wait()-->");
                content.setState(new WaitBlocked());
                content.request();
                break;
            case 4:
                System.out.print("LockSupport.park()/lock.lock()/synchronized--->");
                content.setState(new LockPool());
                content.request();
                break;
            case 5:
                System.out.print("(废弃)Thread.suspend()--->");
                content.setState(new Suspend());
                content.request();
                break;
            default:
                break;
        }
    }
}

class Blocked implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.println("Thread.sleep()结束或者t2线程完成或者IO读取完毕--->");
        content.setState(new RunnableState());
        content.request();
    }
}

class WaitBlocked implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.print("obj.notify()/obj.notifyAll()/Thread.interrupt()--->");
        content.setState(new LockPool());
        content.request();
    }
}

class Dead implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.println("Dead线程结束");
    }
}

class Suspend implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.println("Thread.resume()--->");
        content.setState(new RunnableState());
        content.request();
    }
}

class LockPool implements ThreadState {

    @Override
    public void handle(StateContent content) {
        System.out.println("释放监视器--->");
        content.setState(new RunnableState());
        content.request();
    }
}
