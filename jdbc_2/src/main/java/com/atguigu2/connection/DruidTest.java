package com.atguigu2.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Description
 * 测试Druid的数据库连接池技术
 * @Author lipeng
 * @create 2022/7/23
 */
public class DruidTest {
    @Test
    public void testGetConnection()throws Exception{
        Properties pros = new Properties();
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(stream);

        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection connection = source.getConnection();
        System.out.println(connection);

    }
}
