package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private TextView set_user_name,set_phone,set_position;
    private CircleImageView circleImageView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        set_user_name=findViewById(R.id.set_user_name);
        set_phone=findViewById(R.id.set_phone_number);
        set_position=findViewById(R.id.set_position);
        circleImageView=findViewById(R.id.set_profile_image);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        retrive_user_info();
    }

    private void retrive_user_info() {
        userId=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("UserInfo").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists())&& (snapshot.hasChild("username"))&&(snapshot.hasChild("imageURLW"))){
                    String ret_username=snapshot.child("username").getValue().toString();
                    String ret_phone=snapshot.child("phone_number").getValue().toString();
                    String ret_position=snapshot.child("position").getValue().toString();
                    String ret_image=snapshot.child("imageURLW").getValue().toString();

                    set_user_name.setText(ret_username);
                    set_phone.setText(ret_phone);
                    set_position.setText(ret_position);
                }
                else if ((snapshot.exists())&& (snapshot.hasChild("username")))
                {
                    String ret_username=snapshot.child("username").getValue().toString();
                    String ret_phone=snapshot.child("phone_number").getValue().toString();
                    String ret_position=snapshot.child("position").getValue().toString();
                    String ret_image=snapshot.child("imageURLW").getValue().toString();

                    set_user_name.setText(ret_username);
                    set_phone.setText(ret_phone);
                    set_position.setText(ret_position);

                }
                else {
                    Toast.makeText(SettingsActivity.this, "Please set your profile picture", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void profile_update(View view) {
//        String new_username=set_user_name.getText().toString();
//        String new_status=set_phone.getText().toString();
//
//        if (TextUtils.isEmpty(new_username)){
//            set_user_name.setError("Please enter your new username");
//            set_user_name.requestFocus();
//        }
//
//        if (TextUtils.isEmpty(new_status)){
//            set_status.setText("Can't talk, text only");
//        }
////        update_profile(new_username,new_status);
//    }

//    private void update_profile(final String new_username, final String set_status) {
//        userId=firebaseAuth.getCurrentUser().getUid();
//        databaseReference= FirebaseDatabase.getInstance().getReference("UserChat").child(userId);
//        HashMap<String,String> hashMap=new HashMap<>();
//        hashMap.put("username",new_username);
//        hashMap.put("profile_status",set_status);
//        hashMap.put("search",new_username.toLowerCase());
//        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(SettingsActivity.this, "Profile updated succesfully...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SettingsActivity.this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}

