package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CouncellingSignupActivity extends AppCompatActivity {

    EditText reg_username,reg_email,reg_phone,reg_password,reg_address,reg_hospital,reg_specialization,reg_time,reg_desc;
    Spinner spinner;
    Button btn_register;
    TextView txt_login;
    private int item_position;
    String txt_position;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councelling_signup);

        reg_username=findViewById(R.id.signup_server_name);
        reg_email=findViewById(R.id.signup_server_email);
        reg_phone=findViewById(R.id.signup_server_phone);
        reg_password=findViewById(R.id.signup_server_password);
        reg_address=findViewById(R.id.signup_server_address);
        spinner=findViewById(R.id.signup_server_position);
        reg_hospital=findViewById(R.id.signup_server_hospital);
        reg_specialization=findViewById(R.id.signup_server_specialization);
        reg_time=findViewById(R.id.signup_server_time);
        reg_desc=findViewById(R.id.signup_server_aboutyou);
        btn_register=findViewById(R.id.btn_server_signup);
        auth=FirebaseAuth.getInstance();

        ArrayList<String> server_district = new ArrayList<>();
        server_district.add("Please Select");
        server_district.add("Doctor");
        server_district.add("Patient");
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,server_district);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                txt_position=parent.getItemAtPosition(position).toString();
                item_position=position;

                if (txt_position.equals("Doctor")){
                    reg_hospital.setVisibility(View.VISIBLE);
                    reg_specialization.setVisibility(View.VISIBLE);
                    reg_time.setVisibility(View.VISIBLE);
                    reg_desc.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.requestFocus();

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username=reg_username.getText().toString();
                String str_address=reg_address.getText().toString();
                String str_email=reg_email.getText().toString();
                String str_phone=reg_phone.getText().toString();
                String str_password=reg_password.getText().toString();
                String str_hospital=reg_hospital.getText().toString();
                String str_specialization=reg_specialization.getText().toString();
                String str_time=reg_time.getText().toString();
                String str_desc=reg_desc.getText().toString();

                if (TextUtils.isEmpty(str_username)|| TextUtils.isEmpty(str_address)|| TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)||TextUtils.isEmpty(str_phone))
                {
                    Toast.makeText(CouncellingSignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (str_password.length()<6)
                {
                    Toast.makeText(CouncellingSignupActivity.this, "Password must be greater than 6", Toast.LENGTH_SHORT).show();
                }
//                else if(txt_position.equals("Doctor")){
//                    if (TextUtils.isEmpty(str_hospital)||TextUtils.isEmpty(str_specialization)||TextUtils.isEmpty(str_time)||TextUtils.isEmpty(str_desc)){
//                        Toast.makeText(CouncellingSignupActivity.this, "Please enter your details for hospitals", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                else if (txt_position.equals("Patient")){
//                    str_hospital="Not required";
//                    str_specialization="Not required";
//                    str_time="Not required";
//                    str_desc="Not required";
//                }

                else
                {
                    register(str_username,str_email,str_phone,str_password,str_address,txt_position,str_hospital,str_specialization,str_time,str_desc);
                }
            }
        });
    }

    private void register(final String str_username, final String str_email, final String str_phone, final String str_password, final String str_address, final String txt_position,final String str_hospital,final String str_specialization,final String str_time,final String str_desc) {

        Toast.makeText(this, "Inside register fun", Toast.LENGTH_SHORT).show();
        final DatabaseReference RootRef;
        FirebaseUser fuser=auth.getCurrentUser();
        final String userId=fuser.getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (txt_position.equals("Doctor"))
                {

                    if (!(dataSnapshot.child("Doctor").child(userId).exists()))
                    {
                        HashMap<String ,Object> hashMap=new HashMap<>();
                        hashMap.put("id",userId);
                        hashMap.put("userName",str_username);
                        hashMap.put("email",str_email);
                        hashMap.put("phone",str_phone);
                        hashMap.put("password",str_password);
                        hashMap.put("address",str_address);
                        hashMap.put("position",txt_position);
                        hashMap.put("hospital",str_hospital);
                        hashMap.put("specialization",str_specialization);
                        hashMap.put("bookingtime",str_time);
                        hashMap.put("available","available");
                        hashMap.put("description",str_desc);
                        hashMap.put("code","default");


                        RootRef.child("Doctor").child(userId).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(CouncellingSignupActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();


                                            Intent intent = new Intent(CouncellingSignupActivity.this, CouncellingActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {

                                            Toast.makeText(CouncellingSignupActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(CouncellingSignupActivity.this, "This " + str_username + " already exists.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CouncellingSignupActivity.this, "Please try again using another user phone.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CouncellingSignupActivity.this, CouncellingActivity.class);
                        startActivity(intent);
                    }
                }
                else{

                    if (!(dataSnapshot.child("Patient").child(userId).exists()))
                    {
                        HashMap<String ,Object> hashMap=new HashMap<>();
                        hashMap.put("id",userId);
                        hashMap.put("userName",str_username);
                        hashMap.put("email",str_email);
                        hashMap.put("phone",str_phone);
                        hashMap.put("password",str_password);
                        hashMap.put("address",str_address);
                        hashMap.put("position",txt_position);


                        RootRef.child("Patient").child(userId).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(CouncellingSignupActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();


                                            Intent intent = new Intent(CouncellingSignupActivity.this, CouncellingActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {

                                            Toast.makeText(CouncellingSignupActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(CouncellingSignupActivity.this, "This " + str_username + " already exists.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CouncellingSignupActivity.this, "Please try again using another phone.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CouncellingSignupActivity.this, CouncellingActivity.class);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
