package com.ceking.dbutils;

import com.ceking.bean.Customers;
import com.ceking.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/*
 *@author ceking
 *@description
 * dbutils 封装了针对于数据库的增删改查操作
 *@date 2020-12-17 17:18
 */
public class QueryRunnerTest {

    @Test
    public  void  testInsert(){
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="insert into customers(name,email,birth) values(?,?,?)";
            int count = runner.update(conn, sql, "王磊", "wanglei@qq.com", "1978-04-05");
            System.out.println("添加了"+count+"条记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }
    /**
     * BeanHandler：是 ResultSetHandler接口的实现类，用于封装表中的一条记录
     */
    @Test
    public  void  testQuery() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="select id,name,email,birth from customers where id=?";
            BeanHandler<Customers> bean =new BeanHandler<>(Customers.class);
            Customers query = runner.query(conn, sql, bean, 22);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }

    /**
     * BeanListHandler：是 ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
     * @throws Exception
     */
    @Test
    public  void  testQueryList() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="select id,name,email,birth from customers where id<? ";
            BeanListHandler<Customers> bean =new BeanListHandler<>(Customers.class);
            List<Customers> query = runner.query(conn, sql, bean, 100);
            query.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }

    /**
     * MapHandler:是 ResultSetHandler接口的实现类，用于封装表中的一条记录Map
     * @throws Exception
     */
    @Test
    public  void  testQueryMap() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="select id,name,email,birth from customers where id=?";
            MapHandler map =new MapHandler();
            Map<String, Object> map1 = runner.query(conn, sql, map, 22);

            System.out.println(map1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }
    /**
     * MapListHandler：是 ResultSetHandler接口的实现类，用于封装表中的多条记录构成的Map集合
     * @throws Exception
     */
    @Test
    public  void  testQueryMapList() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="select id,name,email,birth from customers where id<? ";
            MapListHandler mapList =new MapListHandler();
            List<Map<String, Object>> mapList1 = runner.query(conn, sql, mapList, 100);
            mapList1.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }

    /**
     * ScalarHandler 获取返回单个的数据
     * @throws Exception
     */
    @Test
    public  void  testQueryCount() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner =new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql ="select count(0) from customers";
            ScalarHandler scalarHandler=new ScalarHandler();
            Object obj = runner.query(conn, sql, scalarHandler);
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null, null);
        }
    }
}

