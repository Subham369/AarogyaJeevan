package com.example.aarogyajeevan.Model;


public class WestDistrict {
    private String transportation_id;
    private String trans_name;
    private String trans_phone;
    private String trans_purpose_details;
    private String trans_vechile_number;
    private String trans_email;
    private String trans_dl_number;
    private String trans_source;
    private String trans_destination;
    private String trans_fromdate;
    private String trans_todate;
    private String trans_progress;

    public WestDistrict() {
    }

    public WestDistrict(String transportation_id, String trans_name, String trans_phone, String trans_purpose_details, String trans_vechile_number, String trans_email, String trans_dl_number, String trans_source, String trans_destination, String trans_fromdate, String trans_todate, String trans_progress) {
        this.transportation_id = transportation_id;
        this.trans_name = trans_name;
        this.trans_phone = trans_phone;
        this.trans_purpose_details = trans_purpose_details;
        this.trans_vechile_number = trans_vechile_number;
        this.trans_email = trans_email;
        this.trans_dl_number = trans_dl_number;
        this.trans_source = trans_source;
        this.trans_destination = trans_destination;
        this.trans_fromdate = trans_fromdate;
        this.trans_todate = trans_todate;
        this.trans_progress = trans_progress;
    }

    public String getTransportation_id() {
        return transportation_id;
    }

    public void setTransportation_id(String transportation_id) {
        this.transportation_id = transportation_id;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public String getTrans_phone() {
        return trans_phone;
    }

    public void setTrans_phone(String trans_phone) {
        this.trans_phone = trans_phone;
    }

    public String getTrans_purpose_details() {
        return trans_purpose_details;
    }

    public void setTrans_purpose_details(String trans_purpose_details) {
        this.trans_purpose_details = trans_purpose_details;
    }

    public String getTrans_vechile_number() {
        return trans_vechile_number;
    }

    public void setTrans_vechile_number(String trans_vechile_number) {
        this.trans_vechile_number = trans_vechile_number;
    }

    public String getTrans_email() {
        return trans_email;
    }

    public void setTrans_email(String trans_email) {
        this.trans_email = trans_email;
    }

    public String getTrans_dl_number() {
        return trans_dl_number;
    }

    public void setTrans_dl_number(String trans_dl_number) {
        this.trans_dl_number = trans_dl_number;
    }

    public String getTrans_source() {
        return trans_source;
    }

    public void setTrans_source(String trans_source) {
        this.trans_source = trans_source;
    }

    public String getTrans_destination() {
        return trans_destination;
    }

    public void setTrans_destination(String trans_destination) {
        this.trans_destination = trans_destination;
    }

    public String getTrans_fromdate() {
        return trans_fromdate;
    }

    public void setTrans_fromdate(String trans_fromdate) {
        this.trans_fromdate = trans_fromdate;
    }

    public String getTrans_todate() {
        return trans_todate;
    }

    public void setTrans_todate(String trans_todate) {
        this.trans_todate = trans_todate;
    }

    public String getTrans_progress() {
        return trans_progress;
    }

    public void setTrans_progress(String trans_progress) {
        this.trans_progress = trans_progress;
    }
}

