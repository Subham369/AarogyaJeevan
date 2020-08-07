package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.MyLatLang;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GeofencingActivity extends FragmentActivity implements OnMapReadyCallback,IOnLoadLocationlistener{

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE=10001;
    private float GEOFENCE_RADIUS=200;
    private GeofenceHelper geofenceHelper;
    private String GEOFENCE_ID="GEOFENCE_ID";
    private static final String TAG = "TrailMapsActivity";
    private int requestCode=10002;
    private List<LatLng> dangerous;
    private DatabaseReference myCity;
    private IOnLoadLocationlistener locationlistener;
    LocationRequest locaRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        builtLocationRequest();
                        buildLocationCallback();
//                        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                        inArea();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(GeofencingActivity.this, "You must accept permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient= LocationServices.getGeofencingClient(this);
        geofenceHelper=new GeofenceHelper(this);
    }



    private void inArea() {

        myCity= FirebaseDatabase.getInstance().getReference("DangerousArea").child("MyLatLang");
        locationlistener=this;
        //Load from Firebase
        myCity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MyLatLang> list=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    MyLatLang latLng=(locationSnapShot.getValue(MyLatLang.class));
                    list.add(latLng);
                }
                locationlistener.onLoadLocationSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                locationlistener.onLoadLocationFailed(databaseError.getMessage());
            }
        });

        myCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Update dangerous area
                List<MyLatLang> list=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {
                    MyLatLang latLng=locationSnapShot.getValue(MyLatLang.class);
                    list.add(latLng);
                }

//                locationlistener.onLoadLocationSuccess(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableLocation();
//        mMap.setOnMapLongClickListener(this);
    }

    private void enableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int request, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(request==FINE_LOCATION_ACCESS_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
            }
            else
            {

            }
        }

        if(request==requestCode){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You can add geofences", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Background location is necessary for adding geofences", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onMapLongClick(LatLng latLng) {
//
//        if (Build.VERSION.SDK_INT>=29){
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED){
//                handLongClick(latLng);
//            }
//            else
//            {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
//                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},requestCode);
//                }
//                else
//                {
//                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},requestCode);
//                }
//            }
//        }
//        else{
//            handLongClick(latLng);
//        }
//    }
//    private void handLongClick(LatLng latLng){
//        mMap.clear();
//        addMarker(latLng);
//        addGeofence(latLng,GEOFENCE_RADIUS);
//    }

    private void addGeofence(){

        for (LatLng latLng:dangerous) {
            Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, GEOFENCE_RADIUS, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
            GeofencingRequest geofencingRequest = geofenceHelper.geofencingRequest(geofence);
            PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: Geofence is added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String errorMessage = geofenceHelper.getErrorString(e);
                    Log.d(TAG, "onFailure:" + errorMessage);
                }
            });
        }
    }

    private void addMarker(){

        for(LatLng latLng:dangerous) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            mMap.addMarker(markerOptions);
        }
    }

    private void addCircle(){

        for (LatLng latLng:dangerous) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(GEOFENCE_RADIUS);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(64, 255, 0, 0));
            circleOptions.strokeWidth(4f);
            mMap.addCircle(circleOptions);
        }
    }

    private void builtLocationRequest() {

        locaRequest=new LocationRequest();
        locaRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locaRequest.setInterval(5000);
        locaRequest.setFastestInterval(3000);
        locaRequest.setSmallestDisplacement(10f);

    }

    private void buildLocationCallback(){

        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap!=null)
                {
//                    addMarker();
//                    addCircle();
//                    addGeofence();

                }
            }
        };

    }


    @Override
    public void onLoadLocationSuccess(List<MyLatLang> latLngs) {

        dangerous=new ArrayList<>();
        for (MyLatLang myLatLang:latLngs)
        {
            LatLng convert=new LatLng(myLatLang.getLatitude(),myLatLang.getLongitude());
            dangerous.add(convert);
        }
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(GeofencingActivity.this);


        //clear Map and load again
        if (mMap !=null)
        {
//            mMap.clear();

//            addMarker();
            addCircle();
            addGeofence();
        }
    }

    @Override
    public void onLoadLocationFailed(String message) {

    }
}
