package com.atguigu2.exer;

import com.atguigu2.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Description 输入身份证号或准考证号可以查询到学生的基本信息
 * @Author lipeng
 * @create 2022/7/19
 */
public class Exer3Test {


    public static void main(String[] args) {

        System.out.println("请选择您要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");

        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if ("a".equalsIgnoreCase(selection)) {
            System.out.println("请输入准考证号");
            String examCard = scanner.next();
            String sql = "SELECT FlowID flowID,Type type,IDCard idCard,ExamCard examCard,StudentName studentName,Location location,Grade grade FROM examstudent where ExamCard=?";
            Student studentInfo = getStudentInfo(Student.class, sql, examCard);
            if (studentInfo != null) {
                System.out.println(studentInfo);
            } else {
                System.out.println("输入的准考证号有误");
            }


        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号");
            String idCard = scanner.next();
            String sql = "SELECT FlowID flowID,Type type,IDCard idCard,ExamCard examCard,StudentName studentName,Location location,Grade grade FROM examstudent where IDCard=?";
            Student studentInfo = getStudentInfo(Student.class, sql, idCard);
            if (studentInfo != null) {
                System.out.println(studentInfo);
            } else {
                System.out.println("输入的身份证号有误");
            }

        } else {
            System.out.println("您的输入有误，请重新进入程序");
        }

    }


    public static <T> T getStudentInfo(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql,返回PreparedStatement实例
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);

            }
            //执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //返回列的个数
            int columnCount = metaData.getColumnCount();


            if (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);


                }
                return t;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
        return null;
    }
}
