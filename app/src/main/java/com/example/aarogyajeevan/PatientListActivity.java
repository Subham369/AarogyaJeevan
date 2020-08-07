package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.aarogyajeevan.Adapter.PassPermissionAdapter;
import com.example.aarogyajeevan.Adapter.PatientListAdapter;
import com.example.aarogyajeevan.Model.EastDistrictProgress;
import com.example.aarogyajeevan.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PatientListAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<Patient> mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PatientListActivity.this));
        mRecyclerView.setHasFixedSize(true);


        mUpload=new ArrayList<>();

            databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Patient upload = postSnapshot.getValue(Patient.class);
                        mUpload.add(upload);
                    }
                    mAdapter = new PatientListAdapter(PatientListActivity.this, mUpload);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PatientListActivity.this, "Error:" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }
}
