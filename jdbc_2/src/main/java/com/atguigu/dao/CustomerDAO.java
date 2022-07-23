package com.atguigu.dao;

import com.atguigu.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
此接口用于规范针对于customer 表的常用操作
 *
 */
public interface CustomerDAO {
    /*
    * 将customer 对象添加到数据库中
    * */
    void insert(Connection connection, Customer customer);
    /*
    *针对指定的id,删除表中的一条记录
    * */
    void deleteById(Connection connection,int id);
    /*
     *针对内存 中customer对象，去修改表中的一条记录
     * */
    void update(Connection connection,Customer customer);
    /*
     *针对指定的id，查询表中的一条记录
     * */
    Customer getCoustomerById(Connection connection,int id);
      /*
      查询表中的所有记录
      * */
    List<Customer>getAll(Connection connection);

    /*
    *返回数据表中的数据的条目数
    * */
    Long getCount(Connection connection);
/*
* 返回数据表中生日的最大数*/
    Date getMaxBirth(Connection connection);
}
