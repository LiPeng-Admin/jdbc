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

    //问题2：输入身份证号或准考证号可以查询到学生的基本信息

    @Test
    public void testQueryStudentInfo() {
        System.out.println("请选择您要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");

        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号");
            String examCard = scanner.next();


        }else if ("b".equalsIgnoreCase(selection)){

        }else {
            System.out.println("您的输入有误，请重新进入程序");
        }

    }

    public <T> T getStudentInfo(Class<T> clazz, String sql, Object... args) {
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
