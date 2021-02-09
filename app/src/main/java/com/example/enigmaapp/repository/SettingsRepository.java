package com.example.enigmaapp.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.settings.ExposureLimitItemResult;
import com.example.enigmaapp.web.settings.MaxQtyPerTradeItemResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRepository {
    private MutableLiveData<List<ExposureLimitItemResult>> allLimitExposures= new MutableLiveData<>();
    private MutableLiveData<List<MaxQtyPerTradeItemResult>> allMaxPerTrades = new MutableLiveData<>();
    private Application application;

    public SettingsRepository(Application application) {
        this.application = application;
    }

    public void fetchLimitExposures(String token) {
        Call<ArrayList<ExposureLimitItemResult>>  call = RetrofitClient.getInstance().getRetrofitInterface().executeGetExposureLimit(token);
        call.enqueue(new Callback<ArrayList<ExposureLimitItemResult>> () {
            @Override
            public void onResponse(Call<ArrayList<ExposureLimitItemResult>>  call, Response<ArrayList<ExposureLimitItemResult>>  response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchBalances - Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allLimitExposures.setValue(response.body());
                System.out.println("___ fetchLimitExposures : allLimitExposures size is: " + allLimitExposures.getValue().size());
            }

            @Override
            public void onFailure(Call<ArrayList<ExposureLimitItemResult>>  call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
            }
        });
    }

    public void fetchMaxPerTrades(String token) {
        Call<ArrayList<MaxQtyPerTradeItemResult>> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetMaxQtyPerTrade(token);
        call.enqueue(new Callback<ArrayList<MaxQtyPerTradeItemResult>>() {
            @Override
            public void onResponse(Call<ArrayList<MaxQtyPerTradeItemResult>> call, Response<ArrayList<MaxQtyPerTradeItemResult>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchBalances - Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allMaxPerTrades.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<MaxQtyPerTradeItemResult>> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
            }
        });
    }

    public MutableLiveData<List<ExposureLimitItemResult>> getAllLimitExposures() {
        return allLimitExposures;
    }

    public MutableLiveData<List<MaxQtyPerTradeItemResult>> getAllMaxPerTrades() {
        return allMaxPerTrades;
    }
}
