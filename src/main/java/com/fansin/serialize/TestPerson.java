package com.fansin.serialize;

import java.io.*;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class TestPerson implements Serializable, ObjectInputValidation {

    private static final long serialVersionUID = -1L;

//    private static final long serialVersionUID = 8070621334146673048L;

    private String sex;
    private String name;
    private int    age;

    public TestPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + "  " + age + " " + sex;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        age = age << 2;
        System.out.println("加密 " + age);
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream oin) throws IOException, ClassNotFoundException {
        oin.defaultReadObject();
        age = age >> 2;
        System.out.println("解密 " + age);
    }

    /**
     * Validates the object.
     *
     * @throws InvalidObjectException If the object cannot validate itself.
     */
    @Override
    public void validateObject() throws InvalidObjectException {
        //可以做一些数据合法性验证
        if (name.equals("fansin")) {
            System.out.println("验证通过!");
        } else {
            throw new InvalidObjectException("对象格式有误!");
        }
    }
}
