package com.fansin.regex;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaofeng on 17-4-12.
 */
public class RegexDemo {

    public static void main(String[] args) throws IOException {


        /*

        java 正则整理/字符串操作
        匹配和捕获正则写法区别:
        匹配:必须将整个字符串全部描述
        捕获:只写自己感兴趣的部分
        例如:
        String str = "abc123aaa";
        System.out.println("匹配:"+str.matches("[a-z]{3}\\d{3}.*"));//匹配的话,必须有.*,否则匹配不到
        Pattern p = Pattern.compile("[a-z]{3}\\d{3}");//捕获则只写感兴趣的,如果加上.*,那就全部匹配了.
        Matcher m = p.matcher(str);
        if(m.find()){
            System.out.println("捕获:"+m.group(0));//没有分组就是0,分组是1
        }

        //
        System.out.println("打印unicode: "+"a\".length()+\"b".length());



        可以用于下面操作
        1 字符串匹配  String.matches(regex)
        2 字符串捕获  Pattern pattern = Patter.match(reg);Matcher matcher = pattern.matcher(str);matcher.group()
        3 字符串切割
        4 字符串替换  Pattern pattern = Patter.match(reg);Matcher matcher = pattern.matcher(str);matcher.appendM
         */
        notPrint();

        //
        strMatch();
    }

    /*
    ****匹配字符串****

    正则语法:
    非打印字符:
    \cx 匹配由x控制的特殊字符
    \f 匹配一个换页符 \cl
    \t 匹配一个水平制表符 \ci \u0009
    \n 匹配一个换行符 \cj \u000a
    \r 匹配一个回车符 \cm \u000d
    \v 匹配一个垂直制表符 \ck
    \e 匹配转义符    \u001b
    \s 匹配任何的空白字符,包含空格,换行,制表符等不可见符 [\f\r\n\t\v]
    \S 匹配任何非空白字符

    其他可见字符:
    \d 匹配任意数字 [0-9]
    \D 匹配非数字 [^0-9]
    \w 匹配数字和字母和下划线 [a-zA-Z0-9_]
    \W 匹配非数字和字母和下划线 [^a-zA-Z0-9_]
    \b 匹配边界字符,"er\b" 匹配never 不匹配verb
    \B 匹配非边界字符,"er\B" 匹配verb 不匹配never
    \u0000-\u00ff 半角字符 u0000:null , u0020:空格 u0022:"

    * */
    public static void notPrint() throws IOException {
        //字符串组成:水平制表符+换行符+回车符+空格+普通字符
        BufferedReader reader = new BufferedReader(new FileReader(
                new File("/home/zhaofeng/dev/ideaworkspace/demo_aop/src/main/java/com/fansin/regex/regex.txt")));
        char[] buf = new char[1024];
        reader.read(buf);
        String str = new String(buf);
        System.out.println("不可见的字符串str=[" + str + "]");
        //匹配
        System.out.println("\\n\\s.+ 匹配:" + str.matches("\\n\\sa\\u0000{1021,1021}"));//换行+空格+a+'\u0000'
        //截取
        //Pattern
        Pattern pattern = Pattern.compile("\\n\\sa\\u0000{1021,1021}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("匹配字符串:" + matcher.group());
        }
        //分割
        //String.split()
        //替换
        //只有这两个可以使用正则 String.repalceFirst()和String.repalceAll()

    }

    /**
     * 字符串获取
     * <p>
     * 捕获组---获取自己需要的字符串(截取),
     * (reg)
     * (reg1(reg2(reg3)))
     * 匹配结果有4个:groupCount() == 4 其中group(0)是全字符
     * (reg1)
     * (reg2(reg3))
     * (reg2)
     * (reg3)
     */
    public static void strMatch() {
        System.out.println("字符串匹配....");
        String str = "aa D \\ aa D \\";
        //匹配
        System.out.println(str.matches("[\\w+\\s{1}\\\\]+"));
        //截取
        Pattern pattern = Pattern.compile("((\\D+)(\\s))(D)");//      ((A)(B))(C)
        Matcher matcher = pattern.matcher(str);
        System.out.println("匹配到字符串数量:" + matcher.matches());//会将匹配位置变为最大,如果要重新匹配,必须先reset匹配位置
        matcher.reset();//重置匹配位置到0
        System.out.println("groupCount=" + matcher.groupCount());
        StringBuffer sb = new StringBuffer();//替换匹配字符
        if (matcher.find()) {
            System.out.println("0 > [" + matcher.group(0) + "]");//整个字符串 ((A)(B))(C)
            System.out.println("1 > [" + matcher.group(1) + "]");//((\D+)(\s)) ((A)(B))
            System.out.println("2 > [" + matcher.group(2) + "]");//((\D+)) (A)
            System.out.println("3 > [" + matcher.group(3) + "]");//(\s)) (B)
            System.out.println("4 > [" + matcher.group(4) + "]");//(D+\s.*) (C)
            matcher.appendReplacement(sb, "AA");//配合matcher.appendTail()
        }
        matcher.appendTail(sb);//将未匹配值追加到缓存
        System.out.println("替换变量:" + sb.toString());

        strMatch1();
    }

    public static void strMatch1() {
        System.out.println("字符串匹配....");
        String str = "AAbABbBAAAaBdBAcBA";
        //匹配
        System.out.println(str.matches("([a-z]?[A-Z])[a-z]([A-Z]).*"));
        System.out.println(str.matches("A(?:b|B|a).*"));
        //捕获
        Pattern pattern = Pattern.compile("([A-Z])[a-z]\\1+");
        Matcher matcher = pattern.matcher(str);
        System.out.println("groupCount=" + matcher.groupCount());

        StringBuffer sb;//替换匹配字符
        while (true) {
            System.out.println("---------------");
            sb = new StringBuffer();
            Boolean c = false;
            while (matcher.find()) {
                c = true;
                //            匹配到字符串
                System.out.println("匹配到字符串:" + matcher.group(0) + " 符合规则,替换掉");
                matcher.appendReplacement(sb, "");//配合matcher.appendTail()
            }
            matcher.appendTail(sb);//将未匹配值追加到缓存
            System.out.println("处理后文件:" + sb.toString());
//            pattern = Pattern.compile("([A-Z])[a-z]\\1+");
            matcher = pattern.matcher(sb.toString());
            if (!c) {
                break;
            }
        }

        System.out.println("最终处理后文件:" + sb.toString());

        pattern();
        System.out.println("去除驼峰标记");

//        System.out.println("smartReplace:"+ ReUtil.smartReplace(str,"(?<first>[A-Z])[a-z]\\k<first>",""));
        pressTestSmartReplace();
    }

    public static void pressTestSmartReplace() {
//        String regex = "(?<first>[A-Z])[a-z]\\k<first>";
//        String replacement = "";
//        for (int i = 0; i < 10; i++) {
//            String result = ReUtil.smartReplace(randomString(),regex,replacement);
//            String validate = result.replaceFirst(regex,"$");
//            Assert.isTrue(result.equals(validate),"测试不通过!"+result);
//            assert result.equals(validate):"测试不通过!"+result;
//        }
//        System.out.println("测试通过!");
    }

    public static String randomString() {
        List randomList = CollectionUtil.newArrayList("A", "B", "C", "a", "b", "c");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(RandomUtil.randomEle(randomList));
        }
        return stringBuilder.toString();
    }

    /**
     * 匹配模式
     * (pattern) 匹配pattern内容,并可以捕获matcher.group(1)
     * X(?:pattern) 匹配pattern,但是不捕获存储到组,在or和|很有效果
     * X(?=pattern) 匹配pattern前面的字符或组,但是不捕获pattern,查找pattern前面的字符/组
     * (?<=pattern)X 匹配pattern后面的字符或组,但是不捕获pattern,查找pattern前面的字符/组
     * (?!pattern)X 匹配不满足pattern前面的字符或组,但是不捕获pattern,查找非pattern前面的字符/组
     * (?<!pattern)X 匹配不满足pattern后面的字符或组,但是不捕获pattern,查找非pattern前面的字符/组
     * X(?>pattern) 独立分组,不对pattern进行回溯
     */
    public static void pattern() {
        String str = "fansinZhao come on Zhao fansinz";
        Pattern p = Pattern.compile("(an)");
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println("匹配到组:" + m.group(1));//这里只有一个分组,0 1表示都是一个
        }
        p = Pattern.compile("a(?:o|n)");//
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {
            System.out.println("X(?:X|Y)匹配到2个:但是不能使用组:" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        p = Pattern.compile("(?:a|c)o");//
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {
            System.out.println("(?:X|Y)X匹配到2个:但是不能使用组:" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        p = Pattern.compile("a.*(?=Zhao)");//?贪婪模式

        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {
            System.out.println("(?=X)[贪婪模式-匹配最后一个]匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("a.*?(?=Zhao)");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?=X)[非贪婪模式-匹配第一个]匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("fansin(?=Zhao)");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("Y(?=X)[非贪婪模式-匹配第一个]匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("(?=fansinZhao)fansin");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?=X)Y[非贪婪模式-匹配第一个]匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("fansin(?!Zhao)\\w+");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?!X)[贪婪模式-匹配第一个]不匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("(?<=fansin)Zhao");//?贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?<=X)Y反向[贪婪模式-匹配第一个]匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("(?<!fansin)Zhao");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?<!X)Y反向[贪婪模式-匹配第一个]不匹配表达式前面的内容" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        System.out.println("X(?>X) 等价与 抢占式匹配模式,所以表达式后面不能有X");
        //fansinZhao come on Zhao fansinz
        p = Pattern.compile("fansin(?>Zhao|ZHAo)");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("X(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        p = Pattern.compile("fansin(?>Zh|Zhao|zhao)");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        p = Pattern.compile("fansin(?>.*)z");//?非贪婪模式
        m = p.matcher(str);
        System.out.println("str = " + str);
        while (m.find()) {//循环匹配
            System.out.println("(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }
        p = Pattern.compile("\\b(?>integer|insert|in)\\b");//
        m = p.matcher("insert");
        System.out.println("str = insert");
        while (m.find()) {//循环匹配
            System.out.println("(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        p = Pattern.compile("(?>in|insert|integer)");//
        m = p.matcher("insert");
        System.out.println("str = insert");
        while (m.find()) {//循环匹配
            System.out.println("(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        //\b 边界,限定了全字匹配
        p = Pattern.compile("\\b(?>in|insert|integer)\\b");//
        m = p.matcher("insert");
        System.out.println("str = insert");
        while (m.find()) {//循环匹配
            System.out.println("(?>X)当匹配不匹配X时,阻止回溯,立即失败" + m.group(0));//无法使用组,group(1)会报越界错误
        }

        chars();
    }

    public static void chars() {

        String str = "aaaa";
        Pattern p = Pattern.compile("\\p{Lower}*");
        Matcher m = p.matcher(str);
        System.out.println("匹配大小写" + m.matches());
        System.out.println("匹配大小写" + str.matches("\\p{Lower}"));

        str = "qwert12345";
        p = Pattern.compile(".*3");
//        p = Pattern.compile(".*");可以匹配,有回溯
        m = p.matcher(str);
        if (m.find()) {
            System.out.println("贪婪匹配 " + m.group());
        }
        p = Pattern.compile(".*?3");
//        p = Pattern.compile(".*?");//匹配不到任何数据,从左到右匹配,无回溯
        m = p.matcher(str);
        if (m.find()) {
            System.out.println("懒散匹配 " + m.group());
        } else {
            System.out.println("懒散匹配 failed");
        }

//        p = Pattern.compile(".*+");//可以匹配
        p = Pattern.compile("q.*+");
//        p = Pattern.compile("q.*+5");//无法匹配.不能回溯
        m = p.matcher(str);
        while (m.find()) {
            System.out.println("强占匹配 " + m.group());
        }

        regExam();

    }

    /**
     * 考试
     */
    public static void regExam() {
        //第一题: 统计字符数
        String cat = "cat cat cat catddd cat";
        Pattern p = Pattern.compile("(\\bcat\\b)");
        Matcher matcher = p.matcher(cat);
        int num = 0;
        while (matcher.find()) {
            num++;
            System.out.println("开始位置-结束位置 " + matcher.start() + "-" + matcher.end());
        }
        System.out.println(matcher.lookingAt());//也可以使用matcher.reset()
        System.out.println("从头开始:" + matcher.start());
        System.out.println("匹配到cat数量:" + num);

        //第二题 匹配 lookingat 尝试从头匹配,find()只是匹配一次 matches() 全串匹配
        String foo = "fooooooo";
        Matcher m = Pattern.compile("foo").matcher(foo);
        System.out.println("全串匹配:" + m.matches());
        System.out.println("字符匹配:" + m.lookingAt());

        //第二道 replace
        String rep = "aaaa and BBB and CCCC";
        System.out.println("替换第一个:" + rep.replaceFirst("a.*?d", "&"));
        System.out.println("替换第二个:" + rep.replaceAll("a.*?d", "&"));

    }

}
