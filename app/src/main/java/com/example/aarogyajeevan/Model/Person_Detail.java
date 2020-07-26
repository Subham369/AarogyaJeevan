package com.example.aarogyajeevan.Model;

public class Person_Detail {
    public Person_Detail(String pers_name, String pers_age, String pers_gender, String pers_location, String pers_recent_visit) {
        this.pers_name = pers_name;
        this.pers_age = pers_age;
        this.pers_gender = pers_gender;
        this.pers_location = pers_location;
        this.pers_recent_visit = pers_recent_visit;
    }

    private String pers_name;
    private String pers_age;
    private String pers_gender;
    private String pers_location;
    private String pers_recent_visit;

    public Person_Detail() {
    }

    public String getPers_name() {
        return pers_name;
    }

    public void setPers_name(String pers_name) {
        this.pers_name = pers_name;
    }

    public String getPers_age() {
        return pers_age;
    }

    public void setPers_age(String pers_age) {
        this.pers_age = pers_age;
    }

    public String getPers_gender() {
        return pers_gender;
    }

    public void setPers_gender(String pers_gender) {
        this.pers_gender = pers_gender;
    }

    public String getPers_location() {
        return pers_location;
    }

    public void setPers_location(String pers_location) {
        this.pers_location = pers_location;
    }

    public String getPers_recent_visit() {
        return pers_recent_visit;
    }

    public void setPers_recent_visit(String pers_recent_visit) {
        this.pers_recent_visit = pers_recent_visit;
    }
}

