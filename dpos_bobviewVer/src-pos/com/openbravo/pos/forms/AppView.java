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

import com.openbravo.data.loader.Session;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.scale.DeviceScale;
import com.openbravo.pos.scanpal2.DeviceScanner;
import java.util.Date;

/**
 *
 * @author adrianromero
 */
public interface AppView {
    
    /**
     *
     * @return
     */
    public DeviceScale getDeviceScale();

    /**
     *
     * @return
     */
    public DeviceTicket getDeviceTicket();

    /**
     *
     * @return
     */
    public DeviceScanner getDeviceScanner();
      
    /**
     *
     * @return
     */
    public Session getSession();

    /**
     *
     * @return
     */
    public AppProperties getProperties();

    /**
     *
     * @param beanfactory
     * @return
     * @throws BeanFactoryException
     */
    public Object getBean(String beanfactory) throws BeanFactoryException;
     
    /**
     *
     * @param value
     * @param iSeq
     * @param dStart
     * @param dEnd
     */
    public void setActiveCash(String value, int iSeq, Date dStart, Date dEnd);

    /**
     *
     * @return
     */
    public String getActiveCashIndex();

    /**
     *
     * @return
     */
    public int getActiveCashSequence();

    /**
     *
     * @return
     */
    public Date getActiveCashDateStart();

    /**
     *
     * @return
     */
    public Date getActiveCashDateEnd();
    
    /**
     *
     * @return
     */
    public String getInventoryLocation();
    
    /**
     *
     */
    public void waitCursorBegin();

    /**
     *
     */
    public void waitCursorEnd();
    
    /**
     *
     * @return
     */
    public AppUserView getAppUserView();
}

