package com.ceking.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
 *@author ceking
 *@description 获取数据库连接
 *@date 2020-12-16 14:30
 */
public class JDBCUtils {

    private  static  DataSource dataSource;
    static {
        try {
            Properties prop =new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            prop.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取数据库连接
     * @return
     * @throws Exception
     */
    public  static Connection getConnection() throws Exception {
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
        Connection connection = DriverManager.getConnection(url, user, password);
        return  connection;
    }

    /**
     * 通过druid获取连接池
     * @return
     * @throws Exception
     */
    public  static Connection getDruidConnection() throws Exception {
        Connection conn = dataSource.getConnection();
        return  conn;
    }

    /**
     * 关闭资源的操作
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps){
        if (ps!=null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源
     * @param conn
     * @param ps
     * @param set
     */
    public static void closeResource(Connection conn, Statement ps,ResultSet set){
        if (ps!=null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (set!=null){
            try {
                set.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
