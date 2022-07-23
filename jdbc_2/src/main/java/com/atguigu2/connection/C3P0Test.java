package com.atguigu2.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/23
 */
public class C3P0Test {
    @Test
    public void testGetConnection()throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource("testc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);

    }
}
