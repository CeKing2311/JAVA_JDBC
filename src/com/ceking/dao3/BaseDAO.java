package com.ceking.dao3;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
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
    public int update(Connection conn, String sql, Object... args){
        try {
            QueryRunner runner =new QueryRunner();
            int count = runner.update(conn, sql, args);
            return  count;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  0;
    }

    //返回一个对象
    public T getInstance(Connection conn, String sql, Object... args) {
        try {
            QueryRunner runner =new QueryRunner();
            BeanHandler<T> bean =new BeanHandler<>(clazz);
            return runner.query(conn, sql, bean, args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }

    //返回数据集
    public List<T> getList(Connection conn, String sql, Object... args) {
        try {
            QueryRunner runner =new QueryRunner();
            BeanListHandler<T> bean =new BeanListHandler<>(clazz);
            return runner.query(conn, sql, bean, args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }
    //查询特殊的值通用方法
    public <E> E getValue(Connection conn, String sql, Object... args) {
        try {
            QueryRunner runner =new QueryRunner();
            ScalarHandler scalarHandler=new ScalarHandler();
            E e = (E) runner.query(conn, sql, scalarHandler);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }
}
