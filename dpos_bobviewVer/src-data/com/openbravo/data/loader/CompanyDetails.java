//    dPOS  - Dongwun Point Of Sale
//    Copyright (c) 2009-2017 Dongwun & uniCenta & previous Openbravo POS works
//    http://www.dongwun.com
//
//    This file is part of dPOS
//
//    dPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   dPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with dPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.data.loader;


import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import java.io.*;

/**
 *
 * @author JG uniCenta
 */
public class CompanyDetails {
    private String db_url;
    private String db_user;       
    private String db_password;
    private File m_config;
    private Session session;

    /**
     *
     */
    public CompanyDetails() {          
   
       AppConfig config = new AppConfig(m_config);
    // AppViewConnection Session = new AppViewConnection();
    
    
}

    /**
     *
     * @param config
     */
    public void loadProperties(AppConfig config) {
         
        db_url=(config.getProperty("db.url"));
        db_user=(config.getProperty("db_user"));
        db_password=(config.getProperty("db.password"));
       //catch (BasicException){
         // Session s = new Session(db_url,db_user,"epos");
        
}

    /**
     *
     * @return
     */
    public String getUser() {
        return db_user;
    }
}





