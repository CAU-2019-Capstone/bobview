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
public interface MagCardReader {
 
    /**
     *
     * @return
     */
    public String getReaderName();
    
    /**
     *
     */
    public void reset();

    /**
     *
     * @param c
     */
    public void appendChar(char c);

    /**
     *
     * @return
     */
    public boolean isComplete();
    
    /**
     *
     * @return
     */
    public String getHolderName();

    /**
     *
     * @return
     */
    public String getCardNumber();

    /**
     *
     * @return
     */
    public String getExpirationDate();
    
    /**
     *
     * @return
     */
    public String getTrack1();

    /**
     *
     * @return
     */
    public String getTrack2();

    /**
     *
     * @return
     */
    public String getTrack3();
}
