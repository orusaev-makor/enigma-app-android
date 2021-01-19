package com.example.enigmaapp.web;

import com.example.enigmaapp.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient single_instance = null;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private RetrofitClient() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
//                .baseUrl(Constant.BASE_URL)
                .baseUrl(Constant.X_API_REST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public static RetrofitClient getInstance() {
        if (single_instance == null) {
            single_instance = new RetrofitClient();
        }
        return single_instance;
    }

    public RetrofitInterface getRetrofitInterface() {
        return retrofitInterface;
    }
}
