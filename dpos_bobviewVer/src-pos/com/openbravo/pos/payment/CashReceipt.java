/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppConfig;

/**
 *
 * @author DongWoon
 */
public class CashReceipt {
    private int receipt_type;
    private String idnumber;
    private AppConfig appconfig;

    public CashReceipt(AppConfig app, int type, String idnum) {
        receipt_type = type;
        idnumber = idnum;
        appconfig = app;
    }
    
    public int publish(double amt)
    {
        String sReader = appconfig.getProperty("payment.gateway");
        String sPort = appconfig.getProperty("payment.terminalport");
        String sBPS = appconfig.getProperty("payment.terminalbps");
        
        switch (sReader) {
            case "external":
                return 0;
            case "KCP(KR)":
                return PaymentDriverKCP.PublishCashReciept(sPort, sBPS, receipt_type, idnumber, amt);
            case "NICE(KR)":
                return PaymentDriverNICE.PublishCashReciept(sPort, sBPS, receipt_type, idnumber, amt);
            case "AuthorizeNet(US, CA, UK, AU, EU)":
                return 0;
            case "BluePay AUTH.NET EMU(US, CA)":
                return 0;
            case "BluePay 2.0 POST(US, CA)":
                return 0;
            case "Planetauthorize(US, UK)":
                return 0;
            case "First Data / LinkPoint / YourPay":
                return 0;
            case "PaymentsGateway.net(US, CA)":
                return 0;
            case "La Caixa (Spain)":
                return 0;
            default:
                return 0;
        }
    }
}
