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
package com.openbravo.pos.sales.restaurant;

import bsh.Interpreter;
import com.openbravo.pos.forms.StartPOS;
import bsh.EvalError;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JRootApp;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;




/**
 *
 * @author JG uniCenta
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JTicketsBagRestaurant extends javax.swing.JPanel {
    private static final long serialVersionUID = 7324436549490061312L;

    private AppView m_App;
    private JTicketsBagRestaurantMap m_restaurant;
    private List<TicketLineInfo> m_aLines;
    private TicketLineInfo line;
    private TicketInfo ticket;
    private Object ticketExt;
    private DataLogicSystem m_dlSystem = null;
    private DeviceTicket m_TP;
    private TicketParser m_TTP2;
    private RestaurantDBUtils restDB;

    private DataLogicSystem dlSystem = null;
    private TicketParser m_TTP;

    private SentenceList senttax;
    private ListKeyed taxcollection;
    private TaxesLogic taxeslogic;

    private Interpreter i;

    private ArrayList<String> tableList = new ArrayList<String>();
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
    private String SQL;
    private String SQL1;
    private String SQL2;
    private ResultSet rs;
    private ResultSet rs1;
    private String res_name;
    private String url;
    private String db_user;
    private String db_pw;
    
    String driver = "com.mysql.jdbc.Driver";
    

    /**
     * Creates new form JTicketsBagRestaurantMap
     *
     * @param app
     * @param restaurant
     */
    public JTicketsBagRestaurant(AppView app, JTicketsBagRestaurantMap restaurant) {
         
        super();

        Properties prop = new Properties();
        
        try {
            prop.load(new FileInputStream("server.ini"));
        } catch (IOException ex) {
            Logger.getLogger(JTicketsBagRestaurant.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        url = prop.getProperty("URL");
        db_user = prop.getProperty("ID");
        db_pw = prop.getProperty("PASSWORD");
        
        m_App = app;
        m_restaurant = restaurant;
        initComponents();
// Added by JDL for kitchen printer
        ticketExt = null;

        restDB = new RestaurantDBUtils(m_App);

        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TP = new DeviceTicket();
        m_TTP2 = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        m_KitchenPrint.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.PrintKitchen"));
        m_KitchenPrint.setVisible(false);

    }

    /**
     *
     */
    public void activate() {

        // Authorization
        m_DelTicket.setEnabled(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));

    }

    /**
     *
     * @param resource
     */
    public void printTicket(String resource) {
        printTicket(resource, ticket, m_restaurant.getTable());
    }

    private void printTicket(String sresourcename, TicketInfo ticket, String table) {
        if (ticket != null) {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                script.put("place", m_restaurant.getTableName());
                m_TTP2.printTicket(script.eval(m_dlSystem.getResourceAsXML(sresourcename)).toString());
            } catch (ScriptException | TicketPrinterException e) {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotprint"), e));
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_DelTicket = new javax.swing.JButton();
        m_MoveTable = new javax.swing.JButton();
        m_TablePlan = new javax.swing.JButton();
        m_KitchenPrint = new javax.swing.JButton();

        setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        setMinimumSize(new java.awt.Dimension(250, 50));
        setPreferredSize(new java.awt.Dimension(250, 50));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_DelTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/sale_delete.png"))); // NOI18N
        m_DelTicket.setToolTipText("현재 주문 삭제");
        m_DelTicket.setFocusPainted(false);
        m_DelTicket.setFocusable(false);
        m_DelTicket.setMargin(new java.awt.Insets(0, 4, 0, 4));
        m_DelTicket.setMaximumSize(new java.awt.Dimension(50, 40));
        m_DelTicket.setMinimumSize(new java.awt.Dimension(50, 40));
        m_DelTicket.setPreferredSize(new java.awt.Dimension(50, 40));
        m_DelTicket.setRequestFocusEnabled(false);
        m_DelTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_DelTicketActionPerformed(evt);
            }
        });
        add(m_DelTicket);

        m_MoveTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/movetable.png"))); // NOI18N
        m_MoveTable.setToolTipText("테이블 이동");
        m_MoveTable.setFocusPainted(false);
        m_MoveTable.setFocusable(false);
        m_MoveTable.setMargin(new java.awt.Insets(0, 4, 0, 4));
        m_MoveTable.setMaximumSize(new java.awt.Dimension(50, 40));
        m_MoveTable.setMinimumSize(new java.awt.Dimension(50, 40));
        m_MoveTable.setPreferredSize(new java.awt.Dimension(50, 40));
        m_MoveTable.setRequestFocusEnabled(false);
        m_MoveTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_MoveTableActionPerformed(evt);
            }
        });
        add(m_MoveTable);

        m_TablePlan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/tables.png"))); // NOI18N
        m_TablePlan.setToolTipText("테이블 플랜");
        m_TablePlan.setFocusPainted(false);
        m_TablePlan.setFocusable(false);
        m_TablePlan.setMargin(new java.awt.Insets(0, 4, 0, 4));
        m_TablePlan.setMaximumSize(new java.awt.Dimension(50, 40));
        m_TablePlan.setMinimumSize(new java.awt.Dimension(50, 40));
        m_TablePlan.setPreferredSize(new java.awt.Dimension(50, 40));
        m_TablePlan.setRequestFocusEnabled(false);
        m_TablePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_TablePlanActionPerformed(evt);
            }
        });
        add(m_TablePlan);

        m_KitchenPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/printer24.png"))); // NOI18N
        m_KitchenPrint.setToolTipText("주방 프린터로 전송");
        m_KitchenPrint.setMargin(new java.awt.Insets(0, 4, 0, 4));
        m_KitchenPrint.setMaximumSize(new java.awt.Dimension(50, 40));
        m_KitchenPrint.setMinimumSize(new java.awt.Dimension(50, 40));
        m_KitchenPrint.setPreferredSize(new java.awt.Dimension(50, 40));
        m_KitchenPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_KitchenPrintActionPerformed(evt);
            }
        });
        add(m_KitchenPrint);
    }// </editor-fold>//GEN-END:initComponents

    public void syncDeleteTable(String tableId) {
        SQL = "SELECT user_order_id FROM user_order WHERE table_id = ?;";
        SQL1 = "UPDATE user_order SET is_active=0 WHERE user_order_id = ?;";
       
        try {
            System.out.println("syncBobviewTable 삭제");
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);
            
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, tableId);
            rs = pstmt.executeQuery();  
           // tableList.clear();
           String user_order_id = null;
            while(rs.next()){           // sharedtickets에 있는 테이블 저장
                user_order_id = rs.getString("user_order_id");
            }
            pstmt1 = con.prepareStatement(SQL1);
            pstmt1.setString(1, user_order_id);
            pstmt1.executeUpdate();          
           
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
    
    private void m_MoveTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_MoveTableActionPerformed

// JG 6 Nov 13 - clear Customer from orignal table - Thanks David Kurniawan
        restDB.clearCustomerNameInTableById(m_restaurant.getTable());
// JG 6 Nov 13 - clear Waiter from orignal table - Thanks David Kurniawan
        restDB.clearWaiterNameInTableById(m_restaurant.getTable());
        restDB.setTableMovedFlag(m_restaurant.getTable());
        m_restaurant.moveTicket();

    }//GEN-LAST:event_m_MoveTableActionPerformed

    @SuppressWarnings("empty-statement")
    private void m_DelTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_DelTicketActionPerformed

        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {

            System.out.println("삭제하면 : " + m_restaurant.getTable());
            syncDeleteTable(m_restaurant.getTable());
            restDB.clearCustomerNameInTableById(m_restaurant.getTable());
            restDB.clearWaiterNameInTableById(m_restaurant.getTable());
            restDB.clearTicketIdInTableById(m_restaurant.getTable());
            m_restaurant.deleteTicket();
        }

    }//GEN-LAST:event_m_DelTicketActionPerformed

    private void m_TablePlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_TablePlanActionPerformed
        m_restaurant.newTicket();

    }//GEN-LAST:event_m_TablePlanActionPerformed

    @SuppressWarnings("empty-statement")
    private void m_KitchenPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_KitchenPrintActionPerformed
// This replaces the code from the buttons script

        ticket = m_restaurant.getActiveTicket();
        String rScript = (m_dlSystem.getResourceAsText("script.SendOrder"));

        Interpreter i = new Interpreter();
        try {
            i.set("ticket", ticket);
            i.set("place", m_restaurant.getTableName());
            i.set("user", m_App.getAppUserView().getUser());
            i.set("sales", this);
            Object result = i.eval(rScript);
        } catch (EvalError ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Autologoff after sales            
        String autoLogoff = (m_App.getProperties().getProperty("till.autoLogoff"));
        String autoLogoffRestaurant = (m_App.getProperties().getProperty("till.autoLogoffrestaurant"));
        if (autoLogoff != null) {
            if (autoLogoff.equals("true")) {
                // check how far to logoof to ie tables or application
                if (autoLogoffRestaurant == null) {
                    ((JRootApp) m_App).closeAppView();
                } else if (autoLogoffRestaurant.equals("true")) {
                    m_restaurant.newTicket();
                } else {
                    ((JRootApp) m_App).closeAppView();
                };
            }
        }
    }//GEN-LAST:event_m_KitchenPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton m_DelTicket;
    private javax.swing.JButton m_KitchenPrint;
    private javax.swing.JButton m_MoveTable;
    private javax.swing.JButton m_TablePlan;
    // End of variables declaration//GEN-END:variables

}
