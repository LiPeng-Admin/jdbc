package com.atguigu2.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JDBCUtils {
    /**
     * @Description 使用c3p0的数据库连接池技术
     * @Author lipeng
     * @create 2022/7/23
     */

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("testc3p0");

    public static Connection getConection1() throws Exception {

        Connection connection = cpds.getConnection();
        return connection;
    }

    /**
     * @Description 使用dbcp的数据库连接池技术
     * @Author lipeng
     * @create 2022/7/24
     */
    private static DataSource source;
    static {
        try {
            Properties properties = new Properties();
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(stream);
            //创建一个DBCP 数据库连接池
            source = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  Connection getConnection2()throws Exception{

        Connection connection = source.getConnection();

        return connection;
    }

    /**
     * @Description 使用Druid的数据库连接池技术
     * @Author lipeng
     * @create 2022/7/24
     */
    private static DataSource source1;
    static {
        try {
            Properties properties = new Properties();

            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

            properties.load(stream);
            source1 = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection3()throws Exception{

        Connection connection = source1.getConnection();
        return connection;
    }



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


}
