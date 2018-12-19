package com.fansin.designpattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhaofeng on 17-5-13.
 */
public class ObserverDemo {

    /*
    观察者模式:
    一对多模式,主题方发生变化,通知所有订阅方,订阅方可以进行相应操作
    适用场景:一个对象的变化会引起其他对象操作.
    业务场景:
    当代付返回成功结果时,需要通知提现服务和监控服务.

     */

    public static void main(String[] args) {

        PayStatusSubject payStatusSubject = new SuccessSubject();
        payStatusSubject.addObserver(new StatisticObserver());
        payStatusSubject.addObserver(new WithdrawObserver());
        payStatusSubject.noticeObservers();

    }
}

abstract class PayStatusSubject {

    private List<Observer> observerList = Collections.synchronizedList(new ArrayList<Observer>());

    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    public void noticeObservers() {
        System.out.println("已注册观察者数量:" + observerList.size());
        for (int i = 0; i < observerList.size(); i++) {
            Observer observer = observerList.get(i);
            observer.update(notice());
        }
    }

    abstract String notice();
}

class SuccessSubject extends PayStatusSubject {

    @Override
    String notice() {
        return "支付成功";
    }
}

interface Observer {

    void update(String msg);
}

class StatisticObserver implements Observer {

    @Override
    public void update(String msg) {
        System.out.println("StatisticObserver.update 收到通知:" + msg);
        //TODO 业务处理
        System.out.println("[交易统计服务]更新交易统计信息......");
    }
}

class WithdrawObserver implements Observer {

    @Override
    public void update(String msg) {
        System.out.println("WithdrawObserver.update 收到通知:" + msg);
        //TODO 业务处理
        System.out.println("[提现服务]完成提现入账交易.....");
    }
}
