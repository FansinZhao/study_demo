package com.fansin.escapeAnalysis;

import cn.hutool.core.lang.Assert;

/**
 * Created by zhaofeng on 17-5-8.
 */
public class EscapeAnalysisDemo {

    /*
    *未开始逃逸分析
    -server
    -Xmx5m
    -Xms5m
    -XX:+PrintGC
    *开启逃逸分析-XX:+PrintEscapeAnalysis/-XX:+PrintEliminateAllocations只能在debug的jvm使用,借助GC分析
    -server
    -Xmx5m
    -Xms5m
    -XX:+PrintGC
    -XX:+DoEscapeAnalysis
    -XX:+EliminateAllocations
    *
    * */
    public static void main(String[] args) {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            alloc();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);
    }

    public static void alloc() {
        Keeper keeper = new Keeper();
        keeper.全局变量赋值();
//        keeper.返回值();
        keeper.引用传递();
    }
}

class Keeper {

    private static Escaper escaper;

    //1全局变量赋值
    public void 全局变量赋值() {
        escaper = new Escaper();
    }

    //2返回值
    public Escaper 返回值() {
        return new Escaper();
    }

    //3引用传递
    public void 引用传递() {
        返回值().print(this);
    }
}

class Escaper {

    public void print(Keeper keeper) {
        Assert.notBlank(keeper.getClass().getSimpleName());
    }

}
