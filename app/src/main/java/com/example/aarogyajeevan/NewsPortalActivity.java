package com.example.aarogyajeevan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.example.aarogyajeevan.Adapter.Adapter_main;
import com.example.aarogyajeevan.Model.Articles;
import com.example.aarogyajeevan.Model.HeadLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


public class NewsPortalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    final String API_KEY="68d20c5774f64ecdb1c9a5ad78328a00";
    String newsCatagory="health";//change here
    Adapter_main adapter;
    List<Articles> articles=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_portal);
        swipeRefreshLayout=findViewById(R.id.swipeFresh);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country=getCountry();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retriveGson("",country,newsCatagory,API_KEY);//changes here
            }
        });


        retriveGson("",country,newsCatagory,API_KEY);//changes here

        getSupportActionBar().setTitle("News Portal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void retriveGson(String query,String country,String catagory,String apiKey)
    {
        swipeRefreshLayout.setRefreshing(true);
        Call<HeadLine> call;
        call=APIClient.getInstance().getApi().getheadline(country,catagory,apiKey);
        call.enqueue(new Callback<HeadLine>() {
            @Override
            public void onResponse(Call<HeadLine> call, Response<HeadLine> response) {
                if (response.isSuccessful() && response.body().getArticles()!=null)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles= response.body().getArticles();
                    adapter=new Adapter_main(NewsPortalActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<HeadLine> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsPortalActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry()
    {
        Locale locale=Locale.getDefault();

        String country=locale.getCountry();
        return country.toLowerCase();
    }

}

