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

package com.openbravo.pos.panels;

import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author adrianromero
 */
public class JPanelPayments extends JPanelTable {
    private static final long serialVersionUID = -6623548984119934520L;
    
    private PaymentsEditor jeditor;    
    private DataLogicSales m_dlSales = null;
    
    /** Creates a new instance of JPanelPayments */
    public JPanelPayments() {
    }
    
    /**
     *
     */
    @Override
    protected void init() {
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");         
        jeditor = new PaymentsEditor(app, dirty);    
    }
    
    /**
     *
     * @return
     */
    @Override
    public ListProvider getListProvider() {
        return null;
    }
    
    /**
     *
     * @return
     */
    @Override
    public SaveProvider getSaveProvider() {
        return  new SaveProvider(null
                , m_dlSales.getPaymentMovementInsert()
                , m_dlSales.getPaymentMovementDelete());
    }
    
    /**
     *
     * @return
     */
    @Override
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.Payments");
    }    
}
