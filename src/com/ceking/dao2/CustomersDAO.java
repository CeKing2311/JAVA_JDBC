package com.ceking.dao2;

import com.ceking.bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
 *@author ceking
 *@description
 * 对于customers表的常用操作
 *@date 2020-12-17 14:28
 */
public interface CustomersDAO {
    //添加操作
    void  insert(Connection conn, Customers cust);
    //删除
    void  deleteById(Connection conn,int id);
    //修改
    void  updateById(Connection conn,Customers cust);
    //查询
    Customers  getById(Connection conn,int id);
    //查询所有记录
    List<Customers> getAll(Connection conn);
    //返回所有记录数量
    Long getCount(Connection conn);
    //返回最大的生日
    Date getMaxBirth(Connection conn);

}
