package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AppProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences,app_preferences;
    SharedPreferences.Editor editor;
    Methods methods;
    int appColor;
    int appTheme;
    int themeColor;
    Button button;
    Constant constant;
    DatabaseReference reference;
    DatabaseReference rootref;
    FirebaseUser firebaseUser;
    TextView userinfo_phone_number,userinfo_name,userinfo_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);
        appColor=app_preferences.getInt("color",0);
        appTheme=app_preferences.getInt("theme",0);
        themeColor=appColor;
        constant.color=appColor;

        if (themeColor==0){
            setTheme(Constant.theme);
        }
        else if (appTheme==0){
            setTheme(Constant.theme);
        }
        else
        {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_app_profile);

        userinfo_phone_number=findViewById(R.id.userinfo_phone_number);
        userinfo_name=findViewById(R.id.userinfo_name);
        userinfo_email=findViewById(R.id.userinfo_email);

        final Toolbar toolbar=findViewById(R.id.toolbar_setting);
        toolbar.setTitle("Settings");
        toolbar.setBackgroundColor(Constant.color);
        methods=new Methods();
        button=findViewById(R.id.button);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        colorize();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo user=dataSnapshot.getValue(UserInfo.class);
                userinfo_phone_number.setText(user.getPhone_number());
                userinfo_name.setText(user.getFullname());
                userinfo_email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize(){
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);

        button.setBackground(d);
    }

    public void changePassword(View view) {
        Intent intent=new Intent(AppProfileActivity.this,PasswordChangeActivity.class);
        startActivity(intent);
    }

//    @Nullable
//    public Dialog onCreateDialog(Bundle args) {
//        AlertDialog.Builder dialog=new AlertDialog.Builder(AppProfileActivity.this);
//        dialog.setTitle("Select Color").setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        return dialog.create();
//    }create
}
