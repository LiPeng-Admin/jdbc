package com.atguigu.transaction;

import com.atguigu.bean.User;
import com.atguigu.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description 实现数据BB
 * @Author lipeng
 * @create 2022/7/21
 */
/*
* 数据库事务介绍
一组逻辑操作单元,使数据从一种状态变换到另一种状态。
*   一组逻辑操作单元：一个或多个DML操作
*
*事务处理（事务操作）：
    * 保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
    * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存下来；
    * 要么数据库管理系统将放弃所作的所有修改，整个事务**回滚(rollback)**到最初状态。
*
* 数据一旦提交，就不可回滚
*
* 哪些操作会导致数据的自动提交
* DDL 操作一旦执行，都会自动提交
*       通过set autocommit=false的方式取消DDL不会生效
* DML 默认情况下，一旦执行，就会自动提交
*       通过set autocommit=false 的方式取消DML 操作的自动提交
*
* 默认在关闭连接时，会自动的提交数据
*
* 保数据库中数据的一致性，数据的操纵应当是离散的成组的逻辑单元：
* 当它全部完成时，数据的一致性可以保持，而当这个单元中的一部分操作失败，整个事务应全部视为错误，所有从起始点以后的操作应全部回退到开始状态。
*
*
* 事务的ACID属性
原子性（Atomicity）
原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。

一致性（Consistency）
事务必须使数据库从一个一致性状态变换到另外一个一致性状态。

隔离性（Isolation）
事务的隔离性是指一个事务的执行不能被其他事务干扰，即一个事务内部的操作及使用的数据对并发的其他事务是隔离的，并发执行的各个事务之间不能互相干扰。

持久性（Durability）
持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来的其他操作和数据库故障不应该对其有任何影响。

*
*
*  数据库的并发问题
对于同时运行的多个事务, 当这些事务访问数据库中相同的数据时, 如果没有采取必要的隔离机制, 就会导致各种并发问题:

脏读: 对于两个事务 T1, T2, T1 读取了已经被 T2 更新但还没有被提交的字段。之后, 若 T2 回滚, T1读取的内容就是临时且无效的。
不可重复读: 对于两个事务T1, T2, T1 读取了一个字段, 然后 T2 更新了该字段。之后, T1再次读取同一个字段, 值就不同了。
幻读: 对于两个事务T1, T2, T1 从一个表中读取了一个字段, 然后 T2 在该表中插入了一些新的行。之后, 如果 T1 再次读取同一个表, 就会多出几行。
数据库事务的隔离性: 数据库系统必须具有隔离并发运行各个事务的能力, 使它们不会相互影响, 避免各种并发问题。

一个事务与其他事务隔离的程度称为隔离级别。数据库规定了多种事务隔离级别, 不同隔离级别对应不同的干扰程度, 隔离级别越高, 数据一致性就越好, 但并发性越弱。

* */
public class TransactionTest {
    @Test
    public void testUpdate() {
        String sql = "UPDATE user_table SET balance=balance+100 WHERE `user`=? ";
        update(sql, "AA");

        //模拟网络异常
        System.out.println(10 / 0);
        String sql2 = "UPDATE user_table SET balance=balance-100 WHERE `user`=?";
        update(sql2, "BB");
    }
    //通用的增删改操作

    public int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql,返回PreparedStatement 实例

            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }

        return 0;


    }

    //考虑数据库事务的转账操作
    //通用的增删改操作（2.0版本：考虑事务）
    @Test
    public void testUpdate2() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            //1：取消数据的自动提交
            System.out.println(connection.getAutoCommit());//true
            connection.setAutoCommit(false);
            String sql1 = "UPDATE user_table SET balance=balance+100 WHERE `user`=? ";
            System.out.println(10 / 0);
            update2(connection, sql1, "AA");
            String sql2 = "UPDATE user_table SET balance=balance-100 WHERE `user`=? ";
            update2(connection, sql2, "BB");
            System.out.println("转账成功");
            //2:提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //3:回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    public int update2(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //修改其为自动提交数据
            //主要针对使用数据库连接池使用
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(null, ps);
        }

        return 0;
    }

    //通用的查询操作：用于返回数据表中一条记录（version2.0:考虑上事务）
    public <T> T getQuery(Connection connection, Class<T> clazz, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
//            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //处理结果集一行数据中的每一列
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    // 使用反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //修改其为自动提交数据
                //主要针对使用数据库连接池使用
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(null, ps, resultSet);
        }

        return null;

    }
    @Test
    public void testTransactionQuery()throws  Exception{

        Connection connection = JDBCUtils.getConnection();
        //获取数据库的隔离级别
        System.out.println(connection.getTransactionIsolation());
        //设置数据库的隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消数据的自动提交
        connection.setAutoCommit(false);
        String sql="select user,password,balance from user_table where user=?";
        User user = getQuery(connection, User.class, sql, "CC");
        System.out.println(user);

    }
    @Test
    public void testTransactionUpdate()throws Exception{
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        String sql="UPDATE user_table SET balance=? WHERE `user`=?";
        update2(connection,sql,1000,"CC");



    }

}
