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

package com.openbravo.data.user;

/**
 *
 * @author adrianromero
 * Created on 22 de marzo de 2007, 0:01
 *
 */
public class DocumentLoaderBasic implements DocumentLoader {
    
    /**
     *
     */
    public static final DocumentLoader INSTANCE = new DocumentLoaderBasic();
    
    /** Creates a new instance of DocumentLoaderBasic */
    private DocumentLoaderBasic() {
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Object getValue(Object key) {
        return key;
    }

    /**
     *
     * @param value
     * @return
     */
    public Object getKey(Object value) {
        return value;
    }
}
