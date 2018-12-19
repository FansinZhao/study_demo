package com.fansin.string;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by zhaofeng on 17-4-14.
 */
public class StringDemo {

    public static void main(String[] args) {
        String str = "123456";
//        jdk 新特性
        int i = 1_0_0_2;
        int j = 0b110;
        System.out.println(str + i + " " + j);

        System.out.println("hi".hashCode() + ">" + "ok".hashCode());
        String str1 = "hi" + "ok";
        String str2 = "hi";
        String str3 = "ok";
        System.out.printf("%s %s %s \n", str1.hashCode(), str2.hashCode(), str3.hashCode());

        formatter();

        simpleDateFormat();
        messageFormat();
        choiceFormat();
        decimalFormat();

    }

    public static void simpleDateFormat() {
        System.out.println("---------常用不解释---------");
    }

    public static void choiceFormat() {
        System.out.println("--------------ChoiceFormat------------");
        /*

        当且仅当 limit[j] <= X < limit[j+1] 时，X 匹配 j

       创建 ChoiceFormat 时，必须指定一个 format 数组和一个 limit 数组。这些数组的长度必须相同。例如，

        limits = {1,2,3,4,5,6,7}
        formats = {"Sun","Mon","Tue","Wed","Thur","Fri","Sat"}
        limits = {0, 1, ChoiceFormat.nextDouble(1)}
        formats = {"no files", "one file", "many files"}
        （nextDouble 可用于获取下一个更大的 double 值，以形成半开区间。）

         */

//limits 与 dayOfWeekNames数量一致
        double[] limits = { 1, 2, 3, 4, 5, 6, 7 };
        String[] dayOfWeekNames = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        ChoiceFormat form = new ChoiceFormat(limits, dayOfWeekNames);
        ParsePosition status = new ParsePosition(0);
        for (double i = 0.0; i <= 8.0; ++i) {
            status.setIndex(0);//设置第一个字符解析
            System.out.println(i + " -> " + form.format(i) + " -> "
                               + form.parse(form.format(i), status));
        }

        double[] filelimits = { 0, 1, 2 };
        String[] filepart = { "are no files", "is one file", "are {2} files" };
        ChoiceFormat fileform = new ChoiceFormat(filelimits, filepart);
        Format[] testFormats = { fileform, null, NumberFormat.getInstance() };
        MessageFormat pattform = new MessageFormat("There {0} on {1}");
        pattform.setFormats(testFormats);
        Object[] testArgs = { null, "ADisk", null };
        for (int i = 0; i < 4; ++i) {
            testArgs[0] = new Integer(i);
            testArgs[2] = testArgs[0];
            System.out.println(pattform.format(testArgs));
        }

        //数值1#描述1|数值2#描述2|数值3#描述3
        //当输入在两个值区间(下界值<=输入值<上界值),则取下区间值对应描述,无穷小/大,取下/上边界
        ChoiceFormat fmt = new ChoiceFormat(
                "-1#is negative| 0#is zero or fraction | 1#is one |1.0<is 1+ |2#is two |2<is more than 2.");
        System.out.println("Formatter Pattern : " + fmt.toPattern());

        System.out.println("Format with -INF : " + fmt.format(Double.NEGATIVE_INFINITY));
        System.out.println("Format with -1.0 : " + fmt.format(-1.0));
        System.out.println("Format with 0 : " + fmt.format(0));
        System.out.println("Format with 0.9 : " + fmt.format(0.9));
        System.out.println("Format with 1.0 : " + fmt.format(1));
        System.out.println("Format with 1.5 : " + fmt.format(1.5));
        System.out.println("Format with 2 : " + fmt.format(2));
        System.out.println("Format with 2.1 : " + fmt.format(2.1));
        System.out.println("Format with NaN : " + fmt.format(Double.NaN));
        System.out.println("Format with +INF : " + fmt.format(Double.POSITIVE_INFINITY));

    }

    public static void messageFormat() {
        System.out.println("------------MessageFormat 简单的格式化工具---------");
        /*
        格式
         MessageFormatPattern:
         String
         MessageFormatPattern FormatElement String

         FormatElement:
                 { ArgumentIndex }
                 { ArgumentIndex , FormatType }
                 { ArgumentIndex , FormatType , FormatStyle }

         FormatType: one of
                 number date time choice

         FormatStyle:
                 short
                 medium
                 long
                 full
                 integer
                 currency
                 percent
                 SubformatPattern

         String:
                 StringPartopt
                 String StringPart

         StringPart:
                 ''
                 ' QuotedString '
                 UnquotedString

         SubformatPattern:
                 SubformatPatternPartopt
                 SubformatPattern SubformatPatternPart

         SubFormatPatternPart:
                 ' QuotedPattern '
                 UnquotedPattern

         */

        String pattern = "第0个数是{0},第3个数是{2},格式化日期:{1,date,medium},{1,time,medium}";//
        MessageFormat messageFormat = new MessageFormat(pattern);//new
        String result = messageFormat.format(new Object[] { 2334, new Date(), 5556 });
        System.out.println(result);
    }

    public static void decimalFormat() {
        System.out.println("------------DecimalFormat----------");
        /*

        模式+字符

    ----模式

        DecimalFormat 模式具有下列语法：
        模式：
        正数模式
        正数模式；负数模式
        正数模式：
        前缀opt 数字后缀opt
        负数模式：
        前缀opt 数字后缀opt
        前缀：
        除 \uFFFE、\uFFFF 和特殊字符以外的所有 Unicode 字符
        后缀：
        除 \uFFFE、\uFFFF 和特殊字符以外的所有 Unicode 字符
        数字：
        整数指数opt
        整数。小数指数opt
        整数：
        最小整数
                 #
        # 整数
        # , 整数
        最小整数：
                 0
        0 最小整数
        0 , 最小整数
        小数：
        最小小数opt 可选小数opt
        最小小数：
        0 最小小数opt
        可选小数：
        # 可选小数opt
        指数：
        E 最小指数
        最小指数：
        0 最小指数opt
    ----字符
        符号	位置	本地化？	含义
        0	数字	是	阿拉伯数字
        #	数字字	是	阿拉伯数字，如果不存在则显示为 0
        .	数字	是	小数分隔符或货币小数分隔符
        -	数字	是	减号
        ,	数字	是	分组分隔符
        E	数字	是	分隔科学计数法中的尾数和指数。在前缀或后缀中无需加引号。
        ;	子模式边界	是	分隔正数和负数子模式
        %	前缀或后缀	是	乘以 100 并显示为百分数
        \u2030	前缀或后缀	是	乘以 1000 并显示为千分数
        ¤ (\u00A4)	前缀或后缀	否	货币记号，由货币符号替换。如果两个同时出现，则用国际货币符号替换。如果出现在某个模式中，则使用货币小数分隔符，而不使用小数分隔符。
        '	前缀或后缀	否	用于在前缀或或后缀中为特殊字符加引号，例如 "'#'#" 将 123 格式化为 "#123"。要创建单引号本身，请连续使用两个单引号："# o''clock"。

         */

        // Print out a number using the localized number, integer, currency,
        // and percent format for each locale
        Locale[] locales = NumberFormat.getAvailableLocales();
        double myNumber = -1234.56;
        NumberFormat form;
        for (int j = 0; j < 4; ++j) {
            System.out.println("FORMAT");
            for (int i = 0; i < locales.length; ++i) {
                if (locales[i].getCountry().length() == 0) {
                    continue; // Skip language-only locales
                }
                System.out.print(locales[i].getDisplayName());
                switch (j) {
                    case 0://数字模式
                        form = NumberFormat.getInstance(locales[i]);
                        break;
                    case 1://整数模式
                        form = NumberFormat.getIntegerInstance(locales[i]);
                        break;
                    case 2://货币模式
                        form = NumberFormat.getCurrencyInstance(locales[i]);
                        break;
                    default://百分比模式
                        form = NumberFormat.getPercentInstance(locales[i]);
                        break;
                }
                if (form instanceof DecimalFormat) {
                    System.out.print(": " + ((DecimalFormat) form).toPattern());
                }
                System.out.print(" -> " + form.format(myNumber));
                try {
                    System.out.println(" -> " + form.parse(form.format(myNumber)));
                } catch (ParseException e) {
                }
            }
        }

    }

    public static void formatter() {
        System.out.println("-----------formatter jdk示例----------");
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.CHINA);
        System.out.println("1 重排序");
        formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
        System.out.println("System.out 本身就是PrintStream");
        System.out.format("%4$2s %3$2s %2$2s %1$2s", "aaaa", "bbbb", "cccc", "dddd");
        System.out.println("\n------------------");
        System.out.println(formatter.toString());
        System.out.println("2 指定数据风格");
        System.out.format("%(,.3f", 123456789.456);
        Calendar c = Calendar.getInstance();
        System.out.format("\nDuke's Birthday:%1$TH:%1$TM:%1$TS %1$tm %1$te,%1$tY\n", c);
        System.out.format("boolean: %b\n", null);
        System.out.format("Boolean: %B\n", 1);
        System.out.format("boolean: %b\n", false);
        System.out.format("hex String: %h\n", null);
        System.out.format("hex string: %h\n", 15);
        System.out.format("百分比 和换行n: %1$d%%%n", 85);
        System.out.format("String: %s\n", null);
        System.out.format("String: %s\n", "aaa");
        System.out.format("String: %S\n", "aaa");
        System.out.format("char: %c\n", null);
        System.out.format("char: %c\n", 'a');
        System.out.format("char: %C\n", 'a');
//        System.out.format("String: %C\n","a");
        System.out.format("十进制: %d\n", null);
        System.out.format("十进制: %d\n", 20);
//        System.out.format("int: %D\n",20);
//        System.out.format("int: %d\n","20");
        System.out.format("八进制: %o\n", null);
        System.out.format("八进制: %o\n", 9);
//        System.out.format("八进制: %O\n",9);
        System.out.format("十六进制: %x\n", null);
        System.out.format("十六进制: %x\n", 15);
        System.out.format("十六进制: %X\n", 15);
        System.out.format("科学计数法: %e\n", null);
        System.out.format("科学计数法: %e\n", 0000100000e2);
        System.out.format("科学计数法: %E\n", 100000e2);
//        System.out.format("科学计数法: %E\n",100000);
        System.out.format("浮点: %f\n", null);
        System.out.format("浮点: %f\n", 100000.2);
        System.out.format("浮点: %.2f\n", 100000.2);
        System.out.format("带精度科学计数法: %.4g\n", null);
        System.out.format("带精度科学计数法: %.4g\n", 0000101000.2);
        System.out.format("带精度科学计数法: %.4G\n", 101000.2);
        System.out.format("带精度十六进制浮点???: %.4a\n", 15.15);
        System.out.format("带精度十六进制浮点???: %.4A\n", 15.15);
        System.out.format("日期 月份: %TB\n", c);
        System.out.format("日期 月份: %Tb\n", new Date());
        System.out.format("日期 月份: %Th\n", c);
        System.out.format("日期 星期: %TA\n", c);
        System.out.format("日期 星期: %Ta\n", c);
        System.out.format("日期 年份前两位?: %TC\n", c);
        System.out.format("日期 年份后两位: %Ty\n", c);
        System.out.format("日期 四位年份: %TY\n", c);
        System.out.format("日期 一年中的天数: %Tj\n", c);
        System.out.format("日期 月份: %Tm\n", c);
        System.out.format("日期 一个月天数: %Td\n", c);
        System.out.format("日期 一个月天数不带0: %Te\n", c);
        System.out.format("时间 24小时 %tH\n", c);
        System.out.format("时间 24小时不带0 %tk\n", c);
        System.out.format("时间 12小时 %tI\n", c);
        System.out.format("时间 12小时不带0 %tl\n", c);
        System.out.format("时间 分钟 %tM\n", c);
        System.out.format("时间 秒 %tS\n", c);
        System.out.format("时间 毫秒 %tL\n", c);
        System.out.format("时间 毫微秒 %tN\n", c);
        System.out.format("时间 am/pm %tp\n", c);
        System.out.format("时间 am/pm %Tp\n", c);
        System.out.format("时间 时区 %tZ\n", c);
        System.out.format("时间日期组合 24小时 %tR\n", c);
        System.out.format("时间日期组合 24小时 %TR\n", c);
        System.out.format("时间日期组合 24小时 %TT\n", c);
        System.out.format("时间日期组合 24小时 %tT\n", c);
        System.out.format("时间日期组合 12小时 %Tr\n", c);
        System.out.format("时间日期组合 日期 %TD\n", c);
        System.out.format("时间日期组合 日期 %TF\n", c);
        System.out.format("时间日期组合 日期时间 %Tc\n", c);
        System.out.format("flag  左对齐无效?:%s\n", "        dddddddddd   ");
        System.out.format("flag  左对齐无效?:%-10s\n", "         dddddddddd   ");
        System.out.format("flag  在八进制和十六进制前面添加0或者0x替换形式:%#o %#x\n", 7, 15);
        System.out.format("flag  只能整数和浮点显示符号:%+d\n", 15);
        System.out.format("flag  只能整数和浮点显示符号:%+.2f\n", -15.25);
        System.out.format("flag  正值,左面添加空格:% .2f\n", 15.25);
        System.out.format("flag  正值,左面添加空格:% .2f\n", -15.25);
        System.out.format("flag  结果用0填充,浮点在右边填充,:%06f\n", 1525.2);
        System.out.format("flag  结果用0填充,整数在左边填充,:%06d\n", 15252);
        System.out.format("flag  结果使用会计计算,小数部分不做分割:%0,6f\n", 1525.222);
        System.out.format("flag  结果为负数则用括号括起来:%(6f\n", 1525.222);
        System.out.format("flag  结果为负数则用括号括起来:%(6f\n", -1525.222);
        System.out.format("flag  混合使用:%0,+6f\n", 1525.222);
        System.out.format("flag  混合使用顺序无关:%,+06f\n", 1525.222);

    }

}
