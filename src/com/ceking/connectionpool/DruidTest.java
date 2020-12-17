package com.ceking.connectionpool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/*
 *@author ceking
 *@description
 *@date 2020-12-17 16:32
 */
public class DruidTest {

    @Test
    public  void  getConnection() throws Exception {
        Properties prop =new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        prop.load(is);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
        Connection conn = dataSource.getConnection();
        System.out.println(conn);
    }
}
