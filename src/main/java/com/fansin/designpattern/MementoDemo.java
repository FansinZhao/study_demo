package com.fansin.designpattern;

import lombok.Data;

/**
 * Created by zhaofeng on 17-5-16.
 */
public class MementoDemo {
    /*
    备忘模式:
    在不破坏对象内部封装性的情况下,将对象的内部状态保存在外部.并在以后可能需要的时候对其恢复.
    应用场景:
    1 对象备份恢复 入归档
    2 持久化保存 序列化
    业务场景:
    涉及网络,就会有重发,使用备忘录模式实现重发

     */

    public static void main(String[] args) {
        MessageOriginator messageOriginator = new MessageOriginator("fansin", "123456789", 10000l);
        MessageCareTaker messageCareTaker = new MessageCareTaker();
        messageCareTaker.setMemento(messageOriginator.createMemento());
        System.out.println("经过一次请求,多个参数发生");
        messageOriginator.setAmt(0L);
        //恢复备份
        messageOriginator.restoreMemento(messageCareTaker.getMemento());
        System.out.println("恢复后");
        System.out.println(messageOriginator.getAmt());
    }
}

abstract class Originator implements Cloneable {

}

//组织者 需要备份的对象
@Data
class MessageOriginator extends Originator {

    private String countName;
    private String countNumber;
    private Long   amt;

    public MessageOriginator(String countName, String countNumber, Long amt) {
        this.countName = countName;
        this.countNumber = countNumber;
        this.amt = amt;
    }

    public MessageOriginator() {
    }

    public Memento createMemento() {
        return new MessageMemento(this);
    }

    public void restoreMemento(Memento memento) {
        MessageOriginator messageOriginator = (MessageOriginator) memento.getOriginator();
        setAmt(messageOriginator.getAmt());
        setCountName(messageOriginator.getCountName());
        setCountNumber(messageOriginator.getCountNumber());
    }

    @Override
    protected Originator clone() throws CloneNotSupportedException {

        //TODO 本类中没有集合类对象,不需要深度复制
        return (Originator) super.clone();
    }
}

interface Memento {

    void setOriginator(Originator originator);

    Originator getOriginator();
}

class MessageMemento implements Memento {

    private Originator originator;

    public MessageMemento(MessageOriginator originator) {
        try {
            this.originator = (MessageOriginator) originator.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOriginator(Originator originator) {
        this.originator = originator;
    }

    @Override
    public Originator getOriginator() {
        return originator;
    }
}

class MessageCareTaker {

    private Memento memento;

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}

