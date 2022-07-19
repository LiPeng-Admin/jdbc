package com.atguigu2.exer;

import com.atguigu2.util.JDBCUtils;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/18
 */
public class Exer2Test {

    public static void main(String[] args) {
        //问题1:向examstudent表中添加一条记录
        /*
	 *  Type:
		IDCard:
		ExamCard:
		StudentName:
		Location:
		Grade:
	 */
        Scanner scanner = new Scanner(System.in);

        System.out.print("四级/六级：");
        int type = scanner.nextInt();
        System.out.print("身份证号：");
        String IDCard = scanner.next();
        System.out.print("准考证号：");
        String examCard = scanner.next();
        System.out.print("学生姓名：");
        String studentName = scanner.next();
        System.out.print("所在城市：");
        String location = scanner.next();
        System.out.print("考试成绩：");
        int grade = scanner.nextInt();
        String sql = "insert into examstudent(type,IDCard,ExamCard,StudentName,Location,Grade)values(?,?,?,?,?,?)";
        int insert = update(sql, type, IDCard, examCard, studentName, location, grade);
        if (insert > 0) {
            System.out.println("信息添加成功");
        } else {
            System.out.println("信息添加失败");
        }
    }

    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1：获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql，返回PreparedStatement 实例
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);

            }
            //执行结果集
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
