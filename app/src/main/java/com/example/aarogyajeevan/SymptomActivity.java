package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SymptomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        getSupportActionBar().setTitle("Symptoms of COVID-19");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}

