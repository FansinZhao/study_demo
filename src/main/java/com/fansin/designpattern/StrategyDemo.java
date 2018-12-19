package com.fansin.designpattern;

/**
 * Created by zhaofeng on 17-5-14.
 */
public class StrategyDemo {

    /*
    策略模式:
    将对象自身与算法分离.
    应用场景:
    1 拥有不同行为的一群类
    2 使用算法变种
    3 else if 比较多
    业务场景:
    测试代付三种场景,定时代付/平台自助提现/平台下提现
     */

    public static void main(String[] args) {
        Payment payment = new Payment(new FixedTimeStrategy());
        payment.pay();
        payment = new Payment(new WithdrawStrategy());
        payment.pay();
        payment = new Payment(new AcquirerStrategy());
        payment.pay();
    }
}

interface Strategy {

    void from();
}

class FixedTimeStrategy implements Strategy {

    @Override
    public void from() {
        System.out.println("定时代付....");
    }
}

class WithdrawStrategy implements Strategy {

    @Override
    public void from() {
        System.out.println("平台自助代付....");
    }
}

class AcquirerStrategy implements Strategy {

    @Override
    public void from() {
        System.out.println("机构独立代付....");
    }
}

class Payment {

    private Strategy strategy;

    public Payment(Strategy strategy) {
        this.strategy = strategy;
    }

    public void pay() {
        strategy.from();
        System.out.println("发送代付请求....");
    }

}
