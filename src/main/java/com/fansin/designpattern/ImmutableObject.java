package com.fansin.designpattern;

/**
 * Created by zhaofeng on 17-5-9.
 */
//1 类final 不可继承
public final class ImmutableObject {

    //2 成员变量私有,不能被外部访问到
    private       String       member;
    //3 不提供setter,防止外部程序修改到内部信息
    //4 可变变量final,只能初始化一次
    private final StringBuffer stringBuffer;

    //5 初始化时,深度复制可变变量
    public ImmutableObject(StringBuffer sb) {
        this.stringBuffer = new StringBuffer(sb.toString());
    }

    //6 getter返回对象的clone
    public StringBuffer getStringBuffer() {
        return new StringBuffer(stringBuffer.toString());//出现变量逃逸现象
    }
}
