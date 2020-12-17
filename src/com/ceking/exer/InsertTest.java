package com.ceking.exer;

import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/*
 *@author ceking
 *@description 使用PreparedStatement 实现批量数据的操作
 * update、delete 本身就具有批量操作的效果
 * insert 批量添加数据
 * 方式一：
 * CREATE TABLE goods(
  id INT PRIMARY KEY AUTO_INCREMENT,
  NAME VARCHAR(50),
  CODE VARCHAR(50)
    );
* 向Goods中插入2万条数据
* 1.1
 *@date 2020-12-17 10:11
 */
public class InsertTest {

    //批量操作方式一
    //使用Statement ,存在sql注入风险，拼串操作频繁
    @Test
    public  void  testInsertBatch(){
        Connection conn = null;
        Statement statement = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            statement = conn.createStatement();
            for (int i=1;i<=20000;i++){
                String sql ="insert into goods(name,code) values("+i+","+i+")";
                statement.execute(sql);
            }
            long end = System.currentTimeMillis();
            System.out.println(end -start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, statement);
        }
    }

    //批量操作方式二
    //使用PreparedStatement
    @Test
    public  void  testInsertBatch1(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql =" insert into goods(name,code) values(?,?)";
            ps = conn.prepareStatement(sql);
            for (int i=1 ;i<=20000;i++){
                ps.setObject(1, i);
                ps.setObject(2, i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
    //批量操作方式三
    // 1.addBacth、executeBatch、clearBatch
    // 2.url=jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
    @Test
    public  void  testInsertBatch2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql =" insert into goods(name,code) values(?,?)";
            ps = conn.prepareStatement(sql);
            for (int i=1 ;i<=1000000;i++){
                ps.setObject(1,"name_"+i);
                ps.setObject(2, "code_"+i);
                //ps.execute();
                //1.批量添加
                ps.addBatch();
                if (i%500==0){
                    //2.批量执行
                    ps.executeBatch();
                    //3.批量清空
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }

    }
    //批量操作方式四（最高效）
    //设置不允许自动提交 conn.setAutoCommit(false);
    //数据写入完成后再提交 conn.commit();
    @Test
    public  void  testInsertBatch3(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            //设置不允许自动提交
            conn.setAutoCommit(false);
            String sql =" insert into goods(name,code) values(?,?)";
            ps = conn.prepareStatement(sql);
            for (int i=1 ;i<=1000000;i++){
                ps.setObject(1,"name_"+i);
                ps.setObject(2, "code_"+i);
                //ps.execute();
                //1.批量添加
                ps.addBatch();
                if (i%500==0){
                    //2.批量执行
                    ps.executeBatch();
                    //3.批量清空
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }

    }


}
