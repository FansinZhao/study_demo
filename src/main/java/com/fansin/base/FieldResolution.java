package com.fansin.base;

/**
 * Created by zhaofeng on 17-4-12.
 */
public class FieldResolution {

    interface Interface0 {

        public final static int A = 0;//默认描述
    }

    interface Interface1 extends Interface0 {

        int A = 1;
    }

    interface Interface2 {

        int A = 2;
    }

    static class Parent implements Interface1 {

        public static int A = 3;
    }

    static class Son extends Parent implements Interface2 {

        public static int A = 4;
    }

    public static void main(String[] args) {
        System.out.println(Interface0.class);
        System.out.println(Son.A);
    }

}
