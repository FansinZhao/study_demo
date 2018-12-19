package com.fansin.reflect.dynamic;

/**
 * Created by zhaofeng on 17-4-3.
 */
public class IUserImpl implements IUser {

    @Override
    public String getName(String name) {
        System.out.println("name " + name);
        return name;
    }

    @Override
    public int getAge(int age) {
        System.out.println("age " + age);
        return age;
    }
}
