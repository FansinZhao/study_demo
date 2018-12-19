package com.fansin.designpattern;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhaofeng on 17-5-13.
 */
public class VisitorDemo {

    /*
    访问者模式:
    将数据结构和操作分离,在不改变数据结构的情况下,为其添加操作
    应用场景:
    1 多种数据结构的集合
    2 每种数据结构的操作不同
    业务场景:
    1 对老业务系统数据分析.
    2 业务系统有三种基本数据结构,银联数据,贷款行数据,机构数据,通过访问者模式来实现笔数/金额统计

    抽象visitor 定义对每个元素类型的处理接口
    多个具体visitor 实现每个元素类型的具体处理,比如日期,金额
    抽象元素Element(组件元素) 基本元素类型
    叶子元素
    容器组件元素

     */
    public static void main(String[] args) {
        CreditPartner creditEntry = new CreditPartner("t0授信平台");
        creditEntry.addEntry(new UnionpayEntry(100l, 2));
        creditEntry.addEntry(new UnionpayEntry(300l, 1));
        creditEntry.addEntry(new AcquirerEntry(520l, 1));
        creditEntry.addEntry(new LoadBankEntry(600l, 3));

        NumberStatistics numberStatistics = new NumberStatistics();
        creditEntry.accept(numberStatistics);
        AmtStatistics amtStatistics = new AmtStatistics();
        creditEntry.accept(amtStatistics);

        System.out.println("UNIONPAY 所有笔数:" + numberStatistics.getTotal(NumberStatistics.UNIONPAY));
        System.out.println("ACQUIRER 所有笔数:" + numberStatistics.getTotal(NumberStatistics.ACQUIRER));
        System.out.println("LOANBANK 所有笔数:" + numberStatistics.getTotal(NumberStatistics.LOANBANK));

        System.out.println("UNIONPAY 所有金额:" + amtStatistics.getTotal(NumberStatistics.UNIONPAY));
        System.out.println("ACQUIRER 所有金额:" + amtStatistics.getTotal(NumberStatistics.ACQUIRER));
        System.out.println("LOANBANK 所有金额:" + amtStatistics.getTotal(NumberStatistics.LOANBANK));

    }
}

//visitor
abstract class Statistics {

    abstract void visit(UnionpayEntry entry);

    abstract void visit(AcquirerEntry entry);

    abstract void visit(LoadBankEntry entry);
//    abstract void visit(BaseEntry entry);
}

//统计交易笔数
class NumberStatistics extends Statistics {

    private       ConcurrentHashMap<String, Integer> buf      = new ConcurrentHashMap<>(10);
    public static String                             UNIONPAY = "UNIONPAY";
    public static String                             ACQUIRER = "ACQUIRER";
    public static String                             LOANBANK = "LOANBANK";

    public NumberStatistics() {
        //init
        buf.put(UNIONPAY, 0);
        buf.put(ACQUIRER, 0);
        buf.put(LOANBANK, 0);
    }

    @Override
    void visit(UnionpayEntry entry) {
        buf.replace(UNIONPAY, buf.get(UNIONPAY), buf.get(UNIONPAY) + entry.getTrans());
    }

    @Override
    void visit(AcquirerEntry entry) {
        buf.replace(ACQUIRER, buf.get(ACQUIRER), buf.get(ACQUIRER) + entry.getTrans());
    }

    @Override
    void visit(LoadBankEntry entry) {
        buf.replace(LOANBANK, buf.get(LOANBANK), buf.get(LOANBANK) + entry.getTrans());
    }

    public int getTotal(String type) {
        return buf.getOrDefault(type, 0);
    }
}

//统计交易笔数
class AmtStatistics extends Statistics {

    private       ConcurrentHashMap<String, Long> buf      = new ConcurrentHashMap<>(10);
    public static String                          UNIONPAY = "UNIONPAY";
    public static String                          ACQUIRER = "ACQUIRER";
    public static String                          LOANBANK = "LOANBANK";

    public AmtStatistics() {
        //init
        buf.put(UNIONPAY, 0L);
        buf.put(ACQUIRER, 0L);
        buf.put(LOANBANK, 0L);
    }

    @Override
    void visit(UnionpayEntry entry) {
        buf.replace(UNIONPAY, buf.get(UNIONPAY), buf.get(UNIONPAY) + entry.getAmt());
    }

    @Override
    void visit(AcquirerEntry entry) {
        buf.replace(ACQUIRER, buf.get(ACQUIRER), buf.get(ACQUIRER) + entry.getAmt());
    }

    @Override
    void visit(LoadBankEntry entry) {
        buf.replace(LOANBANK, buf.get(LOANBANK), buf.get(LOANBANK) + entry.getAmt());
    }

    public Long getTotal(String type) {
        return buf.getOrDefault(type, 0l);
    }
}

//简化数据结构
@Data
abstract class BaseEntry<T> {

    protected Long    amt;
    protected Integer trans;

    public BaseEntry() {
    }

    public BaseEntry(long amt, int trans) {
        this.amt = amt;
        this.trans = trans;
    }

    abstract void accept(Statistics statistics);

    public void addEntry(BaseEntry<T> entry) {
    }

    ;
}

class CreditPartner extends BaseEntry {

    private CopyOnWriteArrayList<BaseEntry> list = new CopyOnWriteArrayList<>();

    private String name;

    public CreditPartner(String name) {

        this.name = name;
    }

    @Override
    public void addEntry(BaseEntry entry) {
        list.add(entry);
    }

    @Override
    void accept(Statistics statistics) {
        for (BaseEntry entry : list) {
            entry.accept(statistics);
        }

    }
}

@Data
class LoadBankEntry extends BaseEntry {

    public LoadBankEntry(long amt, int trans) {
        super(amt, trans);
    }

    @Override
    void accept(Statistics statistics) {
        statistics.visit(this);//双重分派

    }
}

@Data
class AcquirerEntry extends BaseEntry {

    public AcquirerEntry(long amt, int trans) {
        super(amt, trans);
    }

    @Override
    void accept(Statistics statistics) {
        statistics.visit(this);//双重分派

    }
}

@Data
class UnionpayEntry extends BaseEntry {

    public UnionpayEntry(long amt, int trans) {
        super(amt, trans);
    }

    @Override
    void accept(Statistics statistics) {
        statistics.visit(this);//双重分派

    }
}
