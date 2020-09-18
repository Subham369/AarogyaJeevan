package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class TransportPassActivity extends AppCompatActivity {

    private EditText appl_name,appl_phone,appl_purpose_explain,appl_vechile_number,appl_email_addr,appl_driving_license_number,appl_source,appl_destination;
    private EditText appl_person_one,appl_person_two,appl_person_three,appl_person_four,appl_person_five,word_verify;
    private TextView appl_from_date,appl_to_date,txt_person_one,txt_person_two,txt_person_three,txt_person_four,txt_person_five,random_word;
    private DatePickerDialog.OnDateSetListener dateSetListener_from,dateSetListener_to;
    private Spinner spinner_district,spinner_pass_purpose,spinner_person_travelling;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox checkBox_proceed;
    private String text1,text2,text3,default_txt="Click here to select the date";
    private int item_position1,item_position2,item_position3,proceed=0;
    private long random_word_making;
    private String random_word_making_str,txt_option;
    private Random random=new Random();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,databaseReferenceProgress;
    private String transportation_Uid,progress="orange";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_pass);
        appl_from_date=findViewById(R.id.appl_from_date);
        appl_to_date=findViewById(R.id.appl_to_date);
        spinner_district=findViewById(R.id.spinner_district);
        spinner_pass_purpose=findViewById(R.id.spinner_pass_purpose);
        spinner_person_travelling=findViewById(R.id.spinner_person_travelling);
        appl_name=findViewById(R.id.appl_name);
        appl_phone=findViewById(R.id.appl_phone);
        appl_purpose_explain=findViewById(R.id.appl_purpose_explain);
        appl_vechile_number=findViewById(R.id.appl_vechile_number);
        appl_email_addr=findViewById(R.id.appl_email_addr);
        appl_driving_license_number=findViewById(R.id.appl_driving_license_number);
        appl_source=findViewById(R.id.appl_source);
        appl_destination=findViewById(R.id.appl_destination);
        appl_person_one=findViewById(R.id.appl_person_one);
        appl_person_two=findViewById(R.id.appl_person_two);
        appl_person_three=findViewById(R.id.appl_person_three);
        appl_person_four=findViewById(R.id.appl_person_four);
        appl_person_five=findViewById(R.id.appl_person_five);
        radioGroup=findViewById(R.id.radiogroup_transport);
        txt_person_one=findViewById(R.id.txt_person_one);
        txt_person_two=findViewById(R.id.txt_person_two);
        txt_person_three=findViewById(R.id.txt_person_three);
        txt_person_four=findViewById(R.id.txt_person_four);
        txt_person_five=findViewById(R.id.txt_person_five);
        checkBox_proceed=findViewById(R.id.checkBox_proceed);
        word_verify=findViewById(R.id.word_verify);
        random_word=findViewById(R.id.random_word);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReferenceProgress=FirebaseDatabase.getInstance().getReference();

        random_word_making=100000+random.nextInt(999999);
        random_word_making_str=String.valueOf(random_word_making);
        Log.i("DEFAULT_TAG",random_word_making_str);
        random_word.setText(random_word_making_str);


        spinner_value_add();

        appl_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(TransportPassActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener_from,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener_from=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String from_date=dayOfMonth+"/"+month+"/"+year;
                appl_from_date.setText(from_date);
            }
        };

        appl_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(TransportPassActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener_to,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener_to=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String from_date=dayOfMonth+"/"+month+"/"+year;
                appl_to_date.setText(from_date);
            }
        };

        if (checkBox_proceed.isChecked()){
            proceed=1;
        }

    }

    private void spinner_value_add() {
        ArrayList<String> district_name = new ArrayList<>();
        district_name.add("Please Select");
        district_name.add("Odisha");
        district_name.add("West Bengal");
        district_name.add("Tamil Nadu");
        district_name.add("Delhi");
        district_name.add("Punjab");
        district_name.add("Kerela");
        district_name.add("Jammu and Kashmir");
        district_name.add("Ladakh");
        district_name.add("Rajasthan");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,district_name);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_district.setAdapter(arrayAdapter);

        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_district.setSelection(position);
                text1=parent.getItemAtPosition(position).toString();
                item_position1=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_district.requestFocus();

            }
        });

        ArrayList<String> purpose_pass = new ArrayList<>();
        purpose_pass.add("Please Select");
        purpose_pass.add("On Government Duty");
        purpose_pass.add("On Medical Duty");
        purpose_pass.add("Delivery of essential services within state");
        purpose_pass.add("Medical Emergency  within the state");
        purpose_pass.add("Death of a person in the family");
        purpose_pass.add("Inter-State Travel");
        purpose_pass.add("Other(Please specify)");
        purpose_pass.add("Veterinary and Allied Services");
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,purpose_pass);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_pass_purpose.setAdapter(arrayAdapter2);

        spinner_pass_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_pass_purpose.setSelection(position);
                text2=parent.getItemAtPosition(position).toString();
                item_position2=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_district.requestFocus();

            }
        });

        ArrayList<String> people_travelling = new ArrayList<>();
        people_travelling.add("Please Select");
        people_travelling.add("1");
        people_travelling.add("2");
        people_travelling.add("3");
        people_travelling.add("4");
        people_travelling.add("5");
        ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,people_travelling);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_person_travelling.setAdapter(arrayAdapter3);

        spinner_person_travelling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_person_travelling.setSelection(position);
                text3=parent.getItemAtPosition(position).toString();
                item_position3=position;
                Log.i("OUTPUT",String.valueOf(item_position3));

                for (int i=0;i<=item_position3;i++){
                    if (i==1){
                        txt_person_one.setVisibility(View.VISIBLE);
                        appl_person_one.setVisibility( View.VISIBLE);
                    }
                    if (i==2){
                        txt_person_two.setVisibility(View.VISIBLE);
                        appl_person_two.setVisibility( View.VISIBLE);
                    }
                    if (i==3){
                        txt_person_three.setVisibility(View.VISIBLE);
                        appl_person_three.setVisibility( View.VISIBLE);
                    }
                    if (i==4){
                        txt_person_four.setVisibility(View.VISIBLE);
                        appl_person_four.setVisibility( View.VISIBLE);
                    }
                    if (i==5){
                        txt_person_five.setVisibility(View.VISIBLE);
                        appl_person_five.setVisibility( View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_district.requestFocus();

            }
        });


    }

    public void way_choice(View view) {
        int radio_id=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radio_id);
        txt_option=radioButton.getText().toString();
    }

    public void pass_submit(View view) {
        final String uName=appl_name.getText().toString();
        final String uPhoneNumber=appl_phone.getText().toString();
        final String uPurposeDetails=appl_purpose_explain.getText().toString();
        final String uVechileNumber=appl_vechile_number.getText().toString();
        final String uEmail=appl_email_addr.getText().toString();
        final String uDrivingLicenseNumber=appl_driving_license_number.getText().toString();
        final String uSource=appl_source.getText().toString();
        final String uDestination=appl_destination.getText().toString();
        String uword_verify=word_verify.getText().toString();
        String upersonone=appl_person_one.getText().toString();
        String upersontwo=appl_person_two.getText().toString();
        String upersonthree=appl_person_three.getText().toString();
        String upersonfour=appl_person_four.getText().toString();
        String upersonfive=appl_person_five.getText().toString();
        final String ufromdate=appl_from_date.getText().toString();
        final String utodate=appl_to_date.getText().toString();


        if (text1.equals("Please Select")){
            spinner_district.requestFocus();
            Toast.makeText(this, "Please enter district", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(uName)){
            appl_name.requestFocus();
            appl_name.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(uPhoneNumber)){
            appl_phone.requestFocus();
            appl_phone.setError("Please enter your mobile number");
            return;
        }

        if (TextUtils.isEmpty(uPurposeDetails) || uPurposeDetails.length()>300){
            appl_purpose_explain.requestFocus();
            appl_purpose_explain.setError("Please specify purpose in details in max 300 character");
            return;
        }

        if (TextUtils.isEmpty(uVechileNumber)){
            appl_vechile_number.requestFocus();
            appl_vechile_number.setError("Please enter your Vehicle number");
            return;
        }

        if (TextUtils.isEmpty(uEmail)){
            appl_email_addr.requestFocus();
            appl_email_addr.setError("Please enter your email address");
            return;
        }

        if (TextUtils.isEmpty(uDrivingLicenseNumber)){
            appl_driving_license_number.requestFocus();
            appl_driving_license_number.setError("Please enter your driving license number");
            return;
        }

        if (TextUtils.isEmpty(uSource)){
            appl_source.requestFocus();
            appl_source.setError("Please enter your source address");
            return;
        }

        if (TextUtils.isEmpty(uDestination)){
            appl_destination.requestFocus();
            appl_destination.setError("Please enter your destination address");
            return;
        }

        if (TextUtils.isEmpty(uword_verify)|| (!uword_verify.equals(random_word_making_str))){
            word_verify.requestFocus();
            word_verify.setError("Please enter correct word");
            return;
        }

        if (text2.equals("Please Select")){
            Toast.makeText(this, "Please enter purpose pass", Toast.LENGTH_SHORT).show();
            spinner_pass_purpose.requestFocus();
            return;
        }

        if (text3.equals("Please Select")){
            spinner_person_travelling.requestFocus();
            Toast.makeText(this, "Please enter no. of people travelling", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i=1;i<=item_position3;i++){
            if (i==1 && TextUtils.isEmpty(upersonone)){
                appl_person_one.setError("Please enter first person name");
                appl_person_one.requestFocus();
                return;
            }
            if (i==2 && TextUtils.isEmpty(upersontwo)){
                appl_person_two.setError("Please enter second person name");
                appl_person_two.requestFocus();
                return;
            }
            if (i==3 && TextUtils.isEmpty(upersonthree)){
                appl_person_three.setError("Please enter third person name");
                appl_person_three.requestFocus();
                return;
            }
            if (i==4 && TextUtils.isEmpty(upersonfour)){
                appl_person_four.setError("Please enter four person name");
                appl_person_four.requestFocus();
                return;
            }
            if (i==5 && TextUtils.isEmpty(upersonfive)){
                appl_person_five.setError("Please enter fifth person name");
                appl_person_five.requestFocus();
                return;
            }
        }

        if (ufromdate.equals("Click here to select the date")){
            appl_from_date.setError("Please enter the date when you will start ");
            appl_from_date.requestFocus();
            return;
        }

        if (utodate.equals("Click here to select the date")){
            appl_from_date.setError("Please enter the date when you will end ");
            appl_from_date.requestFocus();
            return;
        }

        if ((!txt_option.equals("One Way"))&& (!txt_option.equals("Two Way"))){
            radioGroup.requestFocus();
            Toast.makeText(this, "Please enter whether the e-pass is required for One Way or Two Way", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(checkBox_proceed.isChecked())){
            checkBox_proceed.requestFocus();
            Toast.makeText(this, "Please enter the check box before proceeding ...", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder buider=new AlertDialog.Builder(TransportPassActivity.this);
        buider.setTitle("Confirmation Box");
        buider.setMessage("Do you want to submit the form ?");
        buider.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                register_for_transport(text1,uName,uPhoneNumber,uPurposeDetails,uVechileNumber,uEmail,uDrivingLicenseNumber,uSource,uDestination,ufromdate,utodate);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        buider.show();
    }

    public void pass_close(View view) {
        Intent intent=new Intent(TransportPassActivity.this,AppFeatures.class);
        startActivity(intent);
    }

    public void pass_reset(View view) {
        Toast.makeText(this, "Resetting all inputs", Toast.LENGTH_SHORT).show();
        appl_name.setText("");
        appl_phone.setText("");
        appl_purpose_explain.setText("");
        appl_vechile_number.setText("");
        appl_email_addr.setText("");
        appl_driving_license_number.setText("");
        appl_source.setText("");
        appl_destination.setText("");
        word_verify.setText("");
        random_word_making=100000+random.nextInt(999999);
        random_word_making_str=String.valueOf(random_word_making);
        random_word.setText(random_word_making_str);
        appl_from_date.setText(default_txt);
        appl_from_date.setText(default_txt);
    }

    public void btn_refresh_word(View view) {
        random_word_making=100000+random.nextInt(999999);
        String random_word_making_str=String.valueOf(random_word_making);
        random_word.setText(random_word_making_str);
    }

    private void register_for_transport(String district,String uName, String uPhoneNumber, String uPurposeDetails, String uVechileNumber, String uEmail, String uDrivingLicenseNumber, String uSource, String uDestination, String ufromdate, String utodate) {
        transportation_Uid= firebaseAuth.getUid();
        HashMap<String,String> hashMap_transportation=new HashMap<>();
        hashMap_transportation.put("transportation_id",transportation_Uid);
        hashMap_transportation.put("trans_name",uName);
        hashMap_transportation.put("trans_phone",uPhoneNumber);
        hashMap_transportation.put("trans_purpose_details",uPurposeDetails);
        hashMap_transportation.put("trans_vechile_number",uVechileNumber);
        hashMap_transportation.put("trans_email",uEmail);
        hashMap_transportation.put("trans_dl_number",uDrivingLicenseNumber);
        hashMap_transportation.put("trans_source",uSource);
        hashMap_transportation.put("trans_destination",uDestination);
        hashMap_transportation.put("trans_fromdate",ufromdate);
        hashMap_transportation.put("trans_todate",utodate);
        hashMap_transportation.put("trans_progress",progress);

        if (district.equals("Odisha")){
            databaseReference= FirebaseDatabase.getInstance().getReference("Odisha").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("OdishaProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("West Bengal")){
            databaseReference= FirebaseDatabase.getInstance().getReference("WestBengal").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("WestBengalProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("Tamil Nadu")){
            databaseReference= FirebaseDatabase.getInstance().getReference("TamilNadu").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("TamilNaduProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("Delhi")){
            databaseReference= FirebaseDatabase.getInstance().getReference("Delhi").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("DelhiProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("Punjab")){
            databaseReference= FirebaseDatabase.getInstance().getReference("Punjab").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("PunjabProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        if (district.equals("KerelaActivity")){
            databaseReference= FirebaseDatabase.getInstance().getReference("KerelaActivity").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("KerelaProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("Jammu and Kashmir")){
            databaseReference= FirebaseDatabase.getInstance().getReference("JammuKashmir").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("JammuKashmirProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("LadakhActivity")){
            databaseReference= FirebaseDatabase.getInstance().getReference("LadakhActivity").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("LadakhProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (district.equals("Rajasthan")){
            databaseReference= FirebaseDatabase.getInstance().getReference("Rajasthan").child(transportation_Uid);
            databaseReference.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Data saved successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in saving data in the database please try again...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            databaseReferenceProgress= FirebaseDatabase.getInstance().getReference("RajasthanProgress").child(transportation_Uid);
            databaseReferenceProgress.setValue(hashMap_transportation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TransportPassActivity.this, "Permission in progress...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(TransportPassActivity.this,ResultPermissionActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TransportPassActivity.this, "Error in storing permission progress", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportPassActivity.this, "Error while saving:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
