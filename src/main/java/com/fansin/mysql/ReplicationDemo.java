package com.fansin.mysql;

import com.fansin.concurrent.TraceThreadPoolExecutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaofeng on 17-5-11.
 */
public class ReplicationDemo {

    /**
     * 更多参数请查看
     *
     * @see com.mysql.jdbc.ReplicationConnectionProxy
     * @see com.mysql.jdbc.LoadBalancedConnectionProxy
     * @see com.mysql.jdbc.FailoverConnectionProxy
     * @see com.mysql.jdbc.ConnectionPropertiesImpl
     */

//    private static String URL="jdbc:mysql:replication://172.17.0.3:3306,172.17.0.4:3306,172.17.0.5:3306/test?" +
//            "roundRobinLoadBalance=true&&replicationConnectionGroup=replication&replicationEnableJMX=true";
    private static String URL =
            "jdbc:mysql://(master)(172.17.0.3:3306),(master)(172.17.0.4:3306),(slave)(172.17.0.5:3306)/test?" +
            "roundRobinLoadBalance=true&&replicationConnectionGroup=replication&replicationEnableJMX=true";

    public static void main(String[] args) {
        TraceThreadPoolExecutor poolExecutor = new TraceThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS,
                                                                           new LinkedBlockingDeque<>());
        poolExecutor.submit(new Thread(new WriteTask()));
        poolExecutor.submit(new Thread(new ReadTask()));
        poolExecutor.submit(new Thread(new ReadTask()));
    }

    static Connection getNewConnections() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(URL, "root", "root");
    }

    //写
    static void masterWrite(Connection connection) {
        try {
            connection.setReadOnly(false);
            connection.setAutoCommit(false);
            connection.createStatement().executeUpdate(
                    "INSERT  INTO test.replication_table (name,demo)VALUES (\"" + Thread.currentThread().getName() +
                    "\",\"" + System.currentTimeMillis() + "\")");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //读
    static void slaveRead(Connection connection) {
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT id,name,demo FROM test.replication_table ");
            connection.commit();
            while (resultSet.next()) {
                String row = String.format("%d %s %s", resultSet.getLong("id"), resultSet.getString("name"),
                                           resultSet.getString("demo"));
                System.out.println(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class WriteTask implements Runnable {

        @Override
        public void run() {
            try {
                Connection connection = getNewConnections();
                for (int i = 0; i < 1000; i++) {
                    masterWrite(connection);
                    Thread.currentThread().sleep(3000l);
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

    static class ReadTask implements Runnable {

        @Override
        public void run() {
            try {
                Connection connection = getNewConnections();
                for (int i = 0; i < 3000; i++) {

                    slaveRead(connection);
                    Thread.currentThread().sleep(1000l);
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
