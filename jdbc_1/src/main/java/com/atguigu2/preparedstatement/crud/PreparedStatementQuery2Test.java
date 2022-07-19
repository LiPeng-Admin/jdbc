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
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 使用PreparedStatement实现针对与不同表的通用的查询操作（多条记录）
 * @Author lipeng
 * @create 2022/7/17
 */
public class PreparedStatementQuery2Test {
    public <T> List<T> getInstance2(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //1:获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql，返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                //3：填充占位符
                preparedStatement.setObject(i + 1, args[i]);

            }
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetaData 获取结果集中的列数
            int columnCount = metaData.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();

            while (resultSet.next()) {

                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //处理结果集一行数据中的每一列
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                list.add(t);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);


        }
        return null;

    }
    @Test
    public void test(){
        String sql="SELECT order_id orderId ,order_name orderName,order_date orderDate FROM `order` WHERE order_id<?";
        List<Order> list = getInstance2(Order.class, sql, 3);
        System.out.println(list);

        String sql1="SELECT id,`name`,email,birth FROM customers WHERE id<?";
        List<Customer> list1 = getInstance2(Customer.class, sql1, 10);
        list1.forEach(System.out::println);

    }
}
