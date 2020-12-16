package com.ceking.exer;

import com.ceking.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/*
 *@author ceking
 *@description
 *@date 2020-12-16 17:17
 */
public class ExerTest {
    public static void main(String[] args) {
        addStudent();
    }
    //向 examstudent表添加一条记录
    public static void  addStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级：");
        int type = scanner.nextInt();
        System.out.print("身份证号：");
        String IDCard = scanner.next();
        System.out.print("准考证号：");
        String examCard = scanner.next();
        System.out.print("学生姓名：");
        String name = scanner.next();
        System.out.print("所在城市：");
        String location = scanner.next();
        System.out.print("考试成绩：");
        int grade = scanner.nextInt();

        String sql = " insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";

        int count = update(sql, type, IDCard, examCard, name, location, grade);

        if (count>0){
            System.out.println("添加成功!");
        }else {
            System.out.println("添加失败!");
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
