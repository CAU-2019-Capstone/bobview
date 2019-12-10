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
 * @author ckddn
 */
public class LoginOwner {
  
    private final String UNKNOWN_ID_STR = "존재하지 않는 ID입니다. 올바른 ID를 입력해주세요.";

    private Owner owner;
    
    private AES256Util aES256Util;
    private String id;
    private String pw;
    
    private Session s;
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private String SQL;
    private String SQL2;
    private ResultSet rs;
    private AppView m_App;
    private String url;
    private String db_user;
    private String db_pw;
     
    
    String driver = "com.mysql.jdbc.Driver";


    String owner_id;
    
   
    public LoginOwner(String id, String pw) {
        String rootPath = System.getProperty("user.dir");;
        System.out.println("현재 프로젝트의 경로 : "+rootPath );



        try {
            this.id = id;
            this.pw = pw;
            aES256Util = new AES256Util();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties prop = new Properties();
        
        try {
            prop.load(new FileInputStream("server.ini"));
        } catch (IOException ex) {
            Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        url = prop.getProperty("URL");
        db_user = prop.getProperty("ID");
        db_pw = prop.getProperty("PASSWORD");
        
    }
    
    public Owner login() throws GeneralSecurityException, UnsupportedEncodingException{
     
        SQL = "SELECT * FROM pos_owner";
        SQL2 = "UPDATE pos_owner SET is_active=true WHERE id=?";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);
            pstmt = con.prepareStatement(SQL);
           // pstmt.setString(1, id);
            
            rs = pstmt.executeQuery();
            //System.out.println("나와라 쿼리 " + rs.next());
            while(rs.next()){
                String owner_name = rs.getString("name");
                String owner_id = rs.getString("id");
                String owner_pw = aES256Util.decrypt(rs.getString("password"));
                String rest_name = rs.getString("restaurant_name");
                
                if (id.equals(owner_id) && pw.equals(owner_pw)){
                    
               
                    pstmt = con.prepareStatement(SQL2);
                    pstmt.setString(1, owner_id);
                    pstmt.executeUpdate();
                    
                    owner = new Owner(owner_id, owner_name, rest_name , true, true);
                }
            }
            System.out.println(owner_id);
     
            return owner;
        } catch (SQLException e) {
            
            System.out.println("SQL Error :" + e.getMessage());
            return null;
        } catch (ClassNotFoundException e1) {
            System.out.println("JDBC Driver Error: " + e1.getMessage() + "]");
            return null;
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
