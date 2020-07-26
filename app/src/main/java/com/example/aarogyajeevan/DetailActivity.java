package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private int positionCountry;
    TextView tvCountry,tvCases,tvTodayCases,tvDeaths,tvTodayDeaths,tvRecovered,tvActive,tvCriticalCases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        positionCountry=intent.getIntExtra("position",0);
        getSupportActionBar().setTitle("Details Report of "+ AffectedCountries.countryModelsList.get(positionCountry).getCountry());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvCountry=findViewById(R.id.tvCountry);
        tvCases=findViewById(R.id.tvCases);
        tvTodayCases=findViewById(R.id.tvTodayCases);
        tvDeaths=findViewById(R.id.tvDeaths);
        tvTodayDeaths=findViewById(R.id.tvTodayDeaths);
        tvRecovered=findViewById(R.id.tvRecovered);
        tvActive=findViewById(R.id.tvActive);
        tvCriticalCases=findViewById(R.id.tvCriticalCases);


        tvCountry.setText(AffectedCountries.countryModelsList.get(positionCountry).getCountry());
        tvCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getCases());
        tvTodayCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodaycases());
        tvDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getDeaths());
        tvTodayDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodaydeaths());
        tvRecovered.setText(AffectedCountries.countryModelsList.get(positionCountry).getRecovered());
        tvActive.setText(AffectedCountries.countryModelsList.get(positionCountry).getActive());
        tvCriticalCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getCritical());




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home==item.getItemId())
            finish();
        return super.onOptionsItemSelected(item);
    }
}

