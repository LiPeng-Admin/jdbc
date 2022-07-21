package com.atguigu2.preparedstatement.crud;

import com.atguigu.connection.ConnectionTest;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Description 使用PreparedStatement 来替换Statement,实现对数据表的增删改操作
 * @Author lipeng
 * @create 2022/7/17
 */
public class prepareStatementUpdateTest {

    //通用操作：增删改

    public void update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1:获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2:预编译sql 语句，返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);

            //3:填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);

            }
            //4:执行
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5:关闭资源

            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }
    @Test
    public void testCommonUpdateTest() {

//        String sql = "DELETE FROM customers WHERE id=?";
//        update(sql, 3);
        String sql="UPDATE `order` SET order_name=? WHERE order_id=?";
        update(sql,"CC",4);
    }

    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1:获取配置文件中的4个基本信息
            InputStream resourceAsStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            //2:加载驱动

            Class.forName(driverClass);


            connection = DriverManager.getConnection(url, user, password);

            //4：预编译sql 语句，返回PreparedStatement 实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)"; //？:占位符

            preparedStatement = connection.prepareStatement(sql);

            //5：填充占位符

            preparedStatement.setString(1, "哪吒");
            preparedStatement.setString(2, "nezha@gmail.com");
            //转换时间格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse("2000-10-1");

            preparedStatement.setDate(3, new java.sql.Date(date.getTime()));

            //6：执行操作

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //7：资源的关闭
        try {
            if (preparedStatement != null)
                preparedStatement.close();
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

    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1:获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2:预编译sql 语句，返回PreparedStatement 实例
            String sql = "update customers set name=? where id=?";
            preparedStatement = connection.prepareStatement(sql);

            //3:填充占位符
            preparedStatement.setObject(1, "莫扎特");
            preparedStatement.setObject(2, 18);


            //4:执行
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //5:资源的关闭
        finally {
            JDBCUtils.closeResource(connection, preparedStatement);

        }


    }




}
