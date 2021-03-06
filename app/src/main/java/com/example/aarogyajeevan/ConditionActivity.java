package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aarogyajeevan.Model.Person_Detail;
import com.example.aarogyajeevan.Model.Symptoms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConditionActivity extends AppCompatActivity {

    EditText person_name,person_age,person_gender,person_location,person_recent_visit,person_symptoms;
    FirebaseAuth firebaseAuth;
    Spinner spinner;
    Button btnFamilyDetails;
    DatabaseReference databaseReference;
    String text;
    int item_position;
    int i=0,f=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        getSupportActionBar().setTitle("Wellness Checker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        person_name=findViewById(R.id.person_name);
        person_age=findViewById(R.id.person_age);
        person_gender=findViewById(R.id.person_gender);
        person_location=findViewById(R.id.person_location);
        person_recent_visit=findViewById(R.id.person_recent_visit);
        person_symptoms=findViewById(R.id.person_symptoms);
        spinner=findViewById(R.id.spinner_family_details);
        btnFamilyDetails=findViewById(R.id.btnFamilyDetails);
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("symptoms");

        ArrayList<String> family_Details_Permission = new ArrayList<>();
        family_Details_Permission.add("Please select");
        family_Details_Permission.add("Yes");
        family_Details_Permission.add("No");
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, family_Details_Permission);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                text=parent.getItemAtPosition(position).toString();
                item_position=position;
                if (text.compareTo("Yes")==0){
                    f++;
                }
                else{
                    f=0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.requestFocus();

            }
        });
    }

    public void symptoms_submit(View view) {
        String per_name=person_name.getText().toString();
        String per_age=person_age.getText().toString();
        String per_gender=person_gender.getText().toString();
        String per_location=person_location.getText().toString();
        String per_recent_visit=person_recent_visit.getText().toString();
        String per_symptoms=person_symptoms.getText().toString();

        if (TextUtils.isEmpty(per_name)){
            person_name.requestFocus();
            person_name.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(per_age)){
            person_age.requestFocus();
            person_age.setError("Please enter your age");
            return;
        }

        if (TextUtils.isEmpty(per_gender)){
            person_gender.requestFocus();
            person_gender.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(per_location)){
            person_location.requestFocus();
            person_location.setError("Please enter your present/current location");
            return;
        }

        if (TextUtils.isEmpty(per_recent_visit)){
            person_recent_visit.requestFocus();
            person_recent_visit.setError("Please enter the locations you have visited before falling ill");
            return;
        }

        if (TextUtils.isEmpty(per_symptoms)){
            person_symptoms.requestFocus();
            person_symptoms.setError("Please enter your symptoms");
            return;
        }

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        int value=i++;
        String pos=String.valueOf(value);
        Symptoms symptoms=new Symptoms(per_symptoms);
        String userId=firebaseUser.getUid();
        FirebaseDatabase.getInstance().getReference("symptoms").child(userId).child(pos).setValue(symptoms).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ConditionActivity.this, "Symptoms saved", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ConditionActivity.this, "Symptoms not saved", Toast.LENGTH_SHORT).show();
            }
        });

        Person_Detail detail=new Person_Detail(per_name,per_age,per_gender,per_location,per_recent_visit);
        FirebaseDatabase.getInstance().getReference("person_detail").child(userId).setValue(detail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(ConditionActivity.this, "Your details have saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ConditionActivity.this, "Your details have not saved yet", Toast.LENGTH_SHORT).show();
            }
        });
        f++;
        if (f>=2){
            btnFamilyDetails.setVisibility(View.VISIBLE);
        }
        else{
            btnFamilyDetails.setVisibility(View.INVISIBLE);
        }
    }

    public void familyDetailsSubmit(View view) {
        Intent intent=new Intent(ConditionActivity.this,FamilyDetailsActivity.class);
        startActivity(intent);
    }

    public void viewReport(View view) {
        Intent intent=new Intent(ConditionActivity.this,UserReportActivity.class);
        startActivity(intent);
    }
}

