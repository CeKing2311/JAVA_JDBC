package com.ceking.preparedstatement;

import com.ceking.bean.Customers;
import com.ceking.bean.Order;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/*
 *@author ceking
 *@description preparedstatement实现针对不同表的查询操作
 *@date 2020-12-16 16:12
 */
public class PreparedStatementQuery {

    //获取一个对象
    @Test
    public  void  test(){
        String sql = "select id,name,email,birth from customers where id=?";
        Customers customer = getInstance(Customers.class, sql, 12);
        System.out.println(customer);
        sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
    }
    //获取多条数据
    @Test
    public  void  test2(){
        String sql = "select id,name,email,birth from customers ";
        List<Customers> list = getList(Customers.class, sql);
        list.forEach(System.out::println);
    }
    /**
     * 针对不同的表获取一个对象
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0; i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            res = ps.executeQuery();
            //获取元数据
            ResultSetMetaData metaData = res.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            if (res.next()){
                //Order order =new Order();
                T t = clazz.newInstance();
                for (int i=0;i<columnCount;i++){
                    //获取每个列的值
                    Object columnValue = res.getObject(i+1);
                    //获取每个列的列名
                    // getColumnName 获取列的列名 ,getColumnLabel 获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //String columnName = metaData.getColumnName(i + 1);
                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps,res);
        }
        return  null;
    }

    /**
     * 针对不同的表获取一个列表
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getList(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0; i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            res = ps.executeQuery();
            //获取元数据
            ResultSetMetaData metaData = res.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (res.next()){
                T t = clazz.newInstance();
                for (int i=0;i<columnCount;i++){
                    //获取每个列的值
                    Object columnValue = res.getObject(i+1);
                    //获取每个列的列名
                    // getColumnName 获取列的列名 ,getColumnLabel 获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //String columnName = metaData.getColumnName(i + 1);
                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return  list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps,res);
        }
        return  null;
    }
}
