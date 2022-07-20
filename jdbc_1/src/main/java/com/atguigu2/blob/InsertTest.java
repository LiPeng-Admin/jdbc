package com.atguigu2.blob;

import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Description PreparedStatement 实现批量数据的操作，主要指的是批量插入（update,delete 本身具有批量操作的能力）
 * @Author lipeng
 * @create 2022/7/20
 */
public class InsertTest {
    //使用PreparedStatement 批量插入数据
    //方式1：
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            long startTime = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            //预编译sql,返回PreparedStatement 实例
            String sql = "insert into goods(name)values(?)";
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 1; i <= 20000; i++) {
                preparedStatement.setObject(1, "name_" + i);
                //执行结果集
                preparedStatement.execute();

            }

            long endTime = System.currentTimeMillis();
            System.out.println("花费的时间是：" + (endTime - startTime));//43102
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement);

        }


    }

    @Test
    public void testInsert2() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //方式2
            long startTime = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 1; i <= 1000000; i++) {

                preparedStatement.setObject(1, "name_" + i);
                //1:攒 sql
                preparedStatement.addBatch();
                if (i % 500 == 0) {
                    //2：执行batch
                    preparedStatement.executeBatch();
                    //清空batch
                    preparedStatement.clearBatch();
                }

            }
            long endTime = System.currentTimeMillis();
            System.out.println("花费的时间是：" + (endTime - startTime));//20000:841
            //1000000:16501
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    @Test
    public void testInsert3() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            long startTime = System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            //设置不默认提交
            connection.setAutoCommit(false);
            //预编译sql
            String sql = "insert into goods(name)values(?)";
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i <= 1000000; i++) {
                preparedStatement.setObject(1, "name_" + i);
                //1:攒 sql
                preparedStatement.addBatch();
                if (i % 500 == 0) {
                    //2：执行batch
                    preparedStatement.executeBatch();
                    //3:清空batch
                    preparedStatement.clearBatch();
                }

            }
            connection.commit();
            long endTime = System.currentTimeMillis();
            System.out.println("花费的时间是："+(endTime-startTime));//8446
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement);
        }


    }

}




