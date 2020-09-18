package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppFeatures extends AppCompatActivity {

    Button warnBtn;
    TextView warnTxt;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    PieChart pieChart;
    String t1,t2,t3,t4;
    CircleImageView profile_image;
    TextView username;
    DatabaseReference reference;
    DatabaseReference rootref;

//    SharedPreferences app_preferences;
//    SharedPreferences.Editor editor;
    Methods methods;
    int appColor;
    int appTheme;
    int themeColor;
    ConstantColor constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocal();
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

        setContentView(R.layout.activity_app_features);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        warnBtn=findViewById(R.id.warnBtn);
        warnTxt=findViewById(R.id.warnTxt);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        pieChart=findViewById(R.id.pieChart);
        fetchData();
//        firebaseUser=firebaseAuth.getCurrentUser();
        if (!firebaseUser.isEmailVerified()){
            warnTxt.setVisibility(View.VISIBLE);
            warnBtn.setVisibility(View.VISIBLE);
            warnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser fUser=firebaseAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AppFeatures.this, "Email has been verified", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AppFeatures.this, "onfailure:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("UserInfo").child(firebaseUser.getUid());
        rootref=FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo user=dataSnapshot.getValue(UserInfo.class);
                String userValue="Hello,"+(user.getUsername());
                username.setText(userValue);
                if (user.getImageURLW().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageURLW()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void itemWorkerLocation(View view) {
        Intent intent=new Intent(AppFeatures.this,LaborLocationActivity.class);
        startActivity(intent);
    }

//    public void itemProfile(View view) {
//        Intent intent=new Intent(getApplicationContext(),UserProfile.class);
//        startActivity(intent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),UserDetails.class);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.faq)
        {

            Intent intent=new Intent(getApplicationContext(),FaqActivity.class);
            startActivity(intent);
        }

        if (android.R.id.home==item.getItemId())
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void clkFund(View view) {
        Intent intent=new Intent(getApplicationContext(),FundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkLab(View view) {
        Intent intent=new Intent(AppFeatures.this,HospitalsLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    public void clkNewsUpdate(View view) {
        Intent intent=new Intent(AppFeatures.this,NewsPortalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkSymptoms(View view) {
        Intent intent=new Intent(AppFeatures.this,SymptomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkFAQ(View view) {
        Intent intent=new Intent(AppFeatures.this,FaqActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        startActivity(intent);
    }

    public void clkCondition(View view) {
        Intent intent=new Intent(AppFeatures.this,ConditionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkGeofence(View view) {
        Intent intent=new Intent(AppFeatures.this,GeofencingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkCommunity(View view) {
        Intent intent=new Intent(AppFeatures.this,CommunityPortalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkPass(View view) {
        String userId=firebaseUser.getUid();
        Toast.makeText(this, "User ID : "+userId, Toast.LENGTH_LONG).show();
        if (userId.compareTo("KGhVwUwoy5YtVJzMSwyt2AHQc9q1")==0){
            Toast.makeText(this, "Permission granter", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(AppFeatures.this,EastServerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else{
            Intent intent=new Intent(AppFeatures.this,TransportPassActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    public void clkCouncelling(View view) {
        Intent intent=new Intent(AppFeatures.this,CouncellingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void clkEcommerce(View view) {
        Intent intent=new Intent(AppFeatures.this,ECommerceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void moreDetails(View view) {
        Intent intent=new Intent(AppFeatures.this,TrackerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void fetchData() {
        String url="https://corona.lmao.ninja/v2/all/";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    t1=jsonObject.getString("cases");
                    t2=jsonObject.getString("recovered");
                    t3=jsonObject.getString("active");
                    t4=jsonObject.getString("deaths");

                    pieChart.addPieSlice(new PieModel("Cases",Long.parseLong(t1.toString()), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recovered",Long.parseLong(t2.toString()), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Deaths",Long.parseLong(t4.toString()), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active",Long.parseLong(t3.toString()), Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();
//                    simpleArcLoader.stop();
//                    simpleArcLoader.setVisibility(View.GONE);
//                    scrollView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    simpleArcLoader.stop();
//                    simpleArcLoader.setVisibility(View.GONE);
//                    scrollView.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                simpleArcLoader.stop();
//                simpleArcLoader.setVisibility(View.GONE);
//                scrollView.setVisibility(View.VISIBLE);
//                Toast.makeText(TrackerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void clkProfile(View view) {
        Intent intent=new Intent(AppFeatures.this,AppProfileActivity.class);
        startActivity(intent);
    }

    public void clkHospitalBook(View view) {
        Intent intent=new Intent(AppFeatures.this,HospitalBookingActivity.class);
        startActivity(intent);
    }

    private void setLocate(String language) {
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My Language",language);
        editor.apply();
    }

    public void loadLocal(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My Language","");
        setLocate(language);
    }
}
