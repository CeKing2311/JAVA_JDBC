package com.ceking.dao2;

import com.ceking.bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
 *@author ceking
 *@description
 *@date 2020-12-17 14:33
 */
public class CustomersDAOImpl extends BaseDAO<Customers> implements CustomersDAO {

    @Override
    public void insert(Connection conn, Customers cust) {
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id =?";
        update(conn, sql, id);
    }

    @Override
    public void updateById(Connection conn, Customers cust) {
        String sql = " update customers set name =? ,email=?,birth=? where id=?";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
    }

    @Override
    public Customers getById(Connection conn, int id) {
        String sql = " select id,name,email,birth from customers where id=?";
        return getInstance(conn, sql, id);
    }

    @Override
    public List<Customers> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        return getList(conn, sql);
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(0) from customers ";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn, sql);
    }
}
