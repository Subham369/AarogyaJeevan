package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aarogyajeevan.Model.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorDescActivity extends AppCompatActivity {

    private TextView txt_doctor_name,txt_doctor_email,txt_doctor_specialization,txt_doctor_description,txt_doctor_hospital,txt_doctor_time,doctor_busy;
    private DatabaseReference reference;
    private DatabaseReference rootref;
    private Button btn_appointment;
    private String toemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_desc);
        txt_doctor_name=findViewById(R.id.txt_doctor_name);
        txt_doctor_email=findViewById(R.id.txt_doctor_email);
        txt_doctor_specialization=findViewById(R.id.txt_doctor_specialization);
        txt_doctor_description=findViewById(R.id.txt_doctor_description);
        txt_doctor_hospital=findViewById(R.id.txt_doctor_hospital);
        txt_doctor_time=findViewById(R.id.txt_doctor_time);
        doctor_busy=findViewById(R.id.doctor_busy);
        btn_appointment=findViewById(R.id.btn_appointment);

        Intent intent=getIntent();
        String userid=intent.getStringExtra("userid");

        reference= FirebaseDatabase.getInstance().getReference("Doctor").child(userid);
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor=dataSnapshot.getValue(Doctor.class);
                txt_doctor_name.setText(doctor.getUserName());
                txt_doctor_email.setText(doctor.getEmail());
                toemail=doctor.getEmail();
                txt_doctor_specialization.setText(doctor.getEmail());
                txt_doctor_description.setText(doctor.getEmail());
                txt_doctor_hospital.setText(doctor.getEmail());
                txt_doctor_time.setText(doctor.getEmail());
                if (doctor.getAvailable().equals("available")){
                    doctor_busy.setVisibility(View.GONE);
                    btn_appointment.setEnabled(true);
                }

                else {
                    doctor_busy.setVisibility(View.VISIBLE);
                    btn_appointment.setVisibility(View.GONE);
                    btn_appointment.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorDescActivity.this,VideoCallingActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sendEmail(View view) {
        Intent intent2=new Intent(Intent.ACTION_SEND);
        intent2.putExtra(Intent.EXTRA_EMAIL,new String[]{toemail});
        intent2.setType("message/rfc822");
        startActivity(Intent.createChooser(intent2,"Choose an email client"));
    }
}
