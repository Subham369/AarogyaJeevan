package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Model.MyLatLang;
import com.example.aarogyajeevan.Model.Symptoms;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserReportActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private TextView txt_report;
    private String analysis;
    private int f=0,k=0;
    private String zone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        txt_report=findViewById(R.id.report_txt);

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String patientId=firebaseUser.getUid();
        reference= FirebaseDatabase.getInstance().getReference("symptoms").child(patientId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Symptoms> list=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    Symptoms symp=(locationSnapShot.getValue(Symptoms.class));
                    list.add(symp);
                }

                for (Symptoms i:list){
                    analysis=i.getSymp().toLowerCase().trim();
                    if ("high fever".equals(analysis)){
                        f++;
                    }
                    if (analysis.equals("frequent cold")){
                        f++;
                    }
                    else if (analysis.equals("dry cough")){
                        f++;
                    }
                    else if (analysis.equals("runny noise")||analysis.equals("congestion")){
                        f++;
                    }

                    else if (analysis.equals("stomach ache")){
                        f++;
                    }
//                    else if (analysis.equals("stomach ache")){
//                        f++;
//                    }

                    else if (analysis.equals("fever")|| analysis.equals("chills")){
                        f++;
                    }

                    else if (analysis.equals("headache")){
                        f++;
                    }
//                    else if (analysis.equals("stomach ache")){
//                        f++;
//                    }
                    else if (analysis.equals("loss of taste")||analysis.equals("loss of smell")){
                        f++;
                    }

//                    else if (analysis.equals("stomach ache")){
//                        f++;
//                    }

                    else if (analysis.equals("nausea")||analysis.equals("vomiting")){
                        f++;
                    }

                    else if (analysis.equals("muscle ache")||analysis.equals("body ache")){
                        f++;
                    }

                    else if (analysis.equals("diarrhea")){
                        f++;
                    }
                    else if (analysis.equals("fatigue")){
                        f++;
                    }
                    else{
                        k++;
                    }
                }
                if (f>k||f>7){
                    txt_report.setBackgroundColor(Color.RED);
                    txt_report.setText("You can have symptoms of COVID-19 please contact to the nearest COVID-19 Hospitals as soon as possible");
                    zone="3";
                }
                else if (k>f&&f<4){
                    txt_report.setBackgroundColor(Color.GREEN);
                    txt_report.setText("You can are in safe zone keep washing your hands,maintain distance,prevent going to crowdy places,wear mask and try to boost your immunity.");
                    zone="1";
                }
                else {
                    txt_report.setBackgroundColor(Color.YELLOW);
                    txt_report.setText("You are in warning zone please stay in home isolated and wear mask frequently.May God protect you from COVID-19");
                    zone="2";
                }
                f=0;
                k=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnBack(View view) {
        Intent intent=new Intent(UserReportActivity.this,AppFeatures.class);
        startActivity(intent);
        finish();
    }

    public void btnAnalysis(View view) {
        Intent intent=new Intent(UserReportActivity.this,AnalysisActivity.class);
        intent.putExtra("zone",zone);
        startActivity(intent);
    }
}
