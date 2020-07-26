package com.example.aarogyajeevan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Toast.makeText(context, "Geofence triggered", Toast.LENGTH_SHORT).show();
        NotificationHelper notificationHelper=new NotificationHelper(context);
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofencing event");
            return;
        }
        List<Geofence> geofenceList=geofencingEvent.getTriggeringGeofences();
        Location location=geofencingEvent.getTriggeringLocation();
        for (Geofence geofence:geofenceList) {
            Log.d(TAG, "onReceive:" + geofence.getRequestId());
        }
        int transitionType = geofencingEvent.getGeofenceTransition();
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "You have enter the hotspot region", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER","You have enter the hotspot region",GeofencingActivity.class);
                break;

            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "You are inside the hotspot region", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL","You are inside the hotspot region", GeofencingActivity.class);
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "You are out from the hotspot region", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT","You are out from the hotspot region", GeofencingActivity.class);
                break;
        }
    }
}
