package com.example.aarogyajeevan;

import com.example.aarogyajeevan.Model.MyLatLang;

import java.util.List;

public interface IOnLoadLocationlistener {

    void onLoadLocationSuccess(List<MyLatLang> latLngs);
    void onLoadLocationFailed(String message);
}