package com.ceking.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

import org.junit.Test;

/*
 *@author ceking
 *@description
 *@date 2020-12-16 11:39
 */
public class ConnectionTest {

    //方式一:
    @Test
    public void test1() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();
        //jdbc:mysql ://协议
        //localhost :ip地址
        //3306:端口
        //test:数据库
        //?useUnicode=true&characterEncoding=utf8
        String url = "jdbc:mysql://localhost:3306/test";
        //将用户名和密码封装到Properties 中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "Goldcommon13579");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);

    }
    //方式二:
    @Test
    public void  test2() throws Exception {
        //获取Driver实现类的对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver instance = (Driver) aClass.newInstance();
        //2.提供需要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";
        //3.提供连接需要的用户名和密码
        Properties properties =new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password", "Goldcommon13579");
        //4.获取连接
        Connection connect = instance.connect(url, properties);
        System.out.println(connect);
    }
    //方式三：使用DriverManager 替换Driver
    @Test
    public  void  test3() throws Exception {
        //获取Driver实例类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.连接信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="Goldcommon13579";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式四：优化方式三
    @Test
    public  void  test4() throws Exception {
        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //1.连接信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="Goldcommon13579";
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五:将数据库连接需要的4个基本信息声明在配置文件中
    @Test
    public  void  test5() throws Exception {
        //1.读取配置文件中的信息
        InputStream stream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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

        System.out.println(connection);
    }

}
