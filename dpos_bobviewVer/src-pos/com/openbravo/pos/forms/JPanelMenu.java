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
package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author adrianromero
 */
public class JPanelMenu extends JPanel implements JPanelView {
    private static final long serialVersionUID = 5111450174036109379L;

    private MenuDefinition m_menu;
    private boolean created = false;

    /**
     * Creates new form JStockMenu
     *
     * @param menu
     */
    public JPanelMenu(MenuDefinition menu) {

        m_menu = menu;
        created = false;

        initComponents();
    }

    /**
     *
     * @return
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return m_menu.getTitle();
    }

    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {

        if (created == false) {

            for (int i = 0; i < m_menu.countMenuElements(); i++) {
                MenuElement menuitem = m_menu.getMenuElement(i);
                menuitem.addComponent(this);
            }
            created = true;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean deactivate() {
        return true;
    }

    /**
     *
     * @param title
     */
    public void addTitle(Component title) {

        currententrypanel = null;

        JPanel titlepanel = new JPanel();
        titlepanel.setLayout(new java.awt.BorderLayout());
        titlepanel.add(title, java.awt.BorderLayout.CENTER);
        titlepanel.applyComponentOrientation(getComponentOrientation());

        menucontainer.add(titlepanel);
    }

    /**
     *
     * @param entry
     */
    public void addEntry(Component entry) {

        if (currententrypanel == null) {
            currententrypanel = new JPanel();
            currententrypanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 20, 0));
            currententrypanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
            menucontainer.add(currententrypanel);
        }

        currententrypanel.add(entry);
        currententrypanel.applyComponentOrientation(getComponentOrientation());

    }

    private JPanel currententrypanel = null;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menucontainer = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        setLayout(new java.awt.BorderLayout());

        menucontainer.setLayout(new javax.swing.BoxLayout(menucontainer, javax.swing.BoxLayout.Y_AXIS));
        add(menucontainer, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel menucontainer;
    // End of variables declaration//GEN-END:variables

}
