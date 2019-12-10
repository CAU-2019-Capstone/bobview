/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.bobview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author KSJ
 */
public class AddMenuInfo {
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
    private PreparedStatement pstmt3;
    private PreparedStatement pstmt4;
    private String SQL;
    private String SQL1;
    private String SQL2;
    private String SQL3;
    private String SQL4;
    private ResultSet rs;
    private String res_name;
    private String url;
    private String db_user;
    private String db_pw;
    
    
    String driver = "com.mysql.jdbc.Driver";
    
    public AddMenuInfo(){
             Properties prop = new Properties();
       
             try {
                 prop.load(new FileInputStream("server.ini"));
             } catch (IOException ex) {
                 Logger.getLogger(AddMenuInfo.class.getName()).log(Level.SEVERE, null, ex);
             }

    url = prop.getProperty("URL");
    db_user = prop.getProperty("ID");
    db_pw = prop.getProperty("PASSWORD");

    }

    
    public void sync() {    // 해당 매장 메뉴 포스로 동기화
        try{
            FileReader fr = null;
            File file = new File("restInfo");
            fr = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fr);
            String rest_name = bufReader.readLine();
            res_name = rest_name;
            System.out.println("매장 이름 : " + res_name);
            bufReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }catch(IOException e){
            System.out.println(e);
        }
     
        SQL = "SELECT menu_id, menu_name, menu_price FROM menu_info WHERE restaurant_id=?";        
        SQL1 = "INSERT INTO PRODUCTS (id, reference, code, name, pricesell, display, category, taxcat) values (?,?,?,?,?,?, '000','000')";
                                                                                              //menu_id, menu_id, menu_id, menu_name, menu_price, menu_name
        SQL2 = "INSERT INTO PRODUCTS_CAT (product) values (?)"; // menu_id

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);

            
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, res_name);
     
            rs = pstmt.executeQuery();
            while(rs.next()){
                String menu_id = rs.getString("menu_id");
                String menu_name = rs.getString("menu_name");
                String menu_price = rs.getString("menu_price");
                System.out.println("메뉴id : " + menu_id + "이름 : " + menu_name + "가격 : " + menu_price);
                pstmt1 = con.prepareStatement(SQL1);
                pstmt1.setString(1, menu_id);
                pstmt1.setString(2, menu_id);
                pstmt1.setString(3, menu_id);
                pstmt1.setString(4, menu_name);
                pstmt1.setString(5, menu_price);
                pstmt1.setString(6, menu_name);
                pstmt1.executeUpdate();                    
                pstmt2 = con.prepareStatement(SQL2);
                pstmt2.setString(1, menu_id);
                pstmt2.executeUpdate();                 
            }
           
        } catch (SQLException e) {
            System.out.println("SQL Error :" + e.getMessage());
        } catch (ClassNotFoundException e1) {
            System.out.println("JDBC Driver Error: " + e1.getMessage() + "]");
        } finally {
            if (rs != null){
                try{
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
