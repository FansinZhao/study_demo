package com.fansin.designpattern;

import lombok.Data;

/**
 * Created by zhaofeng on 17-5-16.
 */
public class ResponsibilityDemo {
    /*
    责任链模式:
    很多对象由一个对象对其下级对象的连接而连接成一个链.当链上有个请求时,会顺着链向下查找对象处理.
    发送请求的对象不知道处理对象.处理过程可以动态设置,很好的实现了组织和分配责任.
    应用场景:
    1 多个请求可以被多个类处理.
    2 动态的更换链上的处理类.
    异常捕获是常见的责任链

    业务场景:
    发起一笔交易,当交易选择付款时,有多个选择,可以选择深银联,总银联,广银联,迅联等等
    通过为每个银联处理类定义一个规则,当请求到达时,各个银联根据条件选择是否为其服务.
     */

    public static void main(String[] args) {
        //深银联-->广银联-->迅联
        Handler handler = new ShenZhenHandler();
        handler.setNextHandler(new GuangZhouHandler());
        CustomRequest customRequest = new CustomRequest();
        customRequest.setAmt(999);
        customRequest.setSite("sz");
        customRequest.setType("01");
        customRequest.setHandler(handler);
        System.out.println("-------正常消费--------");
        customRequest.process();
        customRequest.setSite("usa");
        System.out.println("------------消费失败----------");
        customRequest.process();
        handler.getNextHandler().setNextHandler(new XunLianHandler());
        customRequest.setHandler(handler);
        System.out.println("------------动态添加handle,处理成功----------");
        customRequest.process();
    }
}

//将交易抽象化
@Data
abstract class TranRequest {

    protected long   amt;
    protected String site;
    protected String type;

    private Handler handler;

    //模板方法
    void process() {
        if (amt == 0) {
            System.out.println("金额为空");
        }

        while (handler != null && !handler.handle(this)) {
            handler = handler.nextHandler();
        }

        if (handler == null) {
            failRequest();//处理失败
        }

    }

    abstract void failRequest();
}

class CustomRequest extends TranRequest {

    @Override
    void failRequest() {
        System.out.println("普通消费处理失败,发起冲正....");
    }
}

abstract class Handler {

    private Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public Handler getNextHandler() {
        return nextHandler;
    }

    public Handler nextHandler() {
        return nextHandler;
    }

    abstract boolean handle(TranRequest request);
}

class ShenZhenHandler extends Handler {

    @Override
    public boolean handle(TranRequest request) {

        if (request.getAmt() < 10000 && request.getSite().equalsIgnoreCase("sz")) {
            System.out.println("发往深银联进行消费....成功");
            return true;
        }

        System.out.println("深银联处理不了!金额>1000或不是sz");
        return false;
    }
}

class GuangZhouHandler extends Handler {

    @Override
    public boolean handle(TranRequest request) {

        if (request.getAmt() < 2000 && request.getSite().equalsIgnoreCase("gz")) {
            System.out.println("发往广银联进行消费....成功");
            return true;
        }
        System.out.println("广银联处理不了!金额>2000或不是gz");
        return false;
    }
}

class XunLianHandler extends Handler {

    @Override
    public boolean handle(TranRequest request) {

        if (request.getAmt() < 50000 && request.getSite().equalsIgnoreCase("usa")) {
            System.out.println("发往迅联进行消费....成功");
            return true;
        }
        System.out.println("迅联处理不了!金额>5000或不是usa");
        return false;
    }
}


