package com.fansin.designpattern;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.Properties;

/**
 * Created by zhaofeng on 17-5-12.
 */
public class DecoratorDemo {
    /*
     *装饰者模式:
     * 跟建造者模式不同的是,对象的生成过程不是唯一的,可以被0个或多个装饰者修饰.
     * 其思想是动态的为对象添加新功能.
     * 应用场景:
     * 在jdk的各种流中,这个模式用的很多,比如为普通的流提供缓存的BufferedXXX流
     * 在实际项目中的应用,需求:动态生成update的sql脚本,其中条件部分
     * where field=value and .....  field in (xxx,yyy,zzz) group by field1,field2... order by field1,field2...asc|desc
     * 这里只是简单演示,并没有覆盖所有的情景
     *
     */

    public static void main(String[] args) {
        UpdateSQL updateSQL = new DynamicUpdateSQL();
        Properties properties = new Properties();
        properties.setProperty("name", "testDecoratorMode!");
        properties.setProperty("demo", "由装饰模式生成的sql执行!");
        updateSQL.updateSQL("test.replication_table", properties);
        System.out.println("SQL:" + updateSQL.toString());
        WhereConditionSQL whereSQL = new WhereConditionSQL(updateSQL);
        properties.clear();
        properties.setProperty("name", "pool-1-thread-1");
        properties.setProperty("demo", "1494523774786");
        whereSQL.updateWhere(properties, "=");
        System.out.println("Where SQL:" + whereSQL.toString());
    }
}

//component
abstract class UpdateSQL {

    abstract void updateSQL(String table, Properties properties);

    abstract void updateSQL(Properties properties, String operator, String delimiter);

    public void updateSQL(StringBuffer buffer, Properties properties, String operator, String delimiter) {
        if (properties == null && StrUtil.isNotBlank(operator)) {
            buffer.append(String.format(" %s ", operator));
            return;
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String sqlUnit = String.format(" %1$s %2$s '%3$s' %4$s ", entry.getKey(), operator, entry.getValue(),
                                           delimiter);
            sqlUnit = sqlUnit.replaceAll("\\s+", " ");
            buffer.append(sqlUnit);
        }
        buffer.setLength(buffer.length() - delimiter.length() - 1);
    }

}

//基本组件
class DynamicUpdateSQL extends UpdateSQL {

    private StringBuffer cache;

    public DynamicUpdateSQL() {
    }

    @Override
    public String toString() {
        return cache.toString();
    }

    /**
     * 创建 update table set xx=ccc 语句
     *
     * @param table
     * @param properties
     */
    @Override
    void updateSQL(String table, Properties properties) {
        cache = new StringBuffer("UPDATE ").append(table).append(" SET ");
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            cache.append(entry.getKey()).append(" = '").append(entry.getValue()).append("' , ");
        }
        cache.setLength(cache.length() - 2);
    }

    /**
     * 提供 自定义参数设置
     *
     * @param properties
     * @param operator
     * @param delimiter
     */
    @Override
    void updateSQL(Properties properties, String operator, String delimiter) {
        updateSQL(cache, properties, operator, delimiter);
    }
}

class ConditionSQL extends UpdateSQL {

    private UpdateSQL updateSQL;

    public ConditionSQL(UpdateSQL updateSQL) {
        this.updateSQL = updateSQL;
    }

    @Override
    void updateSQL(String table, Properties properties) {
        updateSQL.updateSQL(table, properties);
    }

    @Override
    void updateSQL(Properties properties, String operator, String delimiter) {
        updateSQL.updateSQL(properties, operator, delimiter);
    }

    @Override
    public String toString() {
        return updateSQL.toString();
    }
}

class WhereConditionSQL extends ConditionSQL {

    public WhereConditionSQL(UpdateSQL updateSQL) {
        super(updateSQL);
        updateWhere();
    }

    private void updateWhere() {
        updateSQL(null, "WHERE", "");
    }

    public void updateWhere(Properties properties, String operator) {
        updateSQL(properties, operator, "AND");
    }

}

