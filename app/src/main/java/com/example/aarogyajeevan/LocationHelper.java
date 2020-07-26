package com.example.aarogyajeevan;

public class LocationHelper {

    private double latitude;
    private double longitude;

    public LocationHelper(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationHelper() {
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