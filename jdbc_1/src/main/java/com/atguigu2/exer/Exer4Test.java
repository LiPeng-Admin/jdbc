package com.atguigu2.exer;

import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @Description 完成学生信息的删除功能
 * @Author lipeng
 * @create 2022/7/19
 */
public class Exer4Test {
    public static void main(String[] args) {
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        String sql="delete from examstudent where ExamCard=?";

        int deleteInfo = deleteInfo(sql, examCard);
        if (deleteInfo >0){
            System.out.println("删除成功！");

        }else {
            System.out.println("查无此人，请重新输入！");
        }

    }


    public static int deleteInfo(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1：获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql,返回PrepareStatement实例

            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行结果集，并返回
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
