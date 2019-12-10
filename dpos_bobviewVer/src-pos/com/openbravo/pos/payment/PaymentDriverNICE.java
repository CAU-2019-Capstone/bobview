/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.sun.jna.*;
import java.util.Arrays;
import javax.swing.JOptionPane;
/**
 *
 * @author DongWoon
 */
public class PaymentDriverNICE {
    public static native int ReqStop();
    public static native int ReqToCat(int Port, int Baud, String SendBuf, byte[] RecvBuf);
    static {
        Native.register("/PosToCatReq.dll");
    }
    private static char FS = 0x1C;
    private static String[] anarecv = new String[64];
    private static int AnalyRecv(byte[] recv)
    {
        int sta = 0, cnt = 0, stridx = 0;
        for (int i = 0; i < recv.length; i++) {
            if (recv[i] == 2 || recv[i]==3) continue; // STX || ETX
            if (recv[i] == 0x1c) { // Field separator
                if (cnt == 0) {
                    anarecv[stridx++] = "";
                } else {
                    anarecv[stridx++] = new String(Arrays.copyOfRange(recv, sta, sta+cnt));
                    cnt = 0;
                }
            } else {
                if (cnt == 0) sta = i;
                cnt ++;
            }
        }
        return stridx;
    }
    
    public static int PublishCashReciept(String portid, String bps, int receipt_type, String idnumber, double amt)
    {
        String rt_str = String.format("%02d", receipt_type);
        
        //현금승인 요청
        String SendData = "D3" + FS + rt_str + FS + "@" + FS + idnumber + FS + FS + FS + String.format("%.0f", amt) + FS +
                            " " + FS + "      " + FS + "            " + FS + FS;  
        byte[] RecvData = new byte[1024];
        
        try {
            if (ReqToCat(Integer.parseInt(portid.replace("COM", "")), Integer.parseInt(bps), SendData, RecvData) != 1) return -1;
            if (AnalyRecv(RecvData) <= 0) return -2;
            if (!anarecv[1].equals("A")) return -3;   // "A" : 정상승인, "B" : 거절
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public static int PublishCard(String portid, String bps, PaymentInfoMagcard payinfo)
    {
        String SendData, SvcCode;
        byte[] RecvData = new byte[1024];
        double amt = payinfo.getTotal();
        
        try {
            String Halbu = String.format("%02d", Integer.parseInt(payinfo.getExpirationDate()));
            if (amt < 0) {
                amt = amt * -1.0;
                if (payinfo.getTrack1(true).equals("Y"))    //은련결제여부
                    SvcCode = "A6";
                else
                    SvcCode = "D2";
                SendData = SvcCode + FS + FS + FS + Halbu + FS + FS + FS + String.format("%.0f", amt) + FS + 
                            payinfo.getTrack3(true) + FS + payinfo.getTrack2(true) + FS + FS + FS;  //신용취소 요청
                // payinfo.getTrack3(true) - 취소시 원거래 번호, payinfo.getTrack2(true) - 취소시 원거래 일자
            } else {
                if (payinfo.getTrack1(true).equals("Y"))    //은련결제여부
                    SvcCode = "A5";
                else
                    SvcCode = "D1";
                SendData = SvcCode + FS + FS + FS + Halbu + FS + FS + FS + String.format("%.0f", amt) + FS + 
                            "            " + FS + "        " + FS + FS + FS; //신용승인 요청
            }
            if (ReqToCat(Integer.parseInt(portid.replace("COM", "")), Integer.parseInt(bps), SendData, RecvData) != 1) return -1;
            if (AnalyRecv(RecvData) <= 0) return -2;
            if (!anarecv[1].equals("A")) return -3;   // "A" : 정상승인, "B" : 거절
            payinfo.setCardNumber(anarecv[13]);         //마스킹된 카드번호
            payinfo.paymentOK(anarecv[15], anarecv[7], anarecv[14]); // 7-승인번호, 14-가맹점번호, 15-단말기처리일련번호
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

}
