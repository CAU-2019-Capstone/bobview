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

import java.awt.*;

/**
 *
 * @author adrianromero
 */
public class JPaymentSelectReceipt extends JPaymentSelect {
    private static final long serialVersionUID = -8840274982476096051L;
    
    /** Creates new form JPaymentSelect
     * @param parent
     * @param modal
     * @param o */
    protected JPaymentSelectReceipt(java.awt.Frame parent, boolean modal, ComponentOrientation o) {
        super(parent, modal, o);
    }
    /** Creates new form JPaymentSelect
     * @param parent
     * @param o
     * @param modal */
    protected JPaymentSelectReceipt(java.awt.Dialog parent, boolean modal, ComponentOrientation o) {
        super(parent, modal, o);
    }

    /**
     *
     * @param parent
     * @return
     */
    public static JPaymentSelect getDialog(Component parent) {
         
        Window window = getWindow(parent);
        
        if (window instanceof Frame) { 
            return new JPaymentSelectReceipt((Frame) window, true, parent.getComponentOrientation());
        } else {
            return new JPaymentSelectReceipt((Dialog) window, true, parent.getComponentOrientation());
        }
    }

    /**
     *
     */
    @Override
    protected void addTabs() {
        
        addTabPayment(new JPaymentSelect.JPaymentCashCreator());
        addTabPayment(new JPaymentSelect.JPaymentPaperCreator());            
        addTabPayment(new JPaymentSelect.JPaymentMagcardCreator());                
        addTabPayment(new JPaymentSelect.JPaymentFreeCreator());                
        addTabPayment(new JPaymentSelect.JPaymentDebtCreator());
// JG Added 1 Dec 13 
        addTabPayment(new JPaymentSelect.JPaymentBankCreator());        
        setHeaderVisible(true);
    }
    
    /**
     *
     * @param isPositive
     * @param isComplete
     */
    @Override
    protected void setStatusPanel(boolean isPositive, boolean isComplete) {
        
        setAddEnabled(isPositive && !isComplete);
        setOKEnabled(isComplete);
    }
    
    /**
     *
     * @param total
     * @return
     */
    @Override
    protected PaymentInfo getDefaultPayment(double total) {
        return new PaymentInfoCash_original(total, total);
    }
}
