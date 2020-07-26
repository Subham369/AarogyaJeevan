package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSignup extends AppCompatActivity {
    EditText edtPhone, edtName, edtDOB, edtEmail, edtPassword, edtAddress,edtFullName;
    Spinner spinner,spinner2;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int item_position=0, item_position2=0,f=0,f2=0,f3=0,f4=0,f5=0;
    private String text="",text2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtFullName = findViewById(R.id.edtFullName);
        edtDOB = findViewById(R.id.edtDOB);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtAddress = findViewById(R.id.edtAddress);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayList<String> coname = new ArrayList<>();
        coname.add("Please select");
        coname.add("Hired Worker");
        coname.add("Causal Worker");
        coname.add("Regular Salaried Worker");
        coname.add("Self Employed");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, coname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                text=parent.getItemAtPosition(position).toString();
                item_position=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.requestFocus();

            }
        });

        ArrayList<String> status = new ArrayList<>();
        status.add("Please select");
        status.add("Volenteer");
        status.add("Health Worker");
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, status);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(arrayAdapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner2.setSelection(position);
                text2=parent.getItemAtPosition(position).toString();
                item_position2=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner2.requestFocus();

            }
        });
    }

    public void signUp(View view) {
        String medtName = edtName.getText().toString();
        String medtFullName = edtFullName.getText().toString();
        String medtDOB = edtDOB.getText().toString();
        String medtPhone = edtPhone.getText().toString();
        String medtEmail = edtEmail.getText().toString();
        String medtPassword = edtPassword.getText().toString();
        String medtAddress = edtAddress.getText().toString();
        String mspinner=text;
        String mspinner2=text2;

        for (int i=0;i<medtDOB.length();i++){
            if(medtAddress.charAt(i)=='-'){
                f=1;
                break;
            }
        }

        for (int i=0;i<medtPassword.length();i++){
            if(Character.isUpperCase(medtPassword.charAt(i))){
                f2++;
            }
            else if(Character.isLowerCase(medtPassword.charAt(i))){
                f3++;
            }
            else if(Character.isDigit(medtPassword.charAt(i))){
                f4++;
            }
            else if(Character.isWhitespace(medtPassword.charAt(i))){
                edtPassword.requestFocus();
                edtPassword.setError("Enter valid password without white spaces");
                edtPassword.setText("");
            }

            else{
                f5++;
            }
        }

        if (medtName.isEmpty() || (medtName.length()>8)) {
            edtName.requestFocus();
            edtName.setError("Enter your user name within 8 characters");
            return;
        }

        if (medtFullName.isEmpty()) {
            edtFullName.requestFocus();
            edtFullName.setError("Enter your full name");
            return;
        }

        if (medtDOB.isEmpty() || (medtDOB.length()>=11) || f==1) {
            edtDOB.requestFocus();
            edtDOB.setError("Enter your date of birth in the correct format(i.e dd/mm/yyyy)");
            return;
        }
        if (medtPhone.isEmpty() || (medtPhone.length()>10) || (medtPhone.length()<9)) {
            edtPhone.requestFocus();
            edtPhone.setError("Enter your a valid mobile number");
            return;
        }
        if (medtPassword.isEmpty() || medtPassword.length() < 6 ) {
            edtPassword.requestFocus();
            edtPassword.setError("Enter valid password with length greater than 6");
            return;
        }

        if ((f2<1&&f3<1&&f4<1&&f5<1)) {
            edtPassword.requestFocus();
            edtPassword.setError("Enter a secured password with having at least one uppercase,one lowercase,digit and one special character");
            edtPassword.setText("");
            return;
        }


        if (medtEmail.isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError("Enter valid email address");
            return;
        }
        if (medtAddress.isEmpty()) {
            edtAddress.requestFocus();
            edtAddress.setError("Enter your current address");
            return;
        }

        if (mspinner.isEmpty()) {
            spinner.requestFocus();
            return;
        }

        if (mspinner2.isEmpty()) {
            spinner2.requestFocus();
            return;
        }
        register(medtName,medtFullName, medtDOB, medtPhone, medtEmail, medtPassword, medtAddress,mspinner,mspinner2);
    }

    private void register(final String medtName,final String medtFullName, final String medtDOB, final String medtPhone, final String medtEmail,final String medtPassword, final String medtAddress, final String workInfo, final String positionInfo) {
        firebaseAuth.createUserWithEmailAndPassword(medtEmail, medtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", medtName);
                    hashMap.put("fullname", medtFullName);
                    hashMap.put("dob", medtDOB);
                    hashMap.put("phone_number", medtPhone);
                    hashMap.put("email", medtEmail);
                    hashMap.put("workerInfo", workInfo);
                    hashMap.put("position", positionInfo);
                    hashMap.put("address", medtAddress);
                    hashMap.put("occupation", workInfo);
                    hashMap.put("imageURLI", "https://firebasestorage.googleapis.com/v0/b/instagram-9dd7d.appspot.com/o/profile_image.png?alt=media&token=1f6cd9b7-caad-4ecf-8c8a-bb9c2cf70109");
                    hashMap.put("bio","");
                    hashMap.put("imageURLW", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("search", medtName.toLowerCase());

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(UserSignup.this, UserDetails.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(UserSignup.this, "Details saved successfully", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(UserSignup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

}
