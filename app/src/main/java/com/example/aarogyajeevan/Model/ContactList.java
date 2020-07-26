package com.example.aarogyajeevan.Model;

public class ContactList {
    private String textname,textDescription,textphone;

    public ContactList(String textname, String textDescription, String textphone) {
        this.textname = textname;
        this.textDescription = textDescription;
        this.textphone = textphone;
    }

    public ContactList() {
    }

    public String getTextname() {
        return textname;
    }

    public void setTextname(String textname) {
        this.textname = textname;
    }

    public String getTextphone() {
        return textphone;
    }

    public void setTextphone(String textphone) {
        this.textphone = textphone;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }
}
