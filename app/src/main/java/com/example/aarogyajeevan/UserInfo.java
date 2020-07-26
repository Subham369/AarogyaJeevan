package com.example.aarogyajeevan;

public class UserInfo {
    private String id;
    private String username;
    private String fullname;
    private String dob;
    private String phone_number;
    private String email;
    private String workerInfo;
    private String position;
    private String address;
    private String occupation;
    private String imageURLI;
    private String bio;
    private String imageURLW;
    private String status;
    private String search;

    public UserInfo() {
    }

    public UserInfo(String id, String username,String fullname, String dob, String phone_number, String email, String workerInfo, String position, String address, String occupation, String imageURLI, String bio, String imageURLW, String status, String search) {
        this.id = id;
        if (username.trim().equals(""))
        {
            username="No User Name";
        }
        this.username = username;
        if (fullname.trim().equals(""))
        {
            fullname="No Full Name";
        }
        this.fullname = fullname;
        this.dob = dob;
        this.phone_number = phone_number;
        this.email = email;
        this.workerInfo = workerInfo;
        this.position = position;
        this.address = address;
        this.occupation = occupation;
        this.imageURLI = imageURLI;
        this.bio = bio;
        this.imageURLW = imageURLW;
        this.status = status;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkerInfo() {
        return workerInfo;
    }

    public void setWorkerInfo(String workerInfo) {
        this.workerInfo = workerInfo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImageURLI() {
        return imageURLI;
    }

    public void setImageURLI(String imageURLI) {
        this.imageURLI = imageURLI;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageURLW() {
        return imageURLW;
    }

    public void setImageURLW(String imageURLW) {
        this.imageURLW = imageURLW;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
