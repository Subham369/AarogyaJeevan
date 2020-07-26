package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HostRequestActivity extends AppCompatActivity {

    Button personal_service_pass,transport_service_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_request);
        personal_service_pass=findViewById(R.id.btn_personal_pass);
        transport_service_pass=findViewById(R.id.btn_transport_pass);

        personal_service_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(HostRequestActivity.this,PersonalPassActivity.class);
                startActivity(intent1);
            }
        });

        transport_service_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(HostRequestActivity.this,TransportPassActivity.class);
                startActivity(intent1);
            }
        });
    }
}

