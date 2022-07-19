package com.atguigu2.statement.crud;

import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Description 演示使用PreparedStatement 替换Statement，解决sql注入问题
 *
 * @Author lipeng
 * @create 2022/7/18
 *
 * PreparedStatement 除了解决sql 拼串问题，sql问题，其他的优势：
 * 1：可以操作Blob 的数据
 * 2：可以实现更高效的批量操作
 *
 */
public class PreparedStatementTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String user = scanner.nextLine();

        System.out.println("请输入密码：");
        String password = scanner.nextLine();
//        String sql = "select user,password from user_table where user=? and password=?";
        String sql="select user,password from user_table where user='1' or 'AND password='=1 or '1'='1' ";
        User returnUser = getInstance(User.class, sql, user, password);
        if (returnUser != null) {
            System.out.println("登录成功");
        } else {
            System.out.println("用户名或密码错误");
        }



    }
    public  static <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //1:获取连接数据库
            connection = JDBCUtils.getConnection();
            //2:预编译sql 语句，返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);
            //3：填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);

            }
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            //通过ResultSetMetaData 获取结果集中的列数
            int columnCount = metaData.getColumnCount();

            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值

                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }
        return null;
    }




}
