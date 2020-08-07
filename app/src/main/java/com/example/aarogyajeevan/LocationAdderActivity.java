package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class LocationAdderActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private EditText latitude,longitude;
    DatabaseReference databaseReference;
    Button location_submit;
    List<LatLng> dangerous;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_adder);

        latitude=findViewById(R.id.add_latitude);
        longitude=findViewById(R.id.add_longitude);
        location_submit=findViewById(R.id.location_submit);

        location_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLat=latitude.getText().toString();
                String strLon=longitude.getText().toString();

                hotspotMarker(strLat,strLon);
            }
        });
    }


    private void hotspotMarker(String strLat, String strLon) {

        HashMap<String,Double> hashMap=new HashMap<>();
        hashMap.put("latitude",Double.valueOf(strLat));
        hashMap.put("longitude",Double.valueOf(strLon));

//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        String userId = firebaseUser.getUid();
        String position=String.valueOf(i++);
        databaseReference = FirebaseDatabase.getInstance().getReference("DangerousArea").child("MyLatLang").child(position);
        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LocationAdderActivity.this, "Details saved successfully", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(LocationAdderActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
