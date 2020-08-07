package com.example.aarogyajeevan.Model;

public class YellowZone {

    private double latitude;
    private double longitude;

    public YellowZone(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public YellowZone() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}