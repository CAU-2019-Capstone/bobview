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
package com.openbravo.pos.mant;

import com.openbravo.pos.forms.StartPOS;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import java.awt.Component;
import java.util.UUID;
import javax.swing.JPanel;

/**
 *
 * @author adrianromero
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class PlacesEditor extends JPanel implements EditorRecord {
    private static final long serialVersionUID = -2360718610517519090L;

    private SentenceList m_sentfloor;
    private ComboBoxValModel m_FloorModel;

    private String m_sID;

    /**
     * Creates new form PlacesEditor
     *
     * @param dlSales
     * @param dirty
     */
    public PlacesEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();

        m_sentfloor = dlSales.getFloorsList();
        m_FloorModel = new ComboBoxValModel();

        m_jName.getDocument().addDocumentListener(dirty);
        m_jFloor.addActionListener(dirty);
        m_jX.getDocument().addDocumentListener(dirty);
        m_jY.getDocument().addDocumentListener(dirty);

        writeValueEOF();
    }

    /**
     *
     * @throws BasicException
     */
    public void activate() throws BasicException {

        m_FloorModel = new ComboBoxValModel(m_sentfloor.list());
        m_jFloor.setModel(m_FloorModel);
    }

    /**
     *
     */
    @Override
    public void refresh() {
    }

    /**
     *
     */
    @Override
    public void writeValueEOF() {

        m_sID = null;
        m_jName.setText(null);
        m_FloorModel.setSelectedKey(null);
        m_jX.setText(null);
        m_jY.setText(null);

        m_jName.setEnabled(false);
        m_jFloor.setEnabled(false);
        m_jX.setEnabled(false);
        m_jY.setEnabled(false);
    }

    /**
     *
     */
    @Override
    public void writeValueInsert() {

        m_sID = UUID.randomUUID().toString();
        m_jName.setText(null);
        m_FloorModel.setSelectedKey(null);
        m_jX.setText(null);
        m_jY.setText(null);

        m_jName.setEnabled(true);
        m_jFloor.setEnabled(true);
        m_jX.setEnabled(true);
        m_jY.setEnabled(true);
    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueDelete(Object value) {

        Object[] place = (Object[]) value;
        m_sID = Formats.STRING.formatValue(place[0]);
        m_jName.setText(Formats.STRING.formatValue(place[1]));
        m_jX.setText(Formats.INT.formatValue(place[2]));
        m_jY.setText(Formats.INT.formatValue(place[3]));
        m_FloorModel.setSelectedKey(place[4]);

        m_jName.setEnabled(false);
        m_jFloor.setEnabled(false);
        m_jX.setEnabled(false);
        m_jY.setEnabled(false);
    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueEdit(Object value) {

        Object[] place = (Object[]) value;
        m_sID = Formats.STRING.formatValue(place[0]);
        m_jName.setText(Formats.STRING.formatValue(place[1]));
        m_jX.setText(Formats.INT.formatValue(place[2]));
        m_jY.setText(Formats.INT.formatValue(place[3]));
        m_FloorModel.setSelectedKey(place[4]);

        m_jName.setEnabled(true);
        m_jFloor.setEnabled(true);
        m_jX.setEnabled(true);
        m_jY.setEnabled(true);
    }

    /**
     *
     * @return @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {
        Object[] place = new Object[6];
        place[0] = m_sID;
        place[1] = m_jName.getText();
        place[2] = Formats.INT.parseValue(m_jX.getText());
        place[3] = Formats.INT.parseValue(m_jY.getText());
        place[4] = m_FloorModel.getSelectedKey();
        place[5] = "qwerty";
        return place;
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jX = new javax.swing.JTextField();
        m_jY = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        m_jFloor = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setLayout(null);

        jLabel2.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 20, 90, 25);

        m_jName.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        add(m_jName);
        m_jName.setBounds(110, 20, 200, 25);

        jLabel3.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel3.setText("세로");
        add(jLabel3);
        jLabel3.setBounds(220, 90, 40, 25);

        m_jX.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        add(m_jX);
        m_jX.setBounds(160, 90, 50, 25);

        m_jY.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        add(m_jY);
        m_jY.setBounds(260, 90, 50, 25);

        jLabel1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.placefloor")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 50, 90, 25);

        m_jFloor.setFont(StartPOS.getgblFont().deriveFont( 0, 14)); // NOI18N
        add(m_jFloor);
        m_jFloor.setBounds(110, 50, 200, 25);

        jLabel5.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel5.setText("가로");
        jLabel5.setToolTipText("");
        add(jLabel5);
        jLabel5.setBounds(110, 90, 50, 25);

        jLabel6.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel6.setText(AppLocal.getIntString("label.placeposition")); // NOI18N
        add(jLabel6);
        jLabel6.setBounds(20, 90, 60, 25);

        jLabel7.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel7.setText("<html>판매 화면에서 위치할 각 테이블의 가로, 세로(Ex. 200, 400)를 설정하시면<br><br>해당 위치에 테이블의 버튼이 위치하게 됩니다.");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setMinimumSize(new java.awt.Dimension(50, 40));
        jLabel7.setPreferredSize(new java.awt.Dimension(489, 40));
        add(jLabel7);
        jLabel7.setBounds(10, 140, 410, 90);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JComboBox m_jFloor;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jX;
    private javax.swing.JTextField m_jY;
    // End of variables declaration//GEN-END:variables

}
