package com.fansin.designpattern;

/**
 * Created by zhaofeng on 17-5-14.
 */
public class MediatorDemo {

    /*
    调停者/中介者:
    所有的对象均与调停者交互,从而形成星型结构.
    应用场景:
    系统内部多个对象,并且多个对象具有交互性.
    jdk中Executor就是典型应用,所有的任务都集中在executor中.
    业务场景:
    机构--提现通知-->贷款行---确认放款-->银联--发送代付请求-->银联机构
    a 贷款行修改都需要通知银联
    b 机构修改通知贷款行
     */

    public static void main(String[] args) {
        WithdrawExecutor executor = new WithdrawExecutor();
        AcquirePlatform acquirePlatform = new AcquirePlatform(executor);
        LoanBankPlatform loanBankPlatform = new LoanBankPlatform(executor);
        UnionPayPlatform unionPayPlatform = new UnionPayPlatform(executor);
        executor.setAcquirePlatform(acquirePlatform);
        executor.setLoanBankPlatform(loanBankPlatform);
        executor.setUnionPayPlatform(unionPayPlatform);

        acquirePlatform.callPlatform();

    }
}

interface Executor {

    void execute(Platform platform);

}

class WithdrawExecutor implements Executor {

    private UnionPayPlatform unionPayPlatform;
    private AcquirePlatform  acquirePlatform;
    private LoanBankPlatform loanBankPlatform;

    public void setUnionPayPlatform(UnionPayPlatform unionPayPlatform) {
        this.unionPayPlatform = unionPayPlatform;
    }

    public void setAcquirePlatform(AcquirePlatform acquirePlatform) {
        this.acquirePlatform = acquirePlatform;
    }

    public void setLoanBankPlatform(LoanBankPlatform loanBankPlatform) {
        this.loanBankPlatform = loanBankPlatform;
    }

    @Override
    public void execute(Platform platform) {
        if (platform instanceof AcquirePlatform) {
            loanBankPlatform.callPlatform();
        } else if (platform instanceof LoanBankPlatform) {
            unionPayPlatform.callPlatform();
        } else {
            System.out.println("代付请求发往......银联机构");
        }
    }
}

abstract class Platform {

    private Executor executor;

    public Platform(Executor executor) {
        this.executor = executor;
    }

    public Executor getExecutor() {
        return executor;
    }

    abstract void callPlatform();
}

class UnionPayPlatform extends Platform {

    public UnionPayPlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("银联发往代付机构....");
        getExecutor().execute(this);
    }
}

class AcquirePlatform extends Platform {

    public AcquirePlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("机构提现通知....");
        getExecutor().execute(this);
    }
}

class LoanBankPlatform extends Platform {

    public LoanBankPlatform(Executor executor) {
        super(executor);
    }

    @Override
    void callPlatform() {
        System.out.println("贷款行确认放款....");
        getExecutor().execute(this);
    }
}

