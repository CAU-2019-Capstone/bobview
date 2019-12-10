//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (C) 2008-2013 Openbravo, S.L.
//    http://www.unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>
package com.openbravo.pos.forms;

import com.openbravo.pos.forms.bobview.LoginFrame;
import com.openbravo.pos.forms.bobview.AddMenuInfo;
import com.openbravo.format.Formats;
import com.openbravo.pos.instance.InstanceQuery;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import com.openbravo.pos.ticket.TicketInfo;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import javax.swing.JFrame;
import java.io.IOException;
import javax.swing.UIDefaults;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.FontSets;
import java.util.Properties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.util.Properties;
// JG 16 May 2013 deprecated for pushingpixels
// import org.jvnet.substance.SubstanceLookAndFeel;
// import org.jvnet.substance.api.SubstanceSkin;
/**
 *
 * @author adrianromero
 */
public class StartPOS {

    private static final Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");
    public static Font gblfont = new Font("SansSerif", Font.PLAIN, 12);
    public String res_name = null;
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private String SQL;
    private String SQL2;
    private ResultSet rs;
    private AppView m_App;
    

    /**
     * Creates a new instance of StartPOS
     */
    private StartPOS() {
    }

    /**
     *
     * @return
     */
    public static boolean registerApp() {

        // vemos si existe alguna instancia        
        InstanceQuery i = null;
        try {
            i = new InstanceQuery();
            i.getAppMessage().restoreWindow();
            return false;
// JG 6 May 2013 to Multicatch
        } catch (RemoteException | NotBoundException e) {
            return true;
        }
    }

    /**
     *
     * @param args
     */
    public static void main(final String args[]) throws FileNotFoundException {
        

        
        /* login frame start */
        System.out.println("startPos service");
        LoginFrame frame = new LoginFrame();
        AddMenuInfo addMenuInfo = new AddMenuInfo();
        frame.setVisible(true);
        while(true){
            boolean flag = frame.isSignIn();
            System.out.print("");
            if(flag){
                frame.writeRestInfo();
                
                addMenuInfo.sync();
                break;
            }
        } 
             /*   login frame end */
             
     
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                if (!registerApp()) {
                    System.exit(1);
                }

                AppConfig config = new AppConfig(args);
                config.load();

                // set Locale.
                String slang = config.getProperty("user.language");
                String scountry = config.getProperty("user.country");
                String svariant = config.getProperty("user.variant");
                if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
                    Locale.setDefault(new Locale(slang, scountry, svariant));
                }

                // Set the format patterns
                Formats.setIntegerPattern(config.getProperty("format.integer"));
                Formats.setDoublePattern(config.getProperty("format.double"));
                Formats.setCurrencyPattern(config.getProperty("format.currency"));
                Formats.setPercentPattern(config.getProperty("format.percent"));
                Formats.setDatePattern(config.getProperty("format.date"));
                Formats.setTimePattern(config.getProperty("format.time"));
                Formats.setDateTimePattern(config.getProperty("format.datetime"));
                
                try {
                    gblfont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("locales/NanumGothic.ttf") );
                } catch (IOException|FontFormatException e) {
                    // Handle exception
                }

                // Set the look and feel.
                try {

                    Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();
                    if (laf instanceof LookAndFeel) {
                        UIManager.setLookAndFeel((LookAndFeel) laf);
                        setUIFont (new javax.swing.plaf.FontUIResource(gblfont.deriveFont(0, 12)));
                    } else if (laf instanceof SubstanceSkin) {
                        SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
                        setSubstanceFont();
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    logger.log(Level.WARNING, "Cannot set Look and Feel", e);
                }

                String hostname = config.getProperty("machine.hostname");
                TicketInfo.setHostname(hostname);

                String screenmode = config.getProperty("machine.screenmode");
                if ("전체화면".equals(screenmode)) {
                    JRootKiosk rootkiosk = new JRootKiosk();
                    rootkiosk.initFrame(config);
                } else {
                    JRootFrame rootframe = new JRootFrame();
                    rootframe.initFrame(config);
                }
            }
        });
    }
    
    public static Font getgblFont() {
        return gblfont;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get (key);
          if (value != null && value instanceof javax.swing.plaf.FontUIResource)
            UIManager.put (key, f);
        }
    } 
    
    public static void setSubstanceFont (){
          SubstanceLookAndFeel.setFontPolicy(new FontPolicy() { 
            public FontSet getFontSet(String lafName, UIDefaults table){
                Font controlFont=gblfont.deriveFont(0, 12);
                Font menuFont=controlFont;
                Font titleFont=controlFont.deriveFont(Font.BOLD);
                return FontSets.createDefaultFontSet(controlFont,menuFont,titleFont);
            }
          });
    } 
    
}
