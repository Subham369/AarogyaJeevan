package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.Doctor;
import com.example.aarogyajeevan.Model.HospitalCode;
import com.example.aarogyajeevan.Model.MyLatLang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerifyEncryptionActivity extends AppCompatActivity {

    private EditText encryption_code;
    private DatabaseReference reference,rootref;
    private String code;
    private int length;
    private List<HospitalCode> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_encryption);
        encryption_code=findViewById(R.id.encryption_code);

        String doctorUid= getIntent().getStringExtra("doctorid");

        reference= FirebaseDatabase.getInstance().getReference("HospitalCode").child(doctorUid);
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<HospitalCode> list=new ArrayList<>();
                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    HospitalCode user=(locationSnapShot.getValue(HospitalCode.class));
                    list.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
//                HospitalCode user=dataSnapshot.getValue(HospitalCode.class);
//                code=user.getDoctorHospitalCode();

                for (DataSnapshot locationSnapShot:dataSnapshot.getChildren())
                {

                    HospitalCode user=(locationSnapShot.getValue(HospitalCode.class));
                    list.add(user);
                }

                length=list.size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnCheckEncryption(View view) {
        String code_txxt=encryption_code.getText().toString().trim();
        for (HospitalCode hospitalCode:list){
            if (code_txxt.compareTo(hospitalCode.getDoctorHospitalCode().toString())==0){
                Toast.makeText(this, "Code verified!!!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(VerifyEncryptionActivity.this,HospitalHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
