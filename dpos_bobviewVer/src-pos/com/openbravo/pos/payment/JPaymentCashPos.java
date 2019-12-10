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

package com.openbravo.pos.payment;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.StartPOS;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.util.RoundUtils;
import com.openbravo.pos.util.ThumbNailBuilder;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author adrianromero
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JPaymentCashPos extends javax.swing.JPanel implements JPaymentInterface {
    private static final long serialVersionUID = 1196664023528502466L;
    
    private JPaymentNotifier m_notifier;
    private AppConfig m_config;
    private double m_dPaid;
    private double m_dTotal;  
    private Boolean priceWith00;
    
    /** Creates new form JPaymentCash
     * @param notifier
     * @param dlSystem */
    public JPaymentCashPos(JPaymentNotifier notifier, DataLogicSystem dlSystem) {
        
        m_notifier = notifier;
        
        initComponents();  
        
        m_jTendered.addPropertyChangeListener("Edition", new RecalculateState());
        m_jTendered.addEditorKeys(m_jKeys);
        txtID.addEditorKeys(m_jKeys);
        
// added JDL 11.05.13        
        AppConfig m_config =  new AppConfig(new File((System.getProperty("user.dir") + ("/database")), AppLocal.APP_ID + ".properties"));        
        m_config.load();        
        priceWith00 =("true".equals(m_config.getProperty("till.pricewith00")));
        if (priceWith00) {
            // use '00' instead of '.'
            m_jKeys.dotIs00(true);
        }
        this.m_config = m_config;
       
        String code = dlSystem.getResourceAsXML("payment.cash");
        if (code != null) {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
                script.put("payment", new ScriptPaymentCash(dlSystem));    
                script.eval(code);
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotexecute"), e);
                msg.show(this);
            }
        }
        
    }
    
    /**
     *
     * @param customerext
     * @param dTotal
     * @param transID
     */
    @Override
    public void activate(CustomerInfoExt customerext, double dTotal, String transID) {
               
               
        m_dTotal = dTotal;
        
        m_jTendered.reset();
        m_jTendered.activate();
        txtID.setText("");
        
        printState();        
    }

    /**
     *
     * @return
     */
    @Override
    public PaymentInfo executePayment() {
        double total, paid;
        
        if (m_dPaid - m_dTotal >= 0.0) {    // full
            total = m_dTotal;
            paid = m_dPaid;
        } else {    //partial
            total = m_dPaid;
            paid = m_dPaid;
        }        
        
        int crtype = m_jCashReceipt.getSelectedIndex();
        String idnumber = txtID.getText();
        if (crtype != 0 && idnumber.length() >= 7 && paid > 0.0) {
            CashReceipt cr = new CashReceipt(this.m_config, crtype, idnumber);  
            int ret = cr.publish(total);
            if (ret != 0) {
                String errmsg = "Cash Receipt publish error = " + ret;
                JOptionPane.showMessageDialog(this, errmsg,AppLocal.getIntString("message.cannotcashreceipt"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return new PaymentInfoCash_original(total, paid);
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }
    
    private void printState() {

        Double value = m_jTendered.getDoubleValue();
        if (value == null || value == 0.0) {
            m_dPaid = m_dTotal;
        } else {            
            m_dPaid = value;

        }   

        int iCompare = RoundUtils.compare(m_dPaid, m_dTotal);
        
        m_jMoneyEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dPaid)));
        m_jChangeEuros.setText(iCompare > 0 
                ? Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal))
                : null); 
        
        m_notifier.setStatus(m_dPaid > 0.0, iCompare >= 0);
    }
    
    private class RecalculateState implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            printState();
        }
    }

    /**
     *
     */
    public class ScriptPaymentCash {
        
        private DataLogicSystem dlSystem;
        private ThumbNailBuilder tnbbutton;
        private AppConfig m_config;
        
        /**
         *
         * @param dlSystem
         */
        public ScriptPaymentCash(DataLogicSystem dlSystem) {
            AppConfig m_config =  new AppConfig(new File((System.getProperty("user.dir") + ("/database")), AppLocal.APP_ID + ".properties"));        
            m_config.load();
            this.m_config = m_config;
        
            this.dlSystem = dlSystem;
            tnbbutton = new ThumbNailBuilder(64, 50, "com/openbravo/images/cash.png");
        }
        
        /**
         *
         * @param image
         * @param amount
         */
        public void addButton(String image, double amount) {
            JButton btn = new JButton();
//added 19.04.13 JDL removal of text on payment buttons if required.   
            try {
            if ((m_config.getProperty("payments.textoverlay")).equals("true")){
                     btn.setIcon(new ImageIcon(tnbbutton.getThumbNailText(dlSystem.getResourceAsImage(image),"")));  
            } else {
                     btn.setIcon(new ImageIcon(tnbbutton.getThumbNailText(dlSystem.getResourceAsImage(image), Formats.CURRENCY.formatValue(amount)))); 
            }
            } catch (Exception e){
                    btn.setIcon(new ImageIcon(tnbbutton.getThumbNailText(dlSystem.getResourceAsImage(image), Formats.CURRENCY.formatValue(amount))));        
            }   
            
            btn.setFocusPainted(false);
            btn.setFocusable(false);
            btn.setRequestFocusEnabled(false);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setMargin(new Insets(2, 2, 2, 2));
            btn.addActionListener(new AddAmount(amount));
            jPanel6.add(btn);  
        }
    }
    
    
    
    private class AddAmount implements ActionListener {        
        private double amount;
        public AddAmount(double amount) {
            this.amount = amount;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Double tendered = m_jTendered.getDoubleValue();
 
            if (tendered == null) {
                 m_jTendered.setDoubleValue(amount);
            } else {
              m_jTendered.setDoubleValue(tendered + amount);    
              
            }

            printState();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jChangeEuros = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jMoneyEuros = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel3 = new javax.swing.JPanel();
        m_jTendered = new com.openbravo.editor.JEditorCurrencyPositive();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        m_jCashReceipt = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        txtID = new com.openbravo.editor.JEditorStringNumber();

        setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel4.setFont(StartPOS.getgblFont().deriveFont(0, 12));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 70));
        jPanel4.setLayout(null);

        m_jChangeEuros.setBackground(new java.awt.Color(255, 255, 255));
        m_jChangeEuros.setFont(StartPOS.getgblFont().deriveFont(0, 18));
        m_jChangeEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jChangeEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jChangeEuros.setOpaque(true);
        m_jChangeEuros.setPreferredSize(new java.awt.Dimension(180, 30));
        jPanel4.add(m_jChangeEuros);
        m_jChangeEuros.setBounds(120, 36, 180, 30);

        jLabel6.setFont(StartPOS.getgblFont().deriveFont(1, 18));
        jLabel6.setText(AppLocal.getIntString("Label.ChangeCash")); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel4.add(jLabel6);
        jLabel6.setBounds(10, 36, 100, 30);

        jLabel8.setFont(StartPOS.getgblFont().deriveFont(1, 18));
        jLabel8.setText(AppLocal.getIntString("Label.InputCash")); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel4.add(jLabel8);
        jLabel8.setBounds(10, 4, 100, 30);

        m_jMoneyEuros.setBackground(new java.awt.Color(204, 255, 51));
        m_jMoneyEuros.setFont(StartPOS.getgblFont().deriveFont(1, 18));
        m_jMoneyEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jMoneyEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jMoneyEuros.setOpaque(true);
        m_jMoneyEuros.setPreferredSize(new java.awt.Dimension(180, 30));
        m_jMoneyEuros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_jMoneyEurosMouseClicked(evt);
            }
        });
        jPanel4.add(m_jMoneyEuros);
        m_jMoneyEuros.setBounds(120, 4, 180, 30);

        jPanel5.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel5.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(m_jKeys);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        m_jTendered.setFont(StartPOS.getgblFont().deriveFont(1, 14));
        m_jTendered.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_jMoneyEurosMouseClicked(evt);
            }
        });
        jPanel3.add(m_jTendered, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel2.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel7.setFont(StartPOS.getgblFont().deriveFont(0, 12));
        jPanel7.setPreferredSize(new java.awt.Dimension(0, 80));
        jPanel7.setRequestFocusEnabled(false);
        jPanel7.setLayout(null);

        jLabel10.setFont(StartPOS.getgblFont().deriveFont(1, 12));
        jLabel10.setText(AppLocal.getIntString("Label.CashReceipt")); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel7.add(jLabel10);
        jLabel10.setBounds(10, 20, 70, 20);

        m_jCashReceipt.setFont(StartPOS.getgblFont().deriveFont(0, 12));
        m_jCashReceipt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "발급안함", "소비자소득공제", "사업자지출증빙", "자진발급" }));
        jPanel7.add(m_jCashReceipt);
        m_jCashReceipt.setBounds(90, 20, 100, 21);

        jLabel11.setFont(StartPOS.getgblFont().deriveFont(1, 12));
        jLabel11.setText(AppLocal.getIntString("Label.PhoneIDNumber")); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel7.add(jLabel11);
        jLabel11.setBounds(10, 50, 70, 20);

        txtID.setFont(StartPOS.getgblFont().deriveFont(0, 12));
        txtID.setPreferredSize(new java.awt.Dimension(180, 25));
        jPanel7.add(txtID);
        txtID.setBounds(90, 50, 130, 20);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        add(jPanel2, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jMoneyEurosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jMoneyEurosMouseClicked
        m_jTendered.activate();
    }//GEN-LAST:event_m_jMoneyEurosMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JComboBox m_jCashReceipt;
    private javax.swing.JLabel m_jChangeEuros;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JLabel m_jMoneyEuros;
    private com.openbravo.editor.JEditorCurrencyPositive m_jTendered;
    private com.openbravo.editor.JEditorStringNumber txtID;
    // End of variables declaration//GEN-END:variables
    
}
