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
//    along with dPOS.  If not, see <http://www.gnu.org/licenses/>
package com.openbravo.pos.forms;

import com.openbravo.beans.LocaleResources;

/**
 *
 * @author adrianromero
 */
public class AppLocal {

    /**
     *
     */
    public static final String APP_NAME = "dPOS";

    /**
     *
     */
    public static final String APP_ID = "dongwundpos";

    /**
     *
     */
    public static final String APP_VERSION = "1.10";

//    public static final String APP_IDJL = "Unicenta John L's changes";    
//    public static final String APP_VERSIONJL ="1.04";
//    public static final String APP_VERSIONJLCORE="130703";
    // private static List<ResourceBundle> m_messages;
    private static final LocaleResources m_resources;

    static {
        m_resources = new LocaleResources();
        m_resources.addBundleName("pos_messages");
        m_resources.addBundleName("erp_messages");
    }

    /**
     * Creates a new instance of AppLocal
     */
    private AppLocal() {
    }

    /**
     *
     * @param sKey
     * @return
     */
    public static String getIntString(String sKey) {
        return m_resources.getString(sKey);
    }

    /**
     *
     * @param sKey
     * @param sValues
     * @return
     */
    public static String getIntString(String sKey, Object... sValues) {
        return m_resources.getString(sKey, sValues);
    }
}
