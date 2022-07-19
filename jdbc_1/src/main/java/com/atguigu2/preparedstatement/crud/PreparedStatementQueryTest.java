package com.atguigu2.preparedstatement.crud;

import com.atguigu2.bean.Customer;
import com.atguigu2.bean.Order;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @Description 使用PreparedStatement实现针对与不同表的通用的查询操作（单条记录）
 * @Author lipeng
 * @create 2022/7/17
 */
public class PreparedStatementQueryTest {
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
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

    @Test
    public void test() {
        String sql="SELECT id,`name`,email,birth FROM customers WHERE id=?";
        Customer customer = getInstance(Customer.class, sql, 2);
        System.out.println(customer);

        String sql1="SELECT order_id orderId ,order_name orderName,order_date orderDate FROM `order` WHERE order_id=?";

        Order order = getInstance(Order.class, sql1, 2);
        System.out.println(order);
    }
}
