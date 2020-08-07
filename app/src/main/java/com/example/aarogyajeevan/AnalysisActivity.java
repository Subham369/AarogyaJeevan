package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.GreenZone;
import com.example.aarogyajeevan.Model.MyLatLang;
import com.example.aarogyajeevan.Model.RedZone;
import com.example.aarogyajeevan.Model.YellowZone;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,com.google.android.gms.location.LocationListener,IOnLoadLocationlistenerRed,IOnLoadLocationlistenerYellow,IOnLoadLocationlistenerGreen {

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask storageTask;
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
    private String name,zone;


    private GeofencingClient geofencingClient;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE=10001;
    private float GEOFENCE_RADIUS=200;
//    private GeofenceHelper geofenceHelper;

    private GeofenceHelperRed geofenceHelperRed;
    private String GEOFENCE_ID="GEOFENCE_ID";
    private static final String TAG = "TrailMapsActivity";
    private int requestCode=10002;
    private List<LatLng> dangerous_red;
    private List<LatLng> dangerous_yellow;
    private List<LatLng> dangerous_green;
    private DatabaseReference redArea;
    private DatabaseReference yellowArea;
    private DatabaseReference greenArea;
    private IOnLoadLocationlistenerRed locationlistenerRed;
    private IOnLoadLocationlistenerYellow locationlistenerYellow;
    private IOnLoadLocationlistenerGreen locationlistenerGreen;
    LocationRequest locaRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        firebaseAuth=FirebaseAuth.getInstance();
//        storageReference= FirebaseStorage.getInstance().getReference("WorkerLocation");
//        databaseReference= FirebaseDatabase.getInstance().getReference("WorkerLocation");

        zone=getIntent().getStringExtra("zone");

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

                        Toast.makeText(AnalysisActivity.this, "You must accept permission", Toast.LENGTH_SHORT).show();
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
        geofenceHelperRed=new GeofenceHelperRed(this);




        if (requestSinglePermission()){

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
        enableLocation();
//        // Add a marker in Sydney and move the camera
//        LatLng india = new LatLng(20.5937, 78.9629);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india,4.0f));
//        if (latLng!=null){
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f));
//        }
//
//        for (int i=0;i<latAdapter.size();i++) {
//            LatLng location=new LatLng(latAdapter.get(i),lonAdapter.get(i));
//            mMap.addMarker(new MarkerOptions().position(location));
//        }

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

       if (zone.equals("1")){
           GreenZone greenZone=new GreenZone(location.getLatitude(),location.getLongitude());
           FirebaseDatabase.getInstance().getReference("GreenZone").child(userId).setValue(greenZone).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       Toast.makeText(AnalysisActivity.this, "Location saved", Toast.LENGTH_SHORT).show();

                   else
                       Toast.makeText(AnalysisActivity.this, "Location not saved", Toast.LENGTH_SHORT).show();
               }

           });
       }

       if (zone.equals("2")){
           YellowZone yellowZone=new YellowZone(location.getLatitude(),location.getLongitude());
           FirebaseDatabase.getInstance().getReference("YellowZone").child(userId).setValue(yellowZone).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       Toast.makeText(AnalysisActivity.this, "Location saved", Toast.LENGTH_SHORT).show();

                   else
                       Toast.makeText(AnalysisActivity.this, "Location not saved", Toast.LENGTH_SHORT).show();
               }

           });
       }

       if (zone.equals("3")){
           RedZone redZone=new RedZone(location.getLatitude(),location.getLongitude());
           FirebaseDatabase.getInstance().getReference("RedZone").child(userId).setValue(redZone).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       Toast.makeText(AnalysisActivity.this, "Location saved", Toast.LENGTH_SHORT).show();

                   else
                       Toast.makeText(AnalysisActivity.this, "Location not saved", Toast.LENGTH_SHORT).show();
               }

           });
       }

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












    private void inArea() {

        redArea= FirebaseDatabase.getInstance().getReference("RedZone");
        locationlistenerRed=this;
        //Load from Firebase
        redArea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RedZone> list_red=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    RedZone latLng=(locationSnapShot.getValue(RedZone.class));
                    list_red.add(latLng);
                }
//                locationlistenerRed.onLoadLocationSuccess(list_red);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                locationlistener.onLoadLocationFailed(databaseError.getMessage());
            }
        });

        redArea.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Update dangerous area
                List<RedZone> list_red=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {
                    RedZone latLng=locationSnapShot.getValue(RedZone.class);
                    list_red.add(latLng);
                }

                locationlistenerRed.onLoadLocationSuccess(list_red);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        yellowArea= FirebaseDatabase.getInstance().getReference("YellowZone");
        locationlistenerYellow=this;
        //Load from Firebase
        yellowArea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<YellowZone> list_yellow=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    YellowZone latLng=(locationSnapShot.getValue(YellowZone.class));
                    list_yellow.add(latLng);
                }
                locationlistenerYellow.onLoadLocationSuccessYellow(list_yellow);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                locationlistener.onLoadLocationFailed(databaseError.getMessage());
            }
        });

        yellowArea.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Update dangerous area
                List<YellowZone> list_yellow=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {
                    YellowZone latLng=locationSnapShot.getValue(YellowZone.class);
                    list_yellow.add(latLng);
                }

                locationlistenerYellow.onLoadLocationSuccessYellow(list_yellow);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        greenArea= FirebaseDatabase.getInstance().getReference("GreenZone");
        locationlistenerGreen=this;
        //Load from Firebase
        greenArea.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<GreenZone> list_green=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    GreenZone latLng=(locationSnapShot.getValue(GreenZone.class));
                    list_green.add(latLng);
                }
                locationlistenerGreen.onLoadLocationSuccessGreen(list_green);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                locationlistener.onLoadLocationFailed(databaseError.getMessage());
            }
        });

        greenArea.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Update dangerous area
                List<GreenZone> list_green=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {
                    GreenZone latLng=locationSnapShot.getValue(GreenZone.class);
                    list_green.add(latLng);
                }

                locationlistenerGreen.onLoadLocationSuccessGreen(list_green);

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

    private void addGeofence(){

        for (LatLng latLng:dangerous_red) {
            Geofence geofence = geofenceHelperRed.getGeofence(GEOFENCE_ID, latLng, GEOFENCE_RADIUS, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
            GeofencingRequest geofencingRequest = geofenceHelperRed.geofencingRequest(geofence);
            PendingIntent pendingIntent = geofenceHelperRed.getPendingIntent();
            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: Geofence is added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String errorMessage = geofenceHelperRed.getErrorString(e);
                    Log.d(TAG, "onFailure:" + errorMessage);
                }
            });
        }
    }

//    private void addMarker(){
//
//        for(LatLng latLng:dangerous) {
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//            mMap.addMarker(markerOptions);
//        }
//    }

    private void addCircle() {

            for (LatLng latLng : dangerous_red) {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng);
                circleOptions.radius(GEOFENCE_RADIUS);
                circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
                circleOptions.fillColor(Color.argb(64, 255, 0, 0));
                circleOptions.strokeWidth(4f);
                mMap.addCircle(circleOptions);
            }

//        if (dangerous_yellow.size() > 0) {
//            for (LatLng latLng : dangerous_yellow) {
//                CircleOptions circleOptions = new CircleOptions();
//                circleOptions.center(latLng);
//                circleOptions.radius(GEOFENCE_RADIUS);
//                circleOptions.strokeColor(Color.argb(255, 255, 165, 0));
//                circleOptions.fillColor(Color.argb(64, 255, 165, 0));
//                circleOptions.strokeWidth(4f);
//                mMap.addCircle(circleOptions);
//            }
//        }
//
//        if (dangerous_green.size() > 0) {
//            for (LatLng latLng : dangerous_green) {
//                CircleOptions circleOptions = new CircleOptions();
//                circleOptions.center(latLng);
//                circleOptions.radius(GEOFENCE_RADIUS);
//                circleOptions.strokeColor(Color.argb(255, 0, 128, 0));
//                circleOptions.fillColor(Color.argb(64, 0, 128, 0));
//                circleOptions.strokeWidth(4f);
//                mMap.addCircle(circleOptions);
//            }
//        }
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
    public void onLoadLocationSuccess(List<RedZone> latLngs) {

        dangerous_red=new ArrayList<>();
        for (RedZone myLatLang:latLngs)
        {
            LatLng convert=new LatLng(myLatLang.getLatitude(),myLatLang.getLongitude());
            dangerous_red.add(convert);
        }

        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AnalysisActivity.this);


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

    @Override
    public void onLoadLocationSuccessYellow(List<YellowZone> latLngs) {

        dangerous_yellow=new ArrayList<>();
        for (YellowZone myLatLang:latLngs)
        {
            LatLng convert=new LatLng(myLatLang.getLatitude(),myLatLang.getLongitude());
            dangerous_yellow.add(convert);
        }

        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AnalysisActivity.this);


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
    public void onLoadLocationFailedYellow(String message) {

    }

    @Override
    public void onLoadLocationSuccessGreen(List<GreenZone> latLngs) {

        dangerous_green=new ArrayList<>();
        for (GreenZone myLatLang:latLngs)
        {
            LatLng convert=new LatLng(myLatLang.getLatitude(),myLatLang.getLongitude());
            dangerous_green.add(convert);
        }

        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AnalysisActivity.this);


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
    public void onLoadLocationFailedGreen(String message) {

    }

}
