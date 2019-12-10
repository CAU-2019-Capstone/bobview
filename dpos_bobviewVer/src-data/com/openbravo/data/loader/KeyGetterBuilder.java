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

package com.openbravo.data.loader;

/**
 *
 * @author adrianromero
 * Created on 27 de febrero de 2007, 22:09
 *
 */
public class KeyGetterBuilder implements IKeyGetter {
  
    /**
     *
     */
    public final static IKeyGetter INSTANCE = new KeyGetterBuilder();
    
    /** Creates a new instance of KeyGetterBuilder */
    public KeyGetterBuilder() {
    }
    
    /**
     *
     * @param value
     * @return
     */
    public Object getKey(Object value) {
        
        return (value == null) 
            ? null
            : ((IKeyed) value).getKey();
    }   
}
