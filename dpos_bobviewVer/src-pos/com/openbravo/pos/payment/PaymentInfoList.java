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

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author JG uniCenta
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PaymentInfoList {
    
    private LinkedList<PaymentInfo> m_apayment;
    
    /** Creates a new instance of PaymentInfoComposed */
    public PaymentInfoList() {
// JG 16 May 12 use diamond inference
        m_apayment = new LinkedList<>();
    }
        
    /**
     *
     * @return
     */
    public double getTotal() {
        
        double dTotal = 0.0;
        Iterator i = m_apayment.iterator();
        while (i.hasNext()) {
            PaymentInfo p = (PaymentInfo) i.next();
            dTotal += p.getTotal();
        }
        
        return dTotal;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return m_apayment.isEmpty();
    }
    
    /**
     *
     * @param p
     */
    public void add(PaymentInfo p) {
        m_apayment.addLast(p);
    }
    
    /**
     *
     */
    public void removeLast() {
        m_apayment.removeLast();
    }
    
    /**
     *
     * @return
     */
    public LinkedList<PaymentInfo> getPayments() {
        return m_apayment;
    }
}
