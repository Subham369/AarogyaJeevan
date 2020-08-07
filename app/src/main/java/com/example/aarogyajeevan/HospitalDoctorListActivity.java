package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.aarogyajeevan.Adapter.DoctorListAdapter;
import com.example.aarogyajeevan.Adapter.HospitalDoctorListAdapter;
import com.example.aarogyajeevan.Model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalDoctorListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HospitalDoctorListAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<Doctor> mUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_list);
        mRecyclerView=findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HospitalDoctorListActivity.this));
        mRecyclerView.setHasFixedSize(true);


        mUpload=new ArrayList<>();
        int size=mUpload.size();
        if (size>0) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Doctor upload = postSnapshot.getValue(Doctor.class);
                        mUpload.add(upload);
                    }
                    mAdapter = new HospitalDoctorListAdapter(HospitalDoctorListActivity.this, mUpload);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HospitalDoctorListActivity.this, "Error:" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        else
        {
            Toast.makeText(this, "There are no permission request available", Toast.LENGTH_SHORT).show();
        }

    }
}
