package com.example.aarogyajeevan.Model;

public class HospitalProducts {

    private String pid,totalbed,image,price,Hname,Hcontact,Hlocation,date,time;

    public HospitalProducts() {
    }

    public HospitalProducts(String pid, String totalbed, String image, String price, String hname, String hcontact, String hlocation, String date, String time) {
        this.pid = pid;
        this.totalbed = totalbed;
        this.image = image;
        this.price = price;
        Hname = hname;
        Hcontact = hcontact;
        Hlocation = hlocation;
        this.date = date;
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTotalbed() {
        return totalbed;
    }

    public void setTotalbed(String totalbed) {
        this.totalbed = totalbed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHname() {
        return Hname;
    }

    public void setHname(String hname) {
        Hname = hname;
    }

    public String getHcontact() {
        return Hcontact;
    }

    public void setHcontact(String hcontact) {
        Hcontact = hcontact;
    }

    public String getHlocation() {
        return Hlocation;
    }

    public void setHlocation(String hlocation) {
        Hlocation = hlocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}