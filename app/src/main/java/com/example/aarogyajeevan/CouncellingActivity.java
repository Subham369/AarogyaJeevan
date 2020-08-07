package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.Doctor;
import com.example.aarogyajeevan.Model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CouncellingActivity extends AppCompatActivity {

    EditText log_email,log_password;
    Button btn_login;
    TextView txt_signUp;

    FirebaseAuth mAuth;
    ProgressDialog pd;
    private String parentDbDoctor="Doctor";
    private String parentDbPatient="Patient";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councelling);
        log_email=findViewById(R.id.login_phone);
        log_password=findViewById(R.id.login_password);
        btn_login=findViewById(R.id.btn_login);
        txt_signUp=findViewById(R.id.txt_signUp);

        mAuth=FirebaseAuth.getInstance();

        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CouncellingActivity.this,CouncellingSignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(CouncellingActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_email=log_email.getText().toString();
                String str_password=log_password.getText().toString();
                if (TextUtils.isEmpty(str_email)|| TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(CouncellingActivity.this, "All field are need to be filled", Toast.LENGTH_SHORT).show();
                }
                else if (str_password.length()<6)
                {
                    Toast.makeText(CouncellingActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    loginUser(str_email,str_password);
                }
            }
        });
    }

    private void loginUser(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        FirebaseUser fuser=mAuth.getCurrentUser();
        final String userId=fuser.getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbDoctor).child(userId).exists())
                {
                    Doctor usersData = dataSnapshot.child(parentDbDoctor).child(userId).getValue(Doctor.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbDoctor.equals("Doctor"))
                            {
                                Toast.makeText(CouncellingActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();

                                Intent intent = new Intent(CouncellingActivity.this, PatientListActivity.class);
//                                intent.putExtra("phone_number",phone);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(CouncellingActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else if (dataSnapshot.child(parentDbPatient).child(userId).exists())
                {
                    Patient usersData = dataSnapshot.child(parentDbPatient).child(userId).getValue(Patient.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbPatient.equals("Patient"))
                            {
                                Toast.makeText(CouncellingActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();

                                Intent intent = new Intent(CouncellingActivity.this, DoctorListActivity.class);
//                                intent.putExtra("phone_number",phone);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(CouncellingActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                {
                    Toast.makeText(CouncellingActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

