package com.fansin.mysql;

import cn.hutool.core.date.TimeInterval;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhaofeng on 17-5-11.
 */
public class FailoverDemo {

    /**
     * 更多参数请查看
     *
     * @see com.mysql.jdbc.FailoverConnectionProxy
     * @see com.mysql.jdbc.LoadBalancedConnectionProxy
     * @see com.mysql.jdbc.FailoverConnectionProxy
     * @see com.mysql.jdbc.ConnectionPropertiesImpl
     */
    private static String URL       = "jdbc:mysql://172.17.0.3:3306,172.17.0.4:3306,172.17.0.5:3306/test?" +
                                      "failoverReadOnly=false&&secondsBeforeRetryPrimaryHost=1&queriesBeforeRetryPrimaryHost=3&retriesAllDown=5";
    private static String URL_ONE   = "jdbc:mysql://172.17.0.13:3306,172.17.0.14:3306,172.17.0.5:3306/test?" +
                                      "failoverReadOnly=false&&secondsBeforeRetryPrimaryHost=1&queriesBeforeRetryPrimaryHost=3&retriesAllDown=5";
    private static String URL_ERROR = "jdbc:mysql://172.17.0.13:3306,172.17.0.14:3306,172.17.0.15:3306/test?" +
                                      "failoverReadOnly=false&&secondsBeforeRetryPrimaryHost=1&queriesBeforeRetryPrimaryHost=3&retriesAllDown=5";

    static Connection getNewConnections(String url) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url, "root", "root");
    }

    public static void main(String[] args) {
        TimeInterval timeInterval = new TimeInterval();
        try {
            System.out.println("全部正确 开始连接.......");
            getNewConnections(URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("耗时:" + timeInterval.intervalSecond());
        }

        timeInterval.restart();
        try {
            System.out.println("最后一个正确 开始连接.......");
            getNewConnections(URL_ONE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("耗时:" + timeInterval.intervalSecond());
        }

        timeInterval.restart();
        try {
            System.out.println("全部错误 开始连接.......");
            getNewConnections(URL_ERROR);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("耗时(主机个数*secondsBeforeRetryPrimaryHost*queriesBeforeRetryPrimaryHost*retriesAllDown):" +
                               timeInterval.intervalSecond());
        }
    }

}
