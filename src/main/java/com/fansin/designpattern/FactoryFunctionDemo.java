package com.fansin.designpattern;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Created by zhaofeng on 17-5-5.
 */
public class FactoryFunctionDemo {

    /***
     * 工厂方法模式:工厂可以有多个,真正生成实例时,由具体工厂类实现,从而实现对象延迟到工厂实例
     *
     * 项目应用:
     * 实时授信统计服务需要为三个角色提供统计结果
     * 产品父类:TransStatistics:共同参数:今日交易笔数,今日交易金额,昨日交易笔数,昨日交易金额
     * 产品子类:UnionPayTransStatistics:银联用户:今日贷款行数,今日机构数
     * 产品子类:LoanBankTransStatistics:贷款行用户:今日机构数
     * 产品子类:AcquirerTransStatistics:机构用户:今日提现用户数
     *
     * @param args
     */

    public static void main(String[] args) {

        TransStatisticsFactory unionPayFactory = new UnionPayTransStatisticsFactory();
        TransStatisticsFactory loanBankPayFactory = new LoanBankTransStatisticsFactory();
        TransStatisticsFactory acquirerFactory = new AcquirerTransStatisticsFactory();
        System.out.println("银联工厂类:" + JSONObject.toJSONString(unionPayFactory.getTransStatistics(null)));
        System.out.println("贷款行工厂类:" + JSONObject.toJSONString(loanBankPayFactory.getTransStatistics(null)));
        System.out.println("机构工厂类:" + JSONObject.toJSONString(acquirerFactory.getTransStatistics(null)));

    }
}

class AcquirerTransStatisticsFactory extends TransStatisticsFactory {

    @Override
    public void preProcess() {
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成贷款行交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        return new AcquirerTransStatistics(33, 33, 33, 33, 33);
    }

}

class LoanBankTransStatisticsFactory extends TransStatisticsFactory {

    @Override
    public void preProcess() {
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成贷款行交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        return new LoanBankTransStatistics(22, 22, 22, 22, 22);
    }

}

class UnionPayTransStatisticsFactory extends TransStatisticsFactory {

    @Override
    public void preProcess() {
        //TODO 业务处理,查询数据库等
        System.out.println("[业务]:生成银联交易统计信息!");
    }

    @Override
    public TransStatistics createTransStatistics(String type) {
//        preProcess();
        if (type == null) {
            return new UnionPayTransStatistics(1, 1, 1, 1, 1, 1);
        }
        switch (type) {
            case "attendance":
                return new UnionPayAttendanceTransStatistics(101, 101, 101, 101, 101, 101);
            case "clerk":
                return new UnionPayClerkTransStatistics(102, 102, 102, 102, 102, 102);
            case "leader":
                return new UnionPayLeaderTransStatistics(103, 103, 103, 103, 103, 103);

            default:
                return new UnionPayTransStatistics(1, 1, 1, 1, 1, 1);
        }
    }

}

abstract class TransStatisticsFactory {

    public abstract void preProcess();

    /*统一创建对象方法*/
    TransStatistics getTransStatistics(String type) {
        preProcess();
        TransStatistics transStatistics = createTransStatistics(type);
        transStatistics = postProcess(transStatistics);
        return transStatistics;
    }

    public TransStatistics postProcess(TransStatistics transStatistics) {
        System.out.println("结果验证!");
        if (transStatistics == null) {
            return new TransStatistics(0L, 0L, 0L, 0L);
        } else {
            return transStatistics;
        }
    }

    public abstract TransStatistics createTransStatistics(String type);
}

class AcquirerTransStatistics extends TransStatistics {

    private long merchantNum;

    public AcquirerTransStatistics(long merchantNum) {
        this.merchantNum = merchantNum;
    }

    public AcquirerTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt,
                                   long merchantNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.merchantNum = merchantNum;
    }

    public void setMerchantNum(long merchantNum) {
        this.merchantNum = merchantNum;
    }
}

class LoanBankTransStatistics extends TransStatistics {

    private long acquirerNum;

    public LoanBankTransStatistics(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }

    public LoanBankTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt,
                                   long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.acquirerNum = acquirerNum;
    }

    public long getAcquirerNum() {
        return acquirerNum;
    }

    public void setAcquirerNum(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }
}

/**/
class UnionPayTransStatistics extends TransStatistics {

    private long loanBankNum;
    private long acquirerNum;

    public UnionPayTransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt,
                                   long loanBankNum, long acquirerNum) {
        super(lastTransNum, lastTransAmt, nowTransNum, nowTransAmt);
        this.loanBankNum = loanBankNum;
        this.acquirerNum = acquirerNum;
    }

    public long getLoanBankNum() {
        return loanBankNum;
    }

    public void setLoanBankNum(long loanBankNum) {
        this.loanBankNum = loanBankNum;
    }

    public long getAcquirerNum() {
        return acquirerNum;
    }

    public void setAcquirerNum(long acquirerNum) {
        this.acquirerNum = acquirerNum;
    }
}

/* 接口或类 */
@Data
class TransStatistics {

    /*昨日交易笔数*/
    private long lastTransNum;
    /*昨日交易金额 单位:分*/
    private long lastTransAmt;
    /*今日交易笔数*/
    private long nowTransNum;
    /*今日交易金额 单位:分*/
    private long nowTransAmt;

    public TransStatistics() {
    }

    public TransStatistics(long lastTransNum, long lastTransAmt, long nowTransNum, long nowTransAmt) {
        this.lastTransNum = lastTransNum;
        this.lastTransAmt = lastTransAmt;
        this.nowTransNum = nowTransNum;
        this.nowTransAmt = nowTransAmt;
    }
//
//    public long getLastTransNum() {
//        return lastTransNum;
//    }
//
//    public void setLastTransNum(long lastTransNum) {
//        this.lastTransNum = lastTransNum;
//    }
//
//    public long getLastTransAmt() {
//        return lastTransAmt;
//    }
//
//    public void setLastTransAmt(long lastTransAmt) {
//        this.lastTransAmt = lastTransAmt;
//    }
//
//    public long getNowTransNum() {
//        return nowTransNum;
//    }
//
//    public void setNowTransNum(long nowTransNum) {
//        this.nowTransNum = nowTransNum;
//    }
//
//    public long getNowTransAmt() {
//        return nowTransAmt;
//    }
//
//    public void setNowTransAmt(long nowTransAmt) {
//        this.nowTransAmt = nowTransAmt;
//    }

}

