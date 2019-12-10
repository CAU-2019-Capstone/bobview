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
package com.openbravo.pos.sales;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurantMap;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.sales.simple.JTicketsBagSimple;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author JG uniCenta
 */
public abstract class JTicketsBag extends JPanel {

    /**
     *
     */
    protected AppView m_App;

    /**
     *
     */
    protected DataLogicSales m_dlSales;

    /**
     *
     */
    protected TicketsEditor m_panelticket;

    /**
     * Creates new form JTicketsBag
     *
     * @param oApp
     * @param panelticket
     */
    public JTicketsBag(AppView oApp, TicketsEditor panelticket) {
        m_App = oApp;
        m_panelticket = panelticket;
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
    }

    /**
     *
     */
    public abstract void activate();

    /**
     *
     * @return
     */
    public abstract boolean deactivate();

    /**
     *
     */
    public abstract void deleteTicket();

    /**
     *
     * @return
     */
    protected abstract JComponent getBagComponent();

    /**
     *
     * @return
     */
    protected abstract JComponent getNullComponent();

    /**
     *
     * @param sName
     * @param app
     * @param panelticket
     * @return
     */
    public static JTicketsBag createTicketsBag(String sName, AppView app, TicketsEditor panelticket) {
        switch (sName) {
            case "표준모드":
                // return new JTicketsBagMulti(oApp, user, panelticket);
                return new JTicketsBagShared(app, panelticket);
            case "테이블 후불 모드":
                return new JTicketsBagRestaurantMap(app, panelticket);
            default:
                // "simple"
                return new JTicketsBagSimple(app, panelticket);
        }
    }
}
