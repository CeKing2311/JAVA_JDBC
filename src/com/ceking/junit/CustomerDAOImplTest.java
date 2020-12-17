package com.ceking.junit;

import com.ceking.bean.Customers;
import com.ceking.dao2.CustomersDAOImpl;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

/*
 *@author ceking
 *@description
 *@date 2020-12-17 15:35
 */
public class CustomerDAOImplTest {
    public CustomersDAOImpl dao =new CustomersDAOImpl();
    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customers cust =new Customers(1,"王重阳","wangchongyang@qq.com",new Date(32342453453L));
            dao.insert(conn,cust);
            System.out.println("添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
