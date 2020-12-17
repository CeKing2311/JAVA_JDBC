package com.ceking.dao2;

import com.ceking.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 *@author ceking
 *@description 封装对于数据表的通用操作
 *@date 2020-12-17 14:20
 */
public abstract class BaseDAO<T> {

    private  Class<T> clazz = null;
    {
        //获取当前BaseDAO的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        //获取了父类的泛型参数
        Type[] typeArguments = paramType.getActualTypeArguments();
        //泛型的第一个参数
        clazz = (Class<T>) typeArguments[0];
    }
    //更新操作（添加,修改,删除）
    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回 PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject((i + 1), args[i]);
            }
            //3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    //返回一个对象
    public T getInstance(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            res = ps.executeQuery();
            //获取元数据
            ResultSetMetaData metaData = res.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            if (res.next()) {
                //Order order =new Order();
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的值
                    Object columnValue = res.getObject(i + 1);
                    //获取每个列的列名
                    // getColumnName 获取列的列名 ,getColumnLabel 获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //String columnName = metaData.getColumnName(i + 1);
                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, res);
        }
        return null;
    }

    //返回数据集
    public List<T> getList(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            res = ps.executeQuery();
            //获取元数据
            ResultSetMetaData metaData = res.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (res.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的值
                    Object columnValue = res.getObject(i + 1);
                    //获取每个列的列名
                    // getColumnName 获取列的列名 ,getColumnLabel 获取列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //String columnName = metaData.getColumnName(i + 1);
                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, res);
        }
        return null;
    }
    //查询特殊的值通用方法
    public <E> E getValue(Connection conn, String sql, Object... args){
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            res = ps.executeQuery();
            if (res.next()){
               return  (E)res.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, res);
        }
        return  null;
    }
}
