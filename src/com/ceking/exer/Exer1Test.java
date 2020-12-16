package com.ceking.exer;

import com.ceking.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/*
 *@author ceking
 *@description
 *@date 2020-12-16 16:51
 */
public class Exer1Test {

    public static void main(String[] args) {
        //添加操作
        addCustomer();
    }
    public static void  addCustomer() {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.print("请输入姓名：");
            String name =  scan.nextLine();
            System.out.print("请输入邮箱：");
            String email =  scan.nextLine();
            System.out.print("请输入生日(yyyy-MM-dd)：");
            String birthday =  scan.nextLine();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date dt = dateFormat.parse(birthday);
//            java.sql.Date birth = new java.sql.Date(dt.getTime());
            String sql = "insert into customers(name,email,birth) values (?,?,?) ";
            int count = update(sql, name, email, birthday);
            if (count>0){
                System.out.println("添加成功!");
            }else {
                System.out.println("添加失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通用的增删改操作
    public static int update(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i=0;i<args.length;i++){
                ps.setObject((i+1), args[i]);
            }
            //执行
            /**
             *ps.execute() 如果执行的是查询操作，返回true
             * 如果执行的是增删改操作，返回false
             */
            //ps.execute();
            int i = ps.executeUpdate();
            return i;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
        return  0;
    }
}
