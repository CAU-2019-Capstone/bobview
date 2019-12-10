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

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.NullIcon;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.TaxInfo;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import static java.lang.Thread.sleep;
import java.util.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.FontUIResource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author JG uniCenta
 */

import javax.swing.UIManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JTicketsBagRestaurantMap extends JTicketsBag {
    private static final long serialVersionUID = -3688599503324333082L;

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
    private String call_userorder_id = null;
    private String call_table = null;
    private String call_message = null;
    public static int check = 0;
    public static String table_id = null;
    
    public static JLabel tableLabel = new JLabel();
    
    private String url;
    private String db_user;
    private String db_pw;
    
    String driver = "com.mysql.jdbc.Driver";
    
    
    /**
     *
     */
    public static void newticket() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void internalFrame(String 알림창, boolean b, boolean b0, boolean b1, boolean b2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private static class ServerCurrent {

        public ServerCurrent() {
        }
    }

//    private static final Icon ICO_OCU = new ImageIcon(JTicketsBag.class.getResource("/com/openbravo/images/edit_group.png"));
//    private static final Icon ICO_FRE = new NullIcon(22, 22);
    private java.util.List<Place> m_aplaces;
    private java.util.List<Floor> m_afloors;
    private ArrayList<String> tableList = new ArrayList<String>();
    private ArrayList<String> newtableList = new ArrayList<String>();
    private JTicketsBagRestaurant m_restaurantmap;
    private JTicketsBagRestaurantRes m_jreservations;

    private Place m_PlaceCurrent;

// TODO - Add Server JG 03.07.2011
    private ServerCurrent m_ServerCurrent;

    // State vars
    private Place m_PlaceClipboard;
    private CustomerInfo customer;

    private DataLogicReceipts dlReceipts = null;
    private DataLogicSales dlSales = null;
    private final RestaurantDBUtils restDB;
    private static final Icon ICO_OCU_SM = new ImageIcon(Place.class.getResource("/com/openbravo/images/edit_group_sm.png"));
    private static final Icon ICO_WAITER = new NullIcon(1, 1);
    private static final Icon ICO_FRE = new NullIcon(22, 22);
    private String waiterDetails;
    private String customerDetails;
    private String tableName;

    /**
     * Creates new form JTicketsBagRestaurant
     *
     * @param app
     * @param panelticket
     */
    public JTicketsBagRestaurantMap(AppView app, TicketsEditor panelticket) {

        super(app, panelticket);

        Properties prop = new Properties();
        

        try {
            prop.load(new FileInputStream("server.ini"));
        } catch (IOException ex) {
            Logger.getLogger(JTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        url = prop.getProperty("URL");
        db_user = prop.getProperty("ID");
        db_pw = prop.getProperty("PASSWORD");
        restDB = new RestaurantDBUtils(app);

        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

        m_restaurantmap = new JTicketsBagRestaurant(app, this);
        m_PlaceCurrent = null;
        m_PlaceClipboard = null;
        customer = null;

        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    "SELECT ID, NAME, IMAGE FROM FLOORS ORDER BY NAME",
                    null,
                    new SerializerReadClass(Floor.class));
            m_afloors = sent.list();

        } catch (BasicException eD) {
            m_afloors = new ArrayList<>();
        }
        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    // "SELECT ID, NAME, X, Y, FLOOR, CUSTOMER FROM PLACES ORDER BY FLOOR", 
                    "SELECT ID, NAME, X, Y, FLOOR, CUSTOMER, WAITER, TICKETID, TABLEMOVED FROM PLACES ORDER BY FLOOR",
                    null,
                    new SerializerReadClass(Place.class));
            m_aplaces = sent.list();
        } catch (BasicException eD) {
            m_aplaces = new ArrayList<>();
        }

        initComponents();
       
        // add the Floors containers
        if (m_afloors.size() > 1) {
            // A tab container for 2 or more floors
            JTabbedPane jTabFloors = new JTabbedPane();
            jTabFloors.applyComponentOrientation(getComponentOrientation());
            jTabFloors.setBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)));
            jTabFloors.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            jTabFloors.setFocusable(false);
            jTabFloors.setRequestFocusEnabled(false);
            m_jPanelMap.add(jTabFloors, BorderLayout.CENTER);

            for (Floor f : m_afloors) {
                f.getContainer().applyComponentOrientation(getComponentOrientation());

                JScrollPane jScrCont = new JScrollPane();
                jScrCont.applyComponentOrientation(getComponentOrientation());
                JPanel jPanCont = new JPanel();
                jPanCont.applyComponentOrientation(getComponentOrientation());

                jTabFloors.addTab(f.getName(), f.getIcon(), jScrCont);
                jScrCont.setViewportView(jPanCont);
                jPanCont.add(f.getContainer());
            }
        } else if (m_afloors.size() == 1) {
            // Just a frame for 1 floor
            Floor f = m_afloors.get(0);
            f.getContainer().applyComponentOrientation(getComponentOrientation());

            JPanel jPlaces = new JPanel();
            jPlaces.applyComponentOrientation(getComponentOrientation());
            jPlaces.setLayout(new BorderLayout());
            jPlaces.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)),
                    new javax.swing.border.TitledBorder(f.getName())));

            JScrollPane jScrCont = new JScrollPane();
            jScrCont.applyComponentOrientation(getComponentOrientation());
            JPanel jPanCont = new JPanel();
            jPanCont.applyComponentOrientation(getComponentOrientation());

            // jPlaces.setLayout(new FlowLayout());           
            m_jPanelMap.add(jPlaces, BorderLayout.CENTER);
            jPlaces.add(jScrCont, BorderLayout.CENTER);
            jScrCont.setViewportView(jPanCont);
            jPanCont.add(f.getContainer());
        }

        // Add all the Table buttons.
        Floor currfloor = null;

        for (Place pl : m_aplaces) {
            int iFloor = 0;

            if (currfloor == null || !currfloor.getID().equals(pl.getFloor())) {
                // Look for a new floor
                do {
                    currfloor = m_afloors.get(iFloor++);
                } while (!currfloor.getID().equals(pl.getFloor()));
            }

            currfloor.getContainer().add(pl.getButton());
            pl.setButtonBounds();
            pl.getButton().addActionListener(new MyActionListener(pl));
        }

        // Add the reservations panel
        m_jreservations = new JTicketsBagRestaurantRes(app, this);
        add(m_jreservations, "res");
    }

    /**
     *
     */
    @Override
    public void activate() {

        // precondicion es que no tenemos ticket activado ni ticket en el panel
        m_PlaceClipboard = null;
        customer = null;

        syncBobviewTable();
     
        loadTickets();
        printState();

        m_panelticket.setActiveTicket(null, null);
        m_restaurantmap.activate();

        showView("map"); // arrancamos en la vista de las mesas.

        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }

    /**
     *
     * @return
     */
    @Override
    public boolean deactivate() {

        // precondicion es que tenemos ticket activado aqui y ticket en el panel
        if (viewTables()) {

            // borramos el clipboard
            m_PlaceClipboard = null;
            customer = null;
            
            // guardamos el ticket
            if (m_PlaceCurrent != null) {

                try {
                    dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                }

                m_PlaceCurrent = null;
            }

            // desactivamos cositas.
            printState();
            m_panelticket.setActiveTicket(null, null);

            return true;
        } else {
            return false;
        }

        // postcondicion es que no tenemos ticket activado
    }

    /**
     *
     * @return
     */
    @Override
    protected JComponent getBagComponent() {
        return m_restaurantmap;
    }

    /**
     *
     * @return
     */
    @Override
    protected JComponent getNullComponent() {
        return this;
    }

    /**
     *
     * @return
     */
    public TicketInfo getActiveTicket() {
        return m_panelticket.getActiveTicket();
    }

    /**
     *
     */
    public void moveTicket() {

        // guardamos el ticket
        if (m_PlaceCurrent != null) {

            try {
                dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }

            // me guardo el ticket que quiero copiar.
            m_PlaceClipboard = m_PlaceCurrent;

            customer = null;
            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean viewTables(CustomerInfo c) {
        // deberiamos comprobar si estamos en reservations o en tables...
        if (m_jreservations.deactivate()) {
            showView("map"); // arrancamos en la vista de las mesas.

            m_PlaceClipboard = null;
            customer = c;
            printState();

            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public boolean viewTables() {
        return viewTables(null);
    }

    /**
     *
     */
    public void newTicket() {

        // guardamos el ticket
        if (m_PlaceCurrent != null) {

            try {
                dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
            } catch (BasicException e) {
                new MessageInf(e).show(this); // maybe other guy deleted it
            }

            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

    /**
     *
     * @return
     */
    public String getTable() {
        String id = null;
        if (m_PlaceCurrent != null) {
            id = m_PlaceCurrent.getId();
        }
        return (id);
    }

    /**
     *
     * @return
     */
    public String getTableName() {
        String tableName = null;
        if (m_PlaceCurrent != null) {
            tableName = m_PlaceCurrent.getName();
        }
        return (tableName);
    }

    /**
     *
     */
    @Override
    public void deleteTicket() {

        if (m_PlaceCurrent != null) {

            String id = m_PlaceCurrent.getId();
            try {
                dlReceipts.deleteSharedTicket(id);
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }

            m_PlaceCurrent.setPeople(false);

            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

// Added JG 03.07.2011 - TODO - Change Server Dialog here
    /**
     *
     */
    public void changeServer() {

        if (m_ServerCurrent != null) {

//          Show list of Users
//          Allow Users - CurrentUsers select
//          Compare Users
//          If newServer equal.currentUser
//              Msg NoChange
//          else
//              m_ServerCurrent.setPeople(newServer);
//              Msg Changed to NewServer
        }
    }

    /**
     *
     */
      
    public void syncBobviewMessage() {
        SQL = "SELECT id, table_id, message FROM messages WHERE restaurant_id = ?;";
        SQL1 = "DELETE FROM messages WHERE id = ?;";
        
        try{
            FileReader fr = null;
            File file = new File("restInfo");
            fr = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fr);
            String rest_name = bufReader.readLine();
            res_name = rest_name;
            System.out.println("메세지 매장 : " + res_name);
            bufReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }catch(IOException e){
            System.out.println(e);
        }

        
        try {
            
            System.out.println("호출메세지 알림");
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);                  
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, res_name);
            rs = pstmt.executeQuery();   
            while(rs.next()){         
                call_table = rs.getString("table_id");
                call_message = rs.getString("message");    
                System.out.println(call_message);
            }
            rs.close();
            pstmt.close();
            
              
            
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, res_name);
            rs = pstmt.executeQuery();  
            int message_id = 0;
            while(rs.next()){  
                message_id = Integer.parseInt(rs.getString("id"));
                pstmt1 = con.prepareStatement(SQL1);                
                pstmt1.setInt(1, message_id);
                pstmt1.executeUpdate();
                JOptionPane.showMessageDialog(null, "0000", "호출 테이블 : " + call_table ,  JOptionPane.INFORMATION_MESSAGE);   
                                
            }
            rs.close();
            pstmt.close();
                   
           
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
    @SuppressWarnings("deprecation")
    public void syncBobviewTable() {
        SQL = "SELECT id FROM SHAREDTICKETS;";
        SQL1 = "SELECT table_id FROM user_order WHERE is_active=1;"; // is_active = 1
        SQL2 = "INSERT INTO SHAREDTICKETS(ID, NAME, CONTENT, PICKUPID) VALUES (?, '점장 - (10:44 640)', 'content', 0);";

        try {
            System.out.println("syncBobviewTable 디비 연동");
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);
            
            pstmt = con.prepareStatement(SQL);
            rs = pstmt.executeQuery();  
            tableList.clear();
            //newtableList.clear();
            while(rs.next()){           // sharedtickets에 있는 테이블 저장
                String table_id = rs.getString("id");
                tableList.add(table_id);
                System.out.println("sharedtickets id : " + table_id);   
                
            }
            pstmt1 = con.prepareStatement(SQL1);
            rs = pstmt1.executeQuery();          
            String alarmTable = null;
            while(rs.next()){       // user_order에 있는 테이블 비교, sharedtickets에 삽입
                table_id = rs.getString("table_id");
                if(!tableList.contains(table_id)){
                    alarmTable = table_id;
                    tableList.add(table_id);
                    newtableList.add(table_id);
                    System.out.println("tableid : " + table_id);
                    pstmt2 = con.prepareStatement(SQL2);        
                    pstmt2.setString(1, table_id);
                    pstmt2.executeUpdate();  
                    String tableId = "테이블 번호 : " + table_id;
                    tableLabel = new JLabel(tableId);  
                    // m_timer.cancel();
                    JLabel tableLabel = new JLabel(tableId);
                    tableLabel.setFont(new Font("돋움", Font.BOLD, 40));  
                    JOptionPane.showMessageDialog(null, tableLabel, "테이블 번호 : " + table_id,  JOptionPane.INFORMATION_MESSAGE);           
                }
            }        
            syncBobviewMessage();         
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
    
    public void syncBobviewTableInfo(TicketInfo ticket, String selectedTable){
        SQL = "SELECT user_order_id FROM user_order WHERE table_id= ? and is_active=1";
        SQL1 = "SELECT menu_id, menu_num FROM order_contents WHERE userorder_id = ?";      
        SQL2 = "SELECT menu_name, menu_price FROM menu_info WHERE menu_id = ?";
       
        try {
            System.out.println("syncBobviewTableInfo 주문정보 연동");
            Class.forName(driver);
            con = DriverManager.getConnection(url, db_user, db_pw);
            
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, selectedTable);
            rs = pstmt.executeQuery();  
            String user_order_id = null;
            while(rs.next()){           // sharedtickets에 있는 테이블 저장
                user_order_id = rs.getString("user_order_id");               
            }
            pstmt1 = con.prepareStatement(SQL1);          
            pstmt1.setString(1, user_order_id);
            rs = pstmt1.executeQuery();
            String menu_id = null;
            double menu_num = 0;
            String restaurant_id = null;
            String menu_name = null;
            double menu_price = 0;
            int line = 0;
            int taxid = 0;
            while(rs.next()){       // user_order에 있는 테이블 비교, sharedtickets에 삽입
                menu_id = rs.getString("menu_id");
                menu_num = Double.parseDouble(rs.getString("menu_num"));
                
                pstmt2 = con.prepareStatement(SQL2);
                pstmt2.setString(1, menu_id);
             
                rs1 = pstmt2.executeQuery(); 
                while(rs1.next()){
                    menu_name = rs1.getString("menu_name");
                    menu_price = Double.parseDouble(rs1.getString("menu_price"));
                }              
               TaxInfo tax = new TaxInfo(Integer.toString(taxid), "면세", "000", "000", null, 0.0, true, taxid);     
               TicketLineInfo ticketline;   
              
               ticketline = new TicketLineInfo(menu_name, menu_id, menu_num, menu_price, tax);
               ticket.insertLine(line,ticketline);
     
               if(ticketline.getTaxInfo() == null){
                   System.out.println("tax 정보 없음");
               }
               else{
                   System.out.println("tax 정보 있음");
               }               
               line++;
               taxid++;
               System.out.println("메뉴리스트 성공!");
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
       
    public void loadTickets() {

        Set<String> atickets = new HashSet<>();

        try {
            java.util.List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();
            for (SharedTicketInfo ticket : l) {
                atickets.add(ticket.getId());
            }
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }

        for (Place table : m_aplaces) {
            table.setPeople(atickets.contains(table.getId()));
        }
    }

    private void printState() {

        if (m_PlaceClipboard == null) {
            if (customer == null) {
                // Select a table
                m_jText.setText(null);
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(true);
// get the customer details form the database
// We have set the option show details on table.   
                    if (m_App.getProperties().getProperty("table.tablecolour") == null) {
                        tableName = "<style=font-size:9px;font-weight:bold;><font color = black>" + place.getName() + "</font></style>";
                    } else {
                        tableName = "<style=font-size:9px;font-weight:bold;><font color =" + m_App.getProperties().getProperty("table.tablecolour") + ">" + place.getName() + "</font></style>";
                    }

                    if (Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue()) {
                        if (m_App.getProperties().getProperty("table.waitercolour") == null) {
                            waiterDetails = (restDB.getWaiterNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color = red>"
                                    + restDB.getWaiterNameInTableById(place.getId()) + "</font></style><br>";
                        } else {
                            waiterDetails = (restDB.getWaiterNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color ="
                                    + m_App.getProperties().getProperty("table.waitercolour") + ">" + restDB.getWaiterNameInTableById(place.getId()) + "</font></style><br>";
                        }                    
                        place.getButton().setIcon(ICO_OCU_SM);
                    } else {
                        waiterDetails = "";
                    }

                    if (Boolean.valueOf(m_App.getProperties().getProperty("table.showcustomerdetails")).booleanValue()) {
                        place.getButton().setIcon((Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue() && (restDB.getCustomerNameInTable(place.getName()) != null)) ? ICO_WAITER : ICO_OCU_SM);
                        if (m_App.getProperties().getProperty("table.customercolour") == null) {
                            customerDetails = (restDB.getCustomerNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color = blue>"
                                    + restDB.getCustomerNameInTableById(place.getId()) + "</font></style><br>";
                        } else {
                            customerDetails = (restDB.getCustomerNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color ="
                                    + m_App.getProperties().getProperty("table.customercolour") + ">" + restDB.getCustomerNameInTableById(place.getId()) + "</font></style><br>";
                        }
                    } else {
                        customerDetails = "";
                    }

                    if ((Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue())
                            || (Boolean.valueOf(m_App.getProperties().getProperty("table.showcustomerdetails")).booleanValue())) {
                        place.getButton().setText("<html><center>" + customerDetails + waiterDetails + tableName + "</html>");
//  JG 29 Aug 13 Bug fix }else{;
                    } else {
                        if (m_App.getProperties().getProperty("table.tablecolour") == null) {
                            tableName = "<style=font-size:10px;font-weight:bold;><font color = black>" + place.getName() + "</font></style>";
                        } else {
                            tableName = "<style=font-size:10px;font-weight:bold;><font color =" + m_App.getProperties().getProperty("table.tablecolour") + ">" + place.getName() + "</font></style>";
                        }

                        place.getButton().setText("<html><center>" + tableName + "</html>");

                    }
                    if (!place.hasPeople()) {                       
                        place.getButton().setIcon(ICO_FRE);
                    }
                }

                m_jbtnReservations.setEnabled(true);
            } else {
                // receive a customer
                m_jText.setText(AppLocal.getIntString("label.restaurantcustomer", new Object[]{customer.getName()}));
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(!place.hasPeople());
                }
                m_jbtnReservations.setEnabled(false);
            }
        } else {
            // Moving or merging the receipt to another table
            m_jText.setText(AppLocal.getIntString("label.restaurantmove", new Object[]{m_PlaceClipboard.getName()}));
            // Enable all empty tables and origin table.
            for (Place place : m_aplaces) {
                place.getButton().setEnabled(true);
            }
            m_jbtnReservations.setEnabled(false);
        }

    }

    private TicketInfo getTicketInfo(Place place) {

        try {
            return dlReceipts.getSharedTicket(place.getId());
        } catch (BasicException e) {
            new MessageInf(e).show(JTicketsBagRestaurantMap.this);
            return null;
        }
    }

    private void setActivePlace(Place place, TicketInfo ticket) {
        m_PlaceCurrent = place;
        m_panelticket.setActiveTicket(ticket, m_PlaceCurrent.getName());
    }

    private void showView(String view) {
        CardLayout cl = (CardLayout) (getLayout());
        cl.show(this, view);
    }

    private class MyActionListener implements ActionListener {

        private final Place m_place;

        public MyActionListener(Place place) {
            m_place = place;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

            if (m_PlaceClipboard == null) {

                if (customer == null) {
                    // tables

                    // check if the sharedticket is the same
                    TicketInfo ticket = getTicketInfo(m_place);
       
                    // check

/* ticket과 선택한 테이블에 사람이 있는지 없는지 확인 */
                    if (ticket == null && !m_place.hasPeople()) {
                        // Empty table and checked

                        // table occupied
                        ticket = new TicketInfo();
                        // (Bobview) 메뉴 삽입 (빈 테이블에)           
                        System.out.println("table_id : " + m_place.getId());
                       // syncBobviewTableInfo(ticket, m_place.getId());


                        
                        try {
//Create a new pickup code because this is a new ticket                            
                            dlReceipts.insertSharedTicket(m_place.getId(), ticket, ticket.getPickupId());
                        } catch (BasicException e) {
                            new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                        }
                        m_place.setPeople(true);
// 선택한 place를 m_PlaceCurrent에 저장, 선택한 place에 대한 ticket을 TicketsEditor에 지정
                        setActivePlace(m_place, ticket);    // 현재 사용중인 테이블임을 지정

                    } else if (ticket == null && m_place.hasPeople()) {
                        // The table is now empty
                        //new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                        ticket = new TicketInfo(); // change for syncBobviewTable
                    // (Bobview) 메뉴 삽입 (동기화되어 채워진 테이블)
                        System.out.println("table_id : " + m_place.getId());
                        syncBobviewTableInfo(ticket, m_place.getId());
//                         try {
////Create a new pickup code because this is a new ticket                            
//                            dlReceipts.insertSharedTicket(m_place.getId(), ticket, ticket.getPickupId());
//                        } catch (BasicException e) {
//                            new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
//                        }
                      
                        
                        m_place.setPeople(true); // fixed 
                        setActivePlace(m_place, ticket);    // change for syncBobviewTable
                       
                      
                    } else if (ticket != null && !m_place.hasPeople()) {
                        // The table is now full
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                        m_place.setPeople(true);

                    } else { // both != null
                        // Full table                
                        // m_place.setPeople(true); // already true                           
                        setActivePlace(m_place, ticket);

                    }
                } else {
                    // receiving customer.

                    // check if the sharedticket is the same
                    TicketInfo ticket = getTicketInfo(m_place);
                    if (ticket == null) {
                        // receive the customer
                        // table occupied
                        ticket = new TicketInfo();

                        try {
                            ticket.setCustomer(customer.getId() == null
                                    ? null
                                    : dlSales.loadCustomerExt(customer.getId()));
                        } catch (BasicException e) {
                            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                            msg.show(JTicketsBagRestaurantMap.this);
                        }

                        try {
                            dlReceipts.insertSharedTicket(m_place.getId(), ticket, ticket.getPickupId());
                        } catch (BasicException e) {
                            new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                        }
                        m_place.setPeople(true);
                        m_PlaceClipboard = null;
                        customer = null;

                        setActivePlace(m_place, ticket);
                    } else {
                        // TODO: msg: The table is now full
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                        m_place.setPeople(true);
                        m_place.getButton().setEnabled(false);
                    }
                }
            } else {
                // check if the sharedticket is the same
                TicketInfo ticketclip = getTicketInfo(m_PlaceClipboard);

                if (ticketclip == null) {
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                    m_PlaceClipboard.setPeople(false);
                    m_PlaceClipboard = null;
                    customer = null;
                    printState();
                } else {
                    // tenemos que copiar el ticket del clipboard
                    if (m_PlaceClipboard == m_place) {
                        // the same button. Canceling.
                        Place placeclip = m_PlaceClipboard;
                        m_PlaceClipboard = null;
                        customer = null;
                        printState();
                        setActivePlace(placeclip, ticketclip);
                    } else if (!m_place.hasPeople()) {
                        // Moving the receipt to an empty table
                        TicketInfo ticket = getTicketInfo(m_place);
////
                        if (ticket == null) {
                            try {
                                dlReceipts.insertSharedTicket(m_place.getId(), ticketclip, ticketclip.getPickupId());//dlSales.getNextPickupIndex());
                                m_place.setPeople(true);
                                dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
                                m_PlaceClipboard.setPeople(false);
                            } catch (BasicException e) {
                                new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                            }

                            m_PlaceClipboard = null;
                            customer = null;
                            printState();

                            // No hace falta preguntar si estaba bloqueado porque ya lo estaba antes
                            // activamos el ticket seleccionado
                            setActivePlace(m_place, ticketclip);

                        } else {
                            // Full table
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                            m_PlaceClipboard.setPeople(true);
                            printState();
                        }
                    } else {
                        // Merge the lines with the receipt of the table
                        TicketInfo ticket = getTicketInfo(m_place);

                        if (ticket == null) {
                            // The table is now empty
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                            m_place.setPeople(false); // fixed                        
                        } else {
                            //asks if you want to merge tables
                            if (JOptionPane.showConfirmDialog(JTicketsBagRestaurantMap.this, AppLocal.getIntString("message.mergetablequestion"), AppLocal.getIntString("message.mergetable"), JOptionPane.YES_NO_OPTION)
                                    == JOptionPane.YES_OPTION) {
                                // merge lines ticket

                                try {
                                    dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
                                    m_PlaceClipboard.setPeople(false);
                                    if (ticket.getCustomer() == null) {
                                        ticket.setCustomer(ticketclip.getCustomer());
                                    }
                                    for (TicketLineInfo line : ticketclip.getLines()) {
                                        ticket.addLine(line);
                                    }
                                    dlReceipts.updateSharedTicket(m_place.getId(), ticket, ticket.getPickupId());
                                } catch (BasicException e) {
                                    new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                                }

                                m_PlaceClipboard = null;
                                customer = null;
//clear the original table data
                                restDB.clearCustomerNameInTable(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearWaiterNameInTable(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearTableMovedFlag(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearTicketIdInTable(restDB.getTableDetails(ticketclip.getId()));

                                //           restDB.clearTableMovedFlag("");
                                printState();

                                setActivePlace(m_place, ticket);
                            } else {
                                // Cancel merge operations
                                Place placeclip = m_PlaceClipboard;
                                m_PlaceClipboard = null;
                                customer = null;
                                printState();
                                setActivePlace(placeclip, ticketclip);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param btnText
     */
    public void setButtonTextBags(String btnText) {
        m_PlaceClipboard.setButtonText(btnText);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanelMap = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jbtnReservations = new javax.swing.JButton();
        m_jbtnRefresh = new javax.swing.JButton();
        m_jText = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        m_jPanelMap.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        m_jPanelMap.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_jbtnReservations.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        m_jbtnReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnReservations.setText(AppLocal.getIntString("button.reservations")); // NOI18N
        m_jbtnReservations.setToolTipText("예약화면 열기");
        m_jbtnReservations.setFocusPainted(false);
        m_jbtnReservations.setFocusable(false);
        m_jbtnReservations.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnReservations.setMaximumSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setMinimumSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setPreferredSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setRequestFocusEnabled(false);
        m_jbtnReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnReservationsActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnReservations);

        m_jbtnRefresh.setFont(StartPOS.getgblFont().deriveFont( 0, 10)); // NOI18N
        m_jbtnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        m_jbtnRefresh.setText(AppLocal.getIntString("button.reloadticket")); // NOI18N
        m_jbtnRefresh.setToolTipText("테이블 정보 새로고침");
        m_jbtnRefresh.setFocusPainted(false);
        m_jbtnRefresh.setFocusable(false);
        m_jbtnRefresh.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnRefresh.setMaximumSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setMinimumSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setPreferredSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setRequestFocusEnabled(false);
        m_jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnRefreshActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnRefresh);
        jPanel2.add(m_jText);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        m_jPanelMap.add(jPanel1, java.awt.BorderLayout.NORTH);

        add(m_jPanelMap, "map");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnRefreshActionPerformed

        syncBobviewTable();                       
        loadTickets();
        printState();    
        m_PlaceClipboard = null;
        customer = null;
        System.out.println("새로고침 - 루프 시작");        // (Bobview) 새로고침 버튼 클릭
        final Timer m_timer = new Timer();
        TimerTask m_task = new TimerTask(){
            public void run(){     
                syncBobviewTable();                       
                loadTickets();
                printState();    
            }            
        };
         m_timer.scheduleAtFixedRate(m_task, 500, 3000); 
         
        for(int i = 0; i < newtableList.size() ; i++){
            String tableId = "테이블 번호 : " + newtableList.get(i);
            JLabel tableLabel = new JLabel(tableId);
            tableLabel.setFont(new Font("돋움", Font.BOLD, 16));  
            JOptionPane.showMessageDialog(this, tableLabel, " 주문이 접수되었습니다.",  JOptionPane.INFORMATION_MESSAGE);                         
        }
        
       if(call_message != null){
        JLabel callMsg = new JLabel(call_message);
        callMsg.setFont(new Font("돋움", Font.PLAIN, 12));  
        JOptionPane.showMessageDialog(this, callMsg, " 호출 테이블 : " + call_table,  JOptionPane.PLAIN_MESSAGE);     
       }
        call_message = null;
        newtableList.clear();
                                      
    }//GEN-LAST:event_m_jbtnRefreshActionPerformed

    
    
    private void m_jbtnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnReservationsActionPerformed

        showView("res");
        m_jreservations.activate();

    }//GEN-LAST:event_m_jbtnReservationsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jPanelMap;
    private javax.swing.JLabel m_jText;
    private javax.swing.JButton m_jbtnRefresh;
    private javax.swing.JButton m_jbtnReservations;
    // End of variables declaration//GEN-END:variables

}
