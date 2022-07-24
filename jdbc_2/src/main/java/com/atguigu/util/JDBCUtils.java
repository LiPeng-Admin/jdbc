package com.atguigu.util;

import org.apache.commons.dbutils.DbUtils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description 操作数据库的工具类
 * @Author lipeng
 * @create 2022/7/17
 */
public class JDBCUtils {
    public static Connection getConnection() throws Exception {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;

    }

    public static void closeResource(Connection connection, Statement statement) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement statement, ResultSet res) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (res != null)
                res.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
    /*
    使用DbUtils工具类实现资源关闭
     *
     */


    public static void closeResource1(Connection connection, Statement statement, ResultSet res) {
        try {
            DbUtils.close(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            DbUtils.close(statement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            DbUtils.close(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
