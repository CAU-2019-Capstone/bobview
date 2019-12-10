/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.sun.jna.*;
import javax.swing.JOptionPane;
/**
 *
 * @author DongWoon
 */
public class PaymentDriverKCP {
    public static native int KcpModuleVer(byte[] ret);
    public static native int KcpCatInitLibrary(String sTrCode, String sProcCode);
    public static native int KcpCatReleaseLibrary();
    public static native int KcpCatSetData(String sKey, String sValue);
    public static native int KcpCatGetData(String sKey, byte[] ret);
    public static native int KcpCatTrans(String sConMode);
    static {
        Native.register("/libKCP.dll");
    }
    
    public static int PublishCashReciept(String portid, String bps, int receipt_type, String idnumber, double amt)
    {
        byte[] retstr = new byte[32];
        int drvstep = 0;
        String rest;
        
        try {
            drvstep = KcpModuleVer(retstr);
            if (drvstep != 1) return -1;
            if (KcpCatInitLibrary("0100", "E00005") != 1) return -2;    //현금영수증 발급
            drvstep = 2;
            if (KcpCatSetData("tx_type", "10") < 0) drvstep = -3;  //거래유형:신분정보
            else if (KcpCatSetData("ms_data", idnumber) < 0) drvstep = -3;  //신분정보
            else if (KcpCatSetData("pass_wd", (receipt_type == 2) ? "10" : "00") < 0) drvstep = -3;  //발급용도:10-사업자지출증빙, 00-소득자소득공제
            else if (KcpCatSetData("cpx_flag", "N") < 0) drvstep = -3;  //복합과세여부(단말기와 동일해야함)
            else if (KcpCatSetData("amtopt", "N") < 0) drvstep = -3;  //POS 금액 계산 여부
            else if (KcpCatSetData("tot_amt", String.valueOf(amt)) < 0) drvstep = -3;  //총거래금액
            else if (KcpCatSetData("org_amt", String.valueOf(amt)) < 0) drvstep = -3;  //과세금액
            else if (KcpCatSetData("prt_cd", "1") < 0) drvstep = -3;  //전표출력여부, 1-고객용 전표 1장 출력
            else if (KcpCatSetData("prtopt", "0") < 0) drvstep = -3;  //전표문구설정
            else if (KcpCatSetData("signopt", "N") < 0) drvstep = -3;  //서명데이터 요청
            else if (KcpCatSetData("signpad_yn", "N") < 0) drvstep = -3;  //서명패드 포스에 부착 여부
            else if (KcpCatSetData("con_info1", portid.replace("COM", "")) < 0) drvstep = -3;  //통신포트, 번호만 전달
            else if (KcpCatSetData("con_info2", bps) < 0) drvstep = -3;  //통신속도
            if (drvstep < 2) {
                KcpCatReleaseLibrary();
                return drvstep;
            }
            drvstep = 3;
            if (KcpCatTrans("SERIAL") < 0) {
                KcpCatReleaseLibrary();
                return -4;
            }
            if (KcpCatGetData("res_cd", retstr) < 0) drvstep = -5; 
            else {
                rest = new String(retstr, 0, 4);
                if (!rest.equals("0000")) drvstep = -5;
            }
            if (drvstep < 3) {
                KcpCatReleaseLibrary();
                return drvstep;
            }
            KcpCatReleaseLibrary();
            return 0;
        } catch (Exception e) {
            if (drvstep >= 2) {
                KcpCatReleaseLibrary();
            }
            return -1;
        }
    }

    public static int PublishCard(String portid, String bps, PaymentInfoMagcard payinfo)
    {
        byte[] retstr = new byte[32];
        int drvstep = 0;
        String rest;
        double amt = payinfo.getTotal();
        
        try {
            drvstep = KcpModuleVer(retstr);
            if (drvstep != 1) return -1;
            if (amt < 0) {
                if (KcpCatInitLibrary("0420", "E00002") != 1) return -2;    //신용취소
            } else {
                if (KcpCatInitLibrary("0100", "E00002") != 1) return -2;    //신용거래
            }
            drvstep = 2;
            if (KcpCatSetData("ins_mon", payinfo.getExpirationDate()) < 0) drvstep = -3;  //할부여부
            else if (payinfo.getTrack1(true).equals("Y") && KcpCatSetData("tx_type", "21") < 0) drvstep = -3;  //은련결제여부
            else if (KcpCatSetData("cpx_flag", "N") < 0) drvstep = -3;  //복합과세여부(단말기와 동일해야함)
            else if (KcpCatSetData("amtopt", "N") < 0) drvstep = -3;  //POS 금액 계산 여부
            
            if (amt < 0) {  //신용취소
                amt = amt * -1.0;
                if (KcpCatSetData("tot_amt", String.valueOf(amt)) < 0) drvstep = -3;  //총거래금액
                else if (KcpCatSetData("otx_dt", payinfo.getTrack2(true)) < 0) drvstep = -3;  //취소시 원거래 일자
                else if (KcpCatSetData("app_no", payinfo.getTrack3(true)) < 0) drvstep = -3;  //취소시 원거래 번호
            } else {
                if (KcpCatSetData("tot_amt", String.valueOf(amt)) < 0) drvstep = -3;  //총거래금액
                else if (KcpCatSetData("org_amt", String.valueOf(amt)) < 0) drvstep = -3;  //과세금액
            }
            
            if (KcpCatSetData("prt_cd", "2") < 0) drvstep = -3;  //전표출력여부, 2-고객용, 매장용각각 1장 출력
            else if (KcpCatSetData("prtopt", "0") < 0) drvstep = -3;  //전표문구설정
            else if (KcpCatSetData("signopt", "N") < 0) drvstep = -3;  //서명데이터 요청
            else if (KcpCatSetData("signpad_yn", "N") < 0) drvstep = -3;  //서명패드 포스에 부착 여부
            else if (KcpCatSetData("con_info1", portid.replace("COM", "")) < 0) drvstep = -3;  //통신포트, 번호만 전달
            else if (KcpCatSetData("con_info2", bps) < 0) drvstep = -3;  //통신속도
            
            if (drvstep < 2) {
                KcpCatReleaseLibrary();
                return drvstep;
            }
            drvstep = 3;
            if (KcpCatTrans("SERIAL") < 0) {
                KcpCatReleaseLibrary();
                return -4;
            }
            if (KcpCatGetData("res_cd", retstr) < 0) drvstep = -5; 
            else {
                rest = new String(retstr, 0, 4);
                if (!rest.equals("0000")) drvstep = -5;
                String tx_no = "000000000000";
                if (KcpCatGetData("tx_no", retstr) >= 0) tx_no = new String(retstr, 0, 12);
                String app_no = "000000000000";
                if (KcpCatGetData("app_no", retstr) >= 0) app_no = new String(retstr, 0, 12);
                String term_id = "          ";
                if (KcpCatGetData("term_id", retstr) >= 0) term_id = new String(retstr, 0, 10);
                String ms_data = "0000000000000000";
                if (KcpCatGetData("ms_data", retstr) >= 0) ms_data = new String(retstr, 0, 20);
                payinfo.setCardNumber(ms_data);
                payinfo.paymentOK(app_no, tx_no, term_id);
            }
            if (drvstep < 3) {
                KcpCatReleaseLibrary();
                return drvstep;
            }
            KcpCatReleaseLibrary();
            return 0;
        } catch (Exception e) {
            if (drvstep >= 2) {
                KcpCatReleaseLibrary();
            }
            return -1;
        }
    }

}
