/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.bobview;

/**
 *
 * @author ckddn
 */
public class Owner {

    private String id;
    private String name;
    private String restaurant;
    private boolean is_active;
    private boolean registered;
  //  private int r_no;
    
    public Owner(String id, String name, String restaurant, boolean is_active, boolean registered) {
  
        this.id = id;
        this.name = name;
        this.restaurant = restaurant;
        this.is_active = is_active;
        this.registered = registered;
    }
    
  /*  public Owner(int no, String id, String name, String restaurant, boolean registered, int r_no) {
        this.no = no;
        this.id = id;
        this.name = name;
        this.restaurant = restaurant;
        this.registered = registered;
        this.r_no = r_no;
    } */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public boolean IsRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isActive() {
        return this.is_active;
    }

    public void setActive(boolean registered) {
        this.is_active = is_active;
    }

}
