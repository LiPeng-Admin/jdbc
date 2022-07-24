package com.atguigu2.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description
 * 测试DBCP的数据库连接池技术
 * @Author lipeng
 * @create 2022/7/23
 */
public class DBCPTest {
    //方式一：不推荐
    @Test
    public void getConnection()throws SQLException {
        //创建了DBCP的数据库连接
        BasicDataSource source = new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        source.setUsername("root");
        source.setPassword("123456");

        //设置其他涉及数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(20);
        Connection connection = source.getConnection();
        System.out.println(connection);

    }
    //方式2：推荐
    @Test
    public void testGetConnection1() throws Exception{
        Properties properties = new Properties();
          //方式1：
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
//        //方式2：
//        FileInputStream stream1 = new FileInputStream("src/main/resources/dbcp.properties");
        properties.load(stream);
        DataSource source = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = source.getConnection();

        System.out.println(connection);


    }
}
