package com.atguigu2.preparedstatement.crud;

import com.atguigu2.bean.Customer;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description 针对于Customer 表 的查询操作
 * @Author lipeng
 * @create 2022/7/17
 */
public class CustomerForQuery {
    /**
     * 针对于Customers 表的通用操作
     */
    public Customer queryForCustomer(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1:获取数据库的连接
            connection = JDBCUtils.getConnection();

            //预编译sql 语句，返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行并返回结果集

            resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
               //处理结果集
            //通过ResultSetMetaData 获取结果集中的列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {

                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的列名

                    String columnName = metaData.getColumnLabel(i + 1);

                    //给customer 对象指定的某个属性，赋值为customerValue
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);

                }
                return customer;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }

        return null;

    }
    @Test
    public void testqueryForCustomer(){
        String sql="SELECT id,`name`,email,birth FROM customers WHERE id=?";
        Customer customer = queryForCustomer(sql, 1);
        System.out.println(customer);


    }

    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1:获取数据库的连接
            connection = JDBCUtils.getConnection();
            String sql = "SELECT id,`name`,email,birth FROM customers WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, 1);

            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();

            //处理结果集
            if (resultSet.next()) {//next():判断结果集的下一条是否有数据，如果有数据，返回true ，并指针下移
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式1：（不推荐）
                //            System.out.println("id "+id+",name "+name+",email "+email+",birth "+birth);
                //方式2：（不推荐）
                //            Object[] objects=new Object[]{id,name,email, birth.toInstant()};
                //方式3：（推荐）:将数据封装为一个对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }
    }
}
