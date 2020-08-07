package com.example.aarogyajeevan;

import com.example.aarogyajeevan.Model.RedZone;
import com.example.aarogyajeevan.Model.YellowZone;

import java.util.List;

public interface IOnLoadLocationlistenerYellow {

    void onLoadLocationSuccessYellow(List<YellowZone> latLngs);
    void onLoadLocationFailedYellow(String message);
}