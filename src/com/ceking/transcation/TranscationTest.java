package com.ceking.transcation;

import com.ceking.bean.User;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
 *@author ceking
 *@description 事务
 *@date 2020-12-17 11:29
 */
public class TranscationTest {

    /**
     * 事务操作
     * AA用户给BB用户转账100
     */
    @Test
    public void test1() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //1.取消数据的自动提交功能
            conn.setAutoCommit(false);
            String sqlAA = "update user_table set balance = balance - 100 where user=?";
            updateTran(conn, sqlAA, "AA");
//            System.out.println(10 / 0);
            String sqlBB = "update user_table set balance = balance + 100 where user=?";
            updateTran(conn, sqlBB, "BB");
            System.out.println("转账成功!");
            //2.提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //3.回滚数据
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(conn, null);
        }
    }

    //通用的增删改操作 v1.0.0
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject((i + 1), args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }
    //带事务的增删改操作 v1.0.0
    public int updateTran(Connection conn, String sql, Object... args) {
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
    //******************设置隔离级别***********************
    /**
     * 通用的查询操作，考虑上事务
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
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
            JDBCUtils.closeResource(null, ps,res);
        }
        return  null;
    }

    @Test
    public  void  testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);

    }
    @Test
    public  void  testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //取消自动提交
        conn.setAutoCommit(false);
        String sql =" update user_table set balance = ? where user = ?";
        int cc = updateTran(conn, sql, 5000, "CC");
        Thread.sleep(10000);
        conn.commit();
        System.out.println("修改完成！");

    }




}
