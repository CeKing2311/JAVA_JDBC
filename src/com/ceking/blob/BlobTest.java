package com.ceking.blob;

import com.ceking.bean.Customers;
import com.ceking.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/*
 *@author ceking
 *@description 使用 PreparedStatement 操作blob类型的数据
 *@date 2020-12-17 9:42
 */
public class BlobTest {

    //向customer中插入Blob类型的字段

    @Test
    public  void  testInsert(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql="insert  into customers(name,email,birth,photo) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "张三丰");
            ps.setObject(2, "zhangsanfeng@qq.com");
            ps.setObject(3, "1546-04-04");
            FileInputStream fs= new FileInputStream(new File("2.jpg"));
            ps.setObject(4, fs);
            boolean execute = ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }

    }

    @Test
    public  void  testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        InputStream is =null;
        FileOutputStream fos =null;
        try {
            conn = JDBCUtils.getConnection();
            String sql =" select id,name,email,birth,photo from customers where id =? ";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,16);

            res = ps.executeQuery();
            if (res.next()){
                //方式1：
    //            int id = res.getInt(1);
    //            String name = res.getString(2);
    //            String email = res.getString(3);
    //            Date birth = res.getDate(4);
                //方式2：
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                Date birth = res.getDate("birth");
                Customers cus= new Customers(id, name, email, birth);
                System.out.println(cus);

                //將Blob类型的字段下载下来，保存到本地
                Blob photo = res.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("girl.jpg");
                byte[] buffer =new  byte[1024];
                int len;
                while ((len = is.read(buffer))!=-1){
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, ps,res);
        }

    }
}
