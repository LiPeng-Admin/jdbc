package com.atguigu.dao;

import com.atguigu.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 封装了针对于数据表的通用操作
 * @Author lipeng
 * @create 2022/7/23
 */
public abstract class BaseDAO {
    //考虑事务的通用的增删改操作
    public int update(Connection connection, String sql, Object... args) {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            return ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(null, ps);

        }

        return 0;
    }

    //考虑事务的通常查询一条记录操作
    public <T> T queryOneRecord(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            //预编译sql，返回PreparedStatement 实例
            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            //执行结果集返回ResultSet 实例
            resultSet = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (resultSet.next()) {
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //获取结果集的每一列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取结果集的列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);


                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.closeResource(null, ps, resultSet);
        }

        return null;
    }

    //考虑事物的通用的查询多条记录的操作
    public <T> List<T> getMultipleRecords(Connection connection, Class<T> clazz, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
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
            JDBCUtils.closeResource(null, ps, resultSet);
        }


        return null;
    }

    //用于查询特殊值的通用方法
    public <E> E getValue(Connection connection, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, resultSet);
        }

        return null;
    }
}
