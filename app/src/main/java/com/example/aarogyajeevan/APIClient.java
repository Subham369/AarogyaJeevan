package com.example.aarogyajeevan;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL="https://newsapi.org/v2/";
    private static APIClient apiClient;
    private static Retrofit retrofit;

    private APIClient()
    {
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized APIClient getInstance()
    {
        if (apiClient ==null)
        {
            apiClient=new APIClient();
        }
        return apiClient;
    }

    public ApiInterface getApi(){

        return retrofit.create(ApiInterface.class);
    }
}

