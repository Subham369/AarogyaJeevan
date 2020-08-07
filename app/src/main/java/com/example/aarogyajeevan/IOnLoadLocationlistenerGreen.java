package com.example.aarogyajeevan;

import com.example.aarogyajeevan.Model.GreenZone;
import com.example.aarogyajeevan.Model.YellowZone;

import java.util.List;

public interface IOnLoadLocationlistenerGreen {

    void onLoadLocationSuccessGreen(List<GreenZone> latLngs);
    void onLoadLocationFailedGreen(String message);
}