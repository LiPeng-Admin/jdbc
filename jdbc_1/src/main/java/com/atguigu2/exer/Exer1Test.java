package com.atguigu2.exer;

import com.atguigu2.util.JDBCUtils;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/18
 */
/*
 * 从控制台向数据库的表customers中插入一条数据，表结构如下：
 *
 * */
public class Exer1Test {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String name = scanner.next();
        System.out.println("请输入邮箱：");
        String email = scanner.next();
        System.out.println("请输入出生日期：");
        String birth = scanner.next();


        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int insert = update(sql, name, email, birth);
        if(insert>0){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }



    }

    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //获取数据库的连接

            connection = JDBCUtils.getConnection();
            //预编译sql,返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);

            }
            //执行
           return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection, preparedStatement);

        }
        return 0;


    }
}
