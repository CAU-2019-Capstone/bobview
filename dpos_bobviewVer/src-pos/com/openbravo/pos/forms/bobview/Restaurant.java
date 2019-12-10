/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.bobview;

/**
 *
 * @author lee
 */
public class Restaurant {
    private String name;
    private String address;
    private String phone;
    private int leftnum;
    private int resnum;
    private int foodtype;
    private int break_num;
    private String Break_time = "";
    private String hours = "";
    private String holiday_num = "";
    private int h_type;
    private String time = "";
    private String image = "";
    private String owner_request = "";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner_request() {
        return owner_request;
    }

    public void setOwner_request(String owner_request) {
        this.owner_request = owner_request;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = ",\"time\":\"" + time + "\"";
    }

    public int getH_type() {
        return h_type;
    }

    public void setH_type(int h_type) {
        this.h_type = h_type;
    }

    public int getBreak_num() {
        return break_num;
    }

    public void setBreak_num(int break_num) {
        this.break_num = break_num;
    }

    public String getBreak_time() {
        return Break_time;
    }

    public void setBreak_time(String Break_time) {
        this.Break_time = ",\"break\":\"" + Break_time + "\"";
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getHoliday_num() {
        return holiday_num;
    }

    public void setHoliday_num(String holiday_num) {
        this.holiday_num = holiday_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLeftnum() {
        return leftnum;
    }

    public void setLeftnum(int leftnum) {
        this.leftnum = leftnum;
    }

    public int getResnum() {
        return resnum;
    }

    public void setResnum(int resnum) {
        this.resnum = resnum;
    }

    public int getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(int foodtype) {
        this.foodtype = foodtype;
    }
}
