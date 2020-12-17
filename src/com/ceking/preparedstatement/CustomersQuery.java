package com.ceking.preparedstatement;

import com.ceking.bean.Customers;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
 *@author ceking
 *@description customers表的查询操作
 *@date 2020-12-16 15:07
 */
public class CustomersQuery {


    @Test
    public  void  queryCustomer(){
        String sql = "select id,name,email from customers where id=?";
        Customers cust = queryForCustomers(sql, 1);
        System.out.println(cust);
    }
    /**
     * 针对于customers的通用操作
     * @param sql
     * @param args
     * @return
     */
    public Customers  queryForCustomers(String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getDruidConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0 ; i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            res = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData metaData = res.getMetaData();
            //通过ResultSetMetaData 获取结果集的列数
            int count = metaData.getColumnCount();
            if (res.next()){
                Customers customer =new Customers();
                //处理结果集一行数据中的每一个列
                for (int i=0;i<count;i++){
                    Object obj = res.getObject(i + 1);
                    //获取每个列的列名
                    //String columnName = metaData.getColumnName(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //给customer对象指定的columnName属性，赋值为columnValue,通过反射
                    Field field = Customers.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer,obj);
                }
                return  customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps,res);
        }
        return  null;
    }

    @Test
    public  void  test1()  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql="select id,name,email,birth from customers where id =1 ";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            //判断结果集的下一条是否有数据
            if (resultSet.next()){
                //获取当前这条数据的各个字段
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                Customers customer= new Customers(id,name,email,birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(conn, ps,resultSet);
        }
    }
}
