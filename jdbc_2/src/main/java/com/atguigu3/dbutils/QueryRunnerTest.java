package com.atguigu3.dbutils;

import com.atguigu.bean.Customer;
import com.atguigu2.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，它是对JDBC的简单封装
 * @Author lipeng
 * @create 2022/7/24
 */
public class QueryRunnerTest {
    //测试增删改（以插入为例）
    @Test
    public void testInsert(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql="INSERT INTO customers(`name`,email,birth)VALUES(?,?,?)";
            int insertCount = runner.update(connection, sql, "陆云生", "luyunsheng@126.com", "1997-04-17");
            System.out.println("添加了"+insertCount+"条记录");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }

    }
    //测试查询
    /*
     *BeanHandler 是ResultSetHandler 接口实现类对象，用于封装表中的一条记录
     * */
    @Test
    public void testQuery1(){

        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            //Druid数据库连接池连接
            connection = JDBCUtils.getConnection3();
            String sql="SELECT id,`name`,email,birth FROM customers WHERE id=?";

            BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);
            Customer customer = runner.query(connection, sql, handler, 18);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }

    }
    /*
     *BeanListHandler 是ResultSetHandler 接口实现类对象，用于封装表中的多条记录
     * */

    @Test
    public void testQuery2(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql="SELECT id,`name`,email,birth FROM customers WHERE id<?";
            BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);
            List<Customer> customerList = runner.query(connection, sql, beanListHandler, 18);
            customerList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }


    }
    /*
     *MapHandler 是ResultSetHandler 接口实现类对象，对应表中的一条记录
     * */

    @Test
    public void testQuery3(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql="SELECT id,`name`,email,birth FROM customers WHERE id=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(connection, sql, handler, 25);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }

    }

    /*
     *MapListHandler 是ResultSetHandler 接口实现类对象，对应表中的多条记录
     * */
    @Test
    public void testQuery4(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql="SELECT id,`name`,email,birth FROM customers WHERE id<?";
            MapListHandler mapListHandler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(connection, sql, mapListHandler, 20);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }


    }
    /*
    * ScalarHandler:ResultSetHandler 接口实现类对象，用于查询表中特殊值
    * */
    @Test
    public void testQuery5(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql="SELECT COUNT(*) FROM customers";
            ScalarHandler<Object> scalarHandler = new ScalarHandler<>();
            Long count =(Long) runner.query(connection, sql, scalarHandler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }


    }@Test
    public void testQuery6(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection();
            String sql="SELECT MAX(birth) FROM customers";
            ScalarHandler<Object> scalarHandler = new ScalarHandler<>();

            Date date = (Date) runner.query(connection, sql, scalarHandler);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,null);
        }

    }

}
