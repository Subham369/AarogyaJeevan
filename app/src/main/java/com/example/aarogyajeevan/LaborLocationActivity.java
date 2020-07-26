package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.PermissionRequest;



import java.util.ArrayList;

public class LaborLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,com.google.android.gms.location.LocationListener {

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask storageTask;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL=2000;
    private long FASTEST_INTERVAL=5000;
    private LatLng latLng;
    private boolean isPermission;
    ArrayList<Double> latAdapter=new ArrayList<>();
    ArrayList<Double> lonAdapter=new ArrayList<>();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_location);
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("WorkerLocation");
        databaseReference= FirebaseDatabase.getInstance().getReference("WorkerLocation");

//        usersRef=rootRef.child();
//        rootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
//                {
//                    Double lat=postSnapshot.child("latitude").getValue(Double.class);
//                    latAdapter.add(lat);
//                }
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
//                {
//                    Double lon=postSnapshot.child("longitude").getValue(Double.class);
//                    lonAdapter.add(lon);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        if (requestSinglePermission()){

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();

    }

    private boolean checkLocation() {
        if (!isLocationEnabled()){
            showAlert();
        }
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private void showAlert(){
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location");
        dialog.setMessage("Your Location Settings is set to 'Off'. \nPlease enable location to use this feature");
        dialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isPermission=true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

                if (response.isPermanentlyDenied())
                    isPermission=false;
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
        return  isPermission;
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

        // Add a marker in Sydney and move the camera
        LatLng india = new LatLng(20.5937, 78.9629);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india,4.0f));
        if (latLng!=null){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f));
        }

        for (int i=0;i<latAdapter.size();i++) {
            LatLng location=new LatLng(latAdapter.get(i),lonAdapter.get(i));
            mMap.addMarker(new MarkerOptions().position(location));
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        startLocationUpdate();
        mLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation!=null)
            startLocationUpdate();
        else
            Toast.makeText(this, "Location not detected", Toast.LENGTH_SHORT).show();
    }

    private void startLocationUpdate() {
        mLocationRequest=LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(UPDATE_INTERVAL).setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

//        String msg="Updated Location :"+Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        LocationHelper locationHelper=new LocationHelper(location.getLatitude(),location.getLongitude());
        FirebaseDatabase.getInstance().getReference("WorkerLocation").child(userId).setValue(locationHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(LaborLocationActivity.this, "Location saved", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(LaborLocationActivity.this, "Location not saved", Toast.LENGTH_SHORT).show();
            }

        });

        latLng=new LatLng(location.getLatitude(),location.getLongitude());
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient!=null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    public void clkLocationOther(View view) {
        Intent intent=new Intent(getApplicationContext(), LocationActivity.class);
        startActivity(intent);
    }

}
