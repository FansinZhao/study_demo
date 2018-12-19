package com.fansin.base;

import java.io.Serializable;

/**
 * Created by zhaofeng on 17-4-11.
 */
public class Base implements Serializable {

    public static void main(String[] args) {
        int i = 170;
        System.out.println("170的二进制表示:" + Integer.toBinaryString(i));
        System.out.println("170补码中1的个数:" + Integer.bitCount(i));
        System.out.println("170最高位(最左边)一位的值" + Integer.highestOneBit(i));
        System.out.println("170最低位(最右边)一位的值" + Integer.lowestOneBit(i));
        System.out.println("170补码左边第一个1左边的0数量" + Integer.numberOfLeadingZeros(i));
        System.out.println("170补码右边第一个1右边的0数量" + Integer.numberOfTrailingZeros(i));

        double value = Math.cos(Math.toRadians(30));
        System.out.println("余弦值30 " + value);
        try {
            Object 中文变量 = new Object();
            System.out.println(中文变量);
            ((Object) null).toString();//运行时报空指针异常
        } catch (NullPointerException e) {
            System.out.println("空指针异常");
        }

    }

}
