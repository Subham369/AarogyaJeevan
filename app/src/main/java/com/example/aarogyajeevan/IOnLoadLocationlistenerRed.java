package com.example.aarogyajeevan;

import com.example.aarogyajeevan.Model.MyLatLang;
import com.example.aarogyajeevan.Model.RedZone;

import java.util.List;

public interface IOnLoadLocationlistenerRed {

    void onLoadLocationSuccess(List<RedZone> latLngs);
    void onLoadLocationFailed(String message);
}