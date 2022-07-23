package com.atguigu.dao;

import com.atguigu.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/23
 */
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {
    @Override
    public void insert(Connection connection, Customer customer) {
        String sql = "INSERT INTO customers(`name`,email,birth) VALUES(?,?,?)";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "DELETE FROM customers WHERE id=?";
        update(connection, sql, id);

    }

    @Override
    public void update(Connection connection, Customer customer) {
        String sql = "UPDATE customers SET `name`=?,email=?,birth=? WHERE id=?";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());

    }

    @Override
    public Customer getCoustomerById(Connection connection, int id) {
        String sql = "SELECT id,`name`,email,birth FROM customers where id=?";
        Customer customer = queryOneRecord(connection, Customer.class, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql="SELECT id,`name`,email,birth FROM customers";
        List<Customer> list = getMultipleRecords(connection, Customer.class, sql);
        return list;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql="SELECT COUNT(*) FROM customers;";
        return getValue(connection, sql);

    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql="SELECT MAX(birth)FROM customers;";
       return getValue(connection,sql);
    }
}
