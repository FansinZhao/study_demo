package com.fansin.designpattern;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by zhaofeng on 17-5-15.
 */
public class ExpressionDemo {

    /*
     *解释器模式
     * 根据给定的语法描述,可以对语言进行解析.
     * 应用场景:
     * 1 重复发生的问题
     * 2 简单的语法环境
     * 业务场景:
     * 中文转换为罗马数字
     * 例如 一千二百零三转为1203
     *
     *
     */
    public static void main(String[] args) {
        //TODO 未完成
    }
}

/**
 * 划分单位
 * 终结字符:千位,百位,十位,各位
 * 非终结字符:万位
 * <p>
 * 如果遇到数字压栈,遇到单位取出栈中数据,进行计算并入栈.读取完毕,全部出栈.
 * 特例十,当栈顶为空,则代表一十.
 */

@Data
class ExpContext {

    private String content;
    private int    value;
}

//通用模板
abstract class Expression {

    protected static Map<String, Integer> numbers = new HashMap<String, Integer>();

    protected static Deque<Long> stack = new LinkedList<>();

    protected static String content;

    static {
        numbers.put("零", 0);
        numbers.put("一", 1);
        numbers.put("二", 2);
        numbers.put("三", 3);
        numbers.put("四", 4);
        numbers.put("五", 5);
        numbers.put("六", 6);
        numbers.put("七", 7);
        numbers.put("八", 8);
        numbers.put("九", 9);
    }

    public void compile(String content) {
        int length = content.length();
        for (int i = 0; i < length; i++) {
            stack.offer(Long.valueOf(content.charAt(i)));
        }
    }

    abstract void interpreter(ExpContext context);

}

//千位
class ThusandsExpression extends Expression {

    @Override
    void interpreter(ExpContext context) {

    }
}

class UnitExpression extends Expression {

    @Override
    void interpreter(ExpContext context) {

        if (StrUtil.isBlank(context.getContent())) {
            // do nothing end
        }

        int index = context.getContent().length();
        String last = context.getContent().substring(index - 1);
        if (numbers.containsKey(last)) {
            stack.offer(Long.valueOf(last));
            content = context.getContent().substring(0, index);
        }
    }
}

class TenExpression extends Expression {

    @Override
    void interpreter(ExpContext context) {

    }
}



