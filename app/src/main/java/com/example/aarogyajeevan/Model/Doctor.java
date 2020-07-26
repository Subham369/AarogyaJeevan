package com.example.aarogyajeevan.Model;

public class Doctor {
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String address;
    private String position;

    public Doctor() {
    }

    public Doctor(String userName, String email, String phone, String password, String address, String position) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
