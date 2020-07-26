package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.aarogyajeevan.Fragment.HomeFragment;
import com.example.aarogyajeevan.Fragment.ProfileFragment;
import com.example.aarogyajeevan.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class CommDashBoardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_dash_board);
        bottomNavigationView=findViewById(R.id.bottom_navigation);

        getSupportActionBar().setTitle("Community Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent=getIntent().getExtras();
        if (intent!=null)
        {
            String publisher=intent.getString("publisherid");
            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileId",publisher);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        }

        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:

                            selectedFragment=new HomeFragment();

                            break;

                        case R.id.nav_search:

                            selectedFragment=new SearchFragment();
                            break;

                        case R.id.nav_add:

                            selectedFragment=null;
                            startActivity(new Intent(CommDashBoardActivity.this, PostActivity.class));
                            break;

//                        case R.id.nav_favourite:
//
//                            selectedFragment=new NotificationFragment();
//                            break;

                        case R.id.nav_profile:

                            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putStringSet("profileId", Collections.singleton(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                            editor.apply();
                            selectedFragment=new ProfileFragment();
                            break;
                    }

                    if (selectedFragment!=null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return true;
                }
            };
}

