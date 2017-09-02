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
public class HeroListJson {
    
    int ename;
    String cname;
    String title;
    int hero_type;
    int new_type;
    int pay_type;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }
    public int getEname() {
        return ename;
    }

    public void setEname(int ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHero_type() {
        return hero_type;
    }

    public void setHero_type(int hero_type) {
        this.hero_type = hero_type;
    }

    public int getNew_type() {
        return new_type;
    }

    public void setNew_type(int new_type) {
        this.new_type = new_type;
    }

    @Override
    public String toString() {
        return "HeroListJson{" + "ename=" + ename + ", cname=" + cname + ", title=" + title + ", hero_type=" + hero_type + ", new_type=" + new_type + ", pay_type=" + pay_type + '}';
    }
    
}
