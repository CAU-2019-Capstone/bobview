/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.bobview;

import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppView;
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

public class RegistOwner {
   
    private AES256Util aES256Util;
    private String name;
    private String id;
    private String pw;
    private String restaurant;

    
    private Session s;
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private String SQL;
    private ResultSet rs;
    private AppView m_App;
    private String url;
    private String db_user;
    private String db_pw; 
    
    
    String driver = "com.mysql.jdbc.Driver";
    
    String user_id;

    public RegistOwner(String name, String id, String pw, String restaurant) {
        try {
            this.aES256Util = new AES256Util();
            this.name = name;
            this.id = id;
            this.pw = aES256Util.encrypt(pw);
            this.restaurant = restaurant;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
                Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties prop = new Properties();
        
   
        try {
            prop.load(new FileInputStream("server.ini"));
        } catch (IOException ex) {
            Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    

        
        url = prop.getProperty("URL");
        db_user = prop.getProperty("ID");
        db_pw = prop.getProperty("PASSWORD");
        
    }
    
    public boolean regist() {
     
        SQL = "INSERT INTO pos_owner(name,id,password,restaurant_name) VALUES (?, ?, ?, ?)";
          
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, pw);
            pstmt.setString(4, restaurant);
            
            int r = pstmt.executeUpdate();

            return true;
   
        } catch (SQLException e) {
            System.out.println("SQL Error :" + e.getMessage());
            return false;
        } catch (ClassNotFoundException e1) {
            System.out.println("JDBC Driver Error: " + e1.getMessage() + "]");
            return false;
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
