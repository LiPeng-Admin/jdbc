package com.atguigu2.bean;

import java.sql.Date;
import java.util.Objects;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/17
 */
/*
* ORM编程思想（object relational mapping）
* 一个数据表对应一个java类
* 表中的一条记录 对应java 类的一个对象
* 表的一个字段对应java 类的一个属性

* */
public class Customer {
    private  int id;
    private String name;
    private String email;
    private Date birth;

    public Customer() {
    }

    public Customer(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(birth, customer.birth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, birth);
    }
}
