package com.atguigu2.preparedstatement.crud;

import com.atguigu2.bean.Order;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description 针对于order 表的通用查询操作
 * @Author lipeng
 * @create 2022/7/17
 */
public class OrderForQuery {
    /*
    * 针对于表的字段名与类的属性名不相同的情况
    * 1:必须声明sql时，使用类的属性来命名字段的别名
    * 2：使用ResultSetMetaData时，需要使用getColumnLabel（）来替换getColumnName（）
    * 获取列的别名
    * 说明：如果sql 中没有给字段起别名，getColumnLabel（） 获取的就是列名
    *
    *
    * */

    public Order queryForOrder(String sql, Object... args)  {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //1:获取数据库的连接

            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);


            }
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的列名 ---不推荐使用
                    // String columnName = metaData.getColumnName(i + 1);

                    //此处应该使用获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //给order 对象指定的某个属性，赋值为columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);

                }
                return order;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement,resultSet);
        }
        return null;

    }

    @Test
    public void testQueryForOrder(){
        String sql="SELECT order_id,order_name,order_date FROM `order` WHERE order_id=?";
        Order order = queryForOrder(sql, 1);
        System.out.println(order);
    }

    @Test
    public void testQuery2() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //1:获取数据库 的连接
            connection = JDBCUtils.getConnection();

            //预编译sql 语句，返回PreparedStatement 实例
            String sql = "SELECT order_id orderId,order_name orderName,order_date orderDate FROM `order` WHERE order_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //处理结果集
            if (resultSet.next()) {
                //获取当前这条数据的各个字段值
                int orderId = resultSet.getInt(1);
                String orderName = resultSet.getString(2);
                Date orderDate = resultSet.getDate(3);
                //将数据封装为一个对象
                Order order = new Order(orderId, orderName, orderDate);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }


    }

}
