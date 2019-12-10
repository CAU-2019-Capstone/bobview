/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.util.AltEncrypter;
import java.util.Currency;
import java.util.Locale;

/**
 *
 * @author DongWoon
 */
public class PaymentGatewayKCP implements PaymentGateway {
    private String sPort;
    private String sBPS;

    public PaymentGatewayKCP() {
    }
    
    public PaymentGatewayKCP (AppProperties props) {
        this.sPort = props.getProperty("payment.terminalport");
        this.sBPS = props.getProperty("payment.terminalbps");
    }
    
    @Override
    public void execute(PaymentInfoMagcard payinfo) {
        int ret = PaymentDriverKCP.PublishCard(sPort, sBPS, payinfo);
        if (ret != 0) {
            payinfo.paymentError("KCP Card error", "error code = "+ ret);
        } 
    }
}
