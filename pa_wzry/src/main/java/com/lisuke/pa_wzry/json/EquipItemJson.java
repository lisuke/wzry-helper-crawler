/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry.json;

/**
 *
 * @author lisuke
 */
public class EquipItemJson {
    private int item_id;
    private String item_name;
    private int item_type;
    private int price;
    private int total_price;
    private String des1;
    private String des2;

    @Override
    public String toString() {
        return "EquipItemJson{" + "item_id=" + item_id + ", item_name=" + item_name + ", item_type=" + item_type + ", price=" + price + ", total_price=" + total_price + ", des1=" + des1 + ", des2=" + des2 + '}';
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getDes1() {
        return des1;
    }

    public void setDes1(String des1) {
        this.des1 = des1;
    }

    public String getDes2() {
        return des2;
    }

    public void setDes2(String des2) {
        this.des2 = des2;
    }
    
}
