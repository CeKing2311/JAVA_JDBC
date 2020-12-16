package com.ceking.preparedstatement;

import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*
 *@author ceking
 *@description 使用PreparedStatement 实现CRUD
 *@date 2020-12-16 14:13
 */
public class PreparedStatementTest {

    //通用的CRUD操作
    @Test
    public  void  updateTest(){
//        String sql="delete from customers where id=?";
//        update(sql,19);

        String sql= "update `order` set order_name = ? where order_id = ?";
        update(sql,"GG",4);
    }
    //通用的增删改操作
    public  void update(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i=0;i<args.length;i++){
             ps.setObject((i+1), args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }

    //向customers添加一条数据
    @Test
    public  void  test1() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.读取配置文件中的信息
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros= new Properties();
            pros.load(stream);
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");
            //2.加载驱动
            Class.forName(driverClass);
            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);

            //4.预编译sql语句
            String sql= "insert into customers(name,email,birth) values (?,?,?)";
            ps = connection.prepareStatement(sql);
            //5.填充占位符
            ps.setString(1, "明道");
            ps.setString(2, "mingdao@qq.com");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = dateFormat.parse("1967-01-12");
            ps.setDate(3, new java.sql.Date(dt.getTime()));
            //6.执行操作
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //7.资源关闭
            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    //修改customers的一条数据
    @Test
    public  void  test2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取一条连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句
            String sql = "update customers set name = ? where id = ?";
            //3.填充占位符
            ps = conn.prepareStatement(sql);
            ps.setString(1, "张三丰");
            ps.setInt(2, 19);
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }
}
