package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CommunityPortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_portal);
    }

    public void btn1Clk(View view) {
        Intent intent=new Intent(CommunityPortalActivity.this,CommDashBoardActivity.class);
        startActivity(intent);
    }

    public void btn2Clk(View view) {
        Intent intent=new Intent(CommunityPortalActivity.this,ChattingActivity.class);
        startActivity(intent);
    }
}
