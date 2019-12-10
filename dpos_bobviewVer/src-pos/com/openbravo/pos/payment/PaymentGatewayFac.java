//    dPOS  - Dongwun Point Of Sale
//    Copyright (C) 2008-2014 Openbravo, S.L.
//    http://www.dongwun.com - additional amends by Walter Wojick for Blue Pay
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

import com.openbravo.pos.forms.AppProperties;

/**
 *
 * @author JG uniCenta
 */
public class PaymentGatewayFac {
    
    /** Creates a new instance of PaymentGatewayFac */
    private PaymentGatewayFac() {
    }
    
    /**
     *
     * @param props
     * @return
     */
    public static PaymentGateway getPaymentGateway(AppProperties props) {
        
        String sReader = props.getProperty("payment.gateway");
        switch (sReader) {
            case "external":
                return new PaymentGatewayExt();
            case "KCP(KR)":
                return new PaymentGatewayKCP(props);
            case "NICE(KR)":
                return new PaymentGatewayNICE(props);
            case "PayPoint / SecPay(UK)":
                return new PaymentGatewayPayPoint(props);
            case "AuthorizeNet(US, CA, UK, AU, EU)":
                return new PaymentGatewayAuthorizeNet(props);
            case "BluePay AUTH.NET EMU(US, CA)":
                return new PaymentGatewayBluePayAUTHNETEMU(props);
            case "BluePay 2.0 POST(US, CA)":
                return new PaymentGatewayBluePay20POST(props);
            case "La Caixa (Spain)":
                return new PaymentGatewayCaixa(props);
            case "Planetauthorize(US, UK)":
                return new PaymentGatewayPlanetauthorize(props);
            case "First Data / LinkPoint / YourPay":
                return new PaymentGatewayLinkPoint(props);
            case "PaymentsGateway.net(US, CA)":
                return new PaymentGatewayPGNET(props);
            default:
                return null;
        }
    }      
}
