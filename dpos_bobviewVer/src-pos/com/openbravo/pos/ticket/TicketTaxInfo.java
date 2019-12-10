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

package com.openbravo.pos.ticket;

import com.openbravo.format.Formats;

/**
 *
 * @author JG uniCenta
 */
public class TicketTaxInfo {
    
    private TaxInfo tax;
    
    private double subtotal;
    private double taxtotal;
            
    /** Creates a new instance of TicketTaxInfo
     * @param tax */
    public TicketTaxInfo(TaxInfo tax) {
        this.tax = tax;
        
        subtotal = 0.0;
        taxtotal = 0.0;
    }
    
    /**
     *
     * @return
     */
    public TaxInfo getTaxInfo() {
        return tax;
    }
    
    /**
     *
     * @param dValue
     */
    public void add(double dValue) {
        subtotal += dValue;
        taxtotal = subtotal * tax.getRate();
    }
    
    /**
     *
     * @return
     */
    public double getSubTotal() {    
        //return subtotal;
        return 0.0;
    }
    
    /**
     *
     * @return
     */
    public double getTax() {       
        return taxtotal;
    }
    
    /**
     *
     * @return
     */
    public double getTotal() {         
        return subtotal + taxtotal;
    }
    
    /**
     *
     * @return
     */
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }

    /**
     *
     * @return
     */
    public String printTax() {
        return Formats.CURRENCY.formatValue(new Double(getTax()));
    }    

    /**
     *
     * @return
     */
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }    
}
