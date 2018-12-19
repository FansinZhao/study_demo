package com.fansin.serialize;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class ProxyPerson implements Serializable {

    private static final long serialVersionUID = -6886613279037637914L;

    private String data;

    public ProxyPerson() {
    }

    public ProxyPerson(TestPerson person) {

        data = person.toString();

    }

    private Object readResolve() throws ObjectStreamException {

        System.out.println("通过代理处理对象!年龄+10");
        //TODO 这里可以根据数据类型进行区分,生成不同的对象
        String[] arr = data.split(" ");
        TestPerson person = new TestPerson(arr[0], Integer.valueOf(arr[2]) + 10);
        return person;
    }

}
