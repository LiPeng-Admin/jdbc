package com.atguigu2.blob;

import com.atguigu2.bean.Customer;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @Description 向数据表customer 中插入/查询blob 类型数据
 * @Author lipeng
 * @create 2022/7/19
 */
public class BlobTest {
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
            String sql = "INSERT INTO customers (name,email,birth,photo)VALUES(?,?,?,?)";
            //预编译sql,返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setObject(1, "张三丰");
            preparedStatement.setObject(2, "zhangsanfeng@126.com");
            preparedStatement.setObject(3, "1998-4-28");
            FileInputStream fileInputStream = new FileInputStream(new File("img.png"));
            preparedStatement.setBlob(4, fileInputStream);

            //执行结果集
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            JDBCUtils.closeResource(connection, preparedStatement);

        }


    }

    //查询数据表Blob 类型字段
    @Test
    public void testQuery() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream blobBinaryStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();

            //预编译sql,返回PreparedStatement 实例
            String sql = "SELECT id,`name`,email,birth,photo FROM customers WHERE id=?";
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            preparedStatement.setObject(1, 23);
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            blobBinaryStream = null;
            fileOutputStream = null;
            if (resultSet.next()) {

                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date date = resultSet.getDate(4);
                Customer customer = new Customer(id, name, email, date);
                System.out.println(customer);
                //将blob 类型的字段下载下来，保存在本地
                Blob blob = resultSet.getBlob(5);

                blobBinaryStream = blob.getBinaryStream();
                fileOutputStream = new FileOutputStream("zhangsanfeng.jpg");
                byte[] bytes = new byte[1024];
                int len;
                while ((len = blobBinaryStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }

        try {
            if (blobBinaryStream != null)
                blobBinaryStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
