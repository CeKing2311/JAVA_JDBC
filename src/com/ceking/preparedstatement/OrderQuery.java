package com.ceking.preparedstatement;

import com.ceking.bean.Order;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
 *@author ceking
 *@description
 *@date 2020-12-16 15:42
 */
public class OrderQuery {


    @Test
    public  void  queryOrderTest(){
        String sql = "select order_id orderId,order_name orderName from `order` where order_id=?";
        Order order = queryForOrder(sql, 1);
        System.out.println(order);
    }
    public  Order queryForOrder(String sql,Object ...args) {
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
                Order order =new Order();
                for (int i=0;i<columnCount;i++){
                    //获取每个列的值
                    Object columnValue = res.getObject(i+1);
                    //获取每个列的列名
                    // getColumnName 获取列的列名 ,getColumnLabel 获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //String columnName = metaData.getColumnName(i + 1);
                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps,res);
        }
        return  null;
    }
    @Test
    public  void  test1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);

            res = ps.executeQuery();

            if (res.next()){
                int order_id = (int) res.getObject(1);
                String order_name = res.getString(2);
                Date order_date = res.getDate(3);
                Order order =new Order(order_id,order_name,order_date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, res);
        }
    }
}
