package com.example.aarogyajeevan;

import com.example.aarogyajeevan.Model.HeadLine;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<HeadLine> getheadline(
            @Query("country") String country,
            @Query("category")String catagory,
            @Query("apiKey") String apikey
    );

    @GET("everything")
    Call<HeadLine> getsearch(
            @Query("q") String query,
            @Query("apiKey") String apikey
    );
}
