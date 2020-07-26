package com.example.aarogyajeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aarogyajeevan.Adapter.MyCustomAdapter;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountries extends AppCompatActivity {

    EditText edtsearch;
    ListView listview;
    SimpleArcLoader loader;

    public static List<CountryModel> countryModelsList=new ArrayList<>();
    CountryModel countryModel;
    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);
        edtsearch=findViewById(R.id.edtsearch);
        listview=findViewById(R.id.listview);
        loader=findViewById(R.id.loader);
        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchData();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),DetailActivity.class).putExtra("position",position));
            }
        });
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home==item.getItemId())
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {

        String url="https://corona.lmao.ninja/v2/countries/";
        loader.start();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String countryname=jsonObject.getString("country");
                        String cases=jsonObject.getString("cases");
                        String todayCases=jsonObject.getString("todayCases");
                        String deaths=jsonObject.getString("deaths");
                        String todayDeaths=jsonObject.getString("todayDeaths");
                        String recovered=jsonObject.getString("recovered");
                        String active=jsonObject.getString("active");
                        String critical=jsonObject.getString("critical");
                        JSONObject object=jsonObject.getJSONObject("countryInfo");
                        String flagUrl=object.getString("flag");

                        countryModel=new CountryModel(flagUrl,countryname,cases,todayCases,deaths,todayDeaths,recovered,active,critical);
                        countryModelsList.add(countryModel);
                    }

                    myCustomAdapter=new MyCustomAdapter(AffectedCountries.this,countryModelsList);
                    listview.setAdapter(myCustomAdapter);
                    loader.stop();
                    loader.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.stop();
                    loader.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loader.stop();
                loader.setVisibility(View.GONE);
                Toast.makeText(AffectedCountries.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

