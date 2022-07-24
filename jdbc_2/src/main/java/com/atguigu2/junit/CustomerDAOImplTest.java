package com.atguigu2.junit;

import com.atguigu.bean.Customer;
import com.atguigu.dao.CustomerDAOImpl;
import com.atguigu2.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/23
 */
public class CustomerDAOImplTest {
    CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Customer customer = new Customer(1, "张三", "zhangsan@126.com", new Date(456789451L));

            dao.insert(connection, customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);

        }

    }

    @Test
    public void testDeleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 16);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(18, "贝多芬", "beiduofen@126.com");
            dao.update(connection, customer);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }


    }

    @Test
    public void testCustomerById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection2();
            Customer coustomer = dao.getCoustomerById(connection, 7);
            System.out.println("查询的结果是：" + coustomer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection3();
            List<Customer> list = dao.getAll(connection);
            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Long count = dao.getCount(connection);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testMaxBirth() throws Exception {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(connection);
            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }
}
