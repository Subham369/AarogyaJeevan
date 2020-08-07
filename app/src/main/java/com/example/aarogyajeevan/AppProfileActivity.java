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
import android.view.MenuItem;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences,app_preferences;
    SharedPreferences.Editor editor;
    Methods methods;
    int appColor;
    int appTheme;
    int themeColor;
    Button button;
    CircleImageView image_profile;
    ConstantColor constant;
    DatabaseReference reference;
    DatabaseReference rootref;
    FirebaseUser firebaseUser;
    TextView userinfo_phone_number,userinfo_name,userinfo_email,addHotspot,addHelpline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        appColor = app_preferences.getInt("color", 0);
//        appTheme = app_preferences.getInt("theme", 0);
//        themeColor = appColor;
//        constant.color = appColor;
//
//        if (themeColor == 0){
//            setTheme(ConstantColor.theme);
//        }else if (appTheme == 0){
//            setTheme(ConstantColor.theme);
//        }else{
//            setTheme(appTheme);
//        }

        setContentView(R.layout.activity_app_profile);

//        final Toolbar toolbar =findViewById(R.id.toolbar_setting);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Settings");
//        toolbar.setBackgroundColor(ConstantColor.color);

        methods = new Methods();
        button =findViewById(R.id.button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        colorize();

        userinfo_phone_number=findViewById(R.id.userinfo_phone_number);
        userinfo_name=findViewById(R.id.userinfo_name);
        userinfo_email=findViewById(R.id.userinfo_email);
        image_profile=findViewById(R.id.image_profile);
        addHotspot=findViewById(R.id.addHotspot);
        addHelpline=findViewById(R.id.addHelpline);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId=firebaseUser.getUid();
        if (userId.equals("bmqeBI1k9AeagChB1ZBzm13rCx02")){
            addHelpline.setVisibility(View.VISIBLE);
            addHotspot.setVisibility(View.VISIBLE);
        }
        else{
            addHelpline.setVisibility(View.GONE);
            addHotspot.setVisibility(View.GONE);
        }

        addHotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LocationAdderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        addHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddActivity.class);
                startActivity(intent);
            }
        });


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ColorChooserDialog dialog = new ColorChooserDialog(AppProfileActivity.this);
               dialog.setTitle("Select");
               dialog.setColorListener(new ColorListener() {
                   @Override
                   public void OnColorClick(View v, int color) {
                       colorize();
                       ConstantColor.color = color;

                       methods.setColorTheme();
                       editor.putInt("color", color);
                       editor.putInt("theme",ConstantColor.theme);
                       editor.commit();

                       Intent intent = new Intent(AppProfileActivity.this, AppFeatures.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }
               });

               dialog.show();
           }
       });


        reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo user=dataSnapshot.getValue(UserInfo.class);
                userinfo_phone_number.setText(user.getPhone_number());
                userinfo_name.setText(user.getFullname());
                userinfo_email.setText(user.getEmail());
                if (user.getImageURLW().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageURLW()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL)
                        .start(AppProfileActivity.this);
            }
        });
    }

    public void changePassword(View view) {
        Intent intent=new Intent(AppProfileActivity.this,PasswordChangeActivity.class);
        startActivity(intent);
    }

    public void shareApp(View view) {
        Intent intentShare=new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        String shareBody="Download this application now:";
        String shareSub="Aarogya Jeevan App";
        intentShare.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        intentShare.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(intentShare,"Share app using:"));
    }

    public void helpLineNumber(View view) {
        Intent intent=new Intent(AppProfileActivity.this,HelpLine.class);
        startActivity(intent);
    }

    public void btnTodoList(View view) {
        Intent intent=new Intent(AppProfileActivity.this,TodoListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize(){
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(ConstantColor.color);

        button.setBackground(d);
    }

    public void btnAboutApp(View view) {
        Intent intent=new Intent(AppProfileActivity.this,AboutAppActivity.class);
        startActivity(intent);
    }

}
