package com.fansin.mysql;

import com.fansin.concurrent.TraceThreadPoolExecutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaofeng on 17-5-11.
 */
public class LoadBalanceDemo {

    /**
     * 测试说明:
     * 1 jvm 添加参数:-Dcom.sun.management.jmxremote
     * 2 jconsole 连接本实例,然后在MBean寻找com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager,然后点击"操作",会看到很多选项,
     * 可以再getRegisteredConnectionGroups看到已注册的groups,可以对每个group进行操作,删除/添加host,查看相关信息.每次操作,都必须输入
     * groups的名称(不是很方便).
     *
     * */

    /**
     * 更多参数请查看
     *
     * @see com.mysql.jdbc.ReplicationConnectionProxy
     * @see com.mysql.jdbc.LoadBalancedConnectionProxy
     * @see com.mysql.jdbc.FailoverConnectionProxy
     * @see com.mysql.jdbc.ConnectionPropertiesImpl
     */
    private static String URL = "jdbc:mysql:loadbalance://172.17.0.3:3306,172.17.0.4:3306,172.17.0.5:3306/test?" +
                                "loadBalanceConnectionGroup=first&loadBalanceEnableJMX=true";

    public static void main(String[] args) {
        TraceThreadPoolExecutor executor = new TraceThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS,
                                                                       new LinkedBlockingDeque<Runnable>());
        executor.submit(new Thread(new Repeater()));
        executor.submit(new Thread(new Repeater()));
        executor.submit(new Thread(new Repeater()));
    }

    static Connection getNewConnections() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(URL, "root", "root");
    }

    static void executeSimpleTransaction(Connection connection, int conn, int trans) {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT sleep(1)/*Connection:" + conn + ",Transaction:" + trans + "*/");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class Repeater implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try (Connection c = getNewConnections()) {
                    for (int j = 0; j < 10; j++) {
                        executeSimpleTransaction(c, i, j);
                        Thread.sleep(Math.round(100 * Math.random()));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
