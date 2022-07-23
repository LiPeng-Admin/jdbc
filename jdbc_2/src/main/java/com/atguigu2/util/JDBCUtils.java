package com.atguigu2.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

/**
 * @Description 使用c3p0的数据库连接池技术
 * @Author lipeng
 * @create 2022/7/23
 */
public class JDBCUtils {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("testc3p0");

    public static Connection getConection() throws Exception {

        Connection connection = cpds.getConnection();
        return connection;
    }
}
