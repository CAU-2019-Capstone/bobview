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

/**
 *
 * @author JG uniCenta
 */
public class PaymentException extends java.lang.Exception {
    private static final long serialVersionUID = 3689406878063494067L;
    
    /**
     * Creates a new instance of <code>PaymentException</code> without detail message.
     */
    public PaymentException() {
    }
   
    
    /**
     * Constructs an instance of <code>PaymentException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PaymentException(String msg) {
        super(msg);
    }
}
