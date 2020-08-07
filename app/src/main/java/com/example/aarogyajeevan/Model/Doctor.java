package com.example.aarogyajeevan.Model;

public class Doctor {
    private String id;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String address;
    private String position;
    private String hospital;
    private String specialization;
    private String bookingtime;
    private String available;
    private String description;
    private String code;

    public Doctor() {
    }

    public Doctor(String id,String userName, String email, String phone, String password, String address, String position,String hospital,String specialization,String bookingtime,String available,String description,String code) {
        this.id=id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.position = position;
        this.hospital=hospital;
        this.specialization=specialization;
        this.bookingtime=bookingtime;
        this.available=available;
        this.description=description;
        this.code=code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getBookingtime() {
        return bookingtime;
    }

    public void setBookingtime(String bookingtime) {
        this.bookingtime = bookingtime;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
