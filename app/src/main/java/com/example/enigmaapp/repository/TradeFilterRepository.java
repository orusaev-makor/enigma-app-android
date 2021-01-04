package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeFilterRepository {

    private MutableLiveData<TradeDatasetResult> tradeDataset = new MutableLiveData<>();
    private Application application;

    public TradeFilterRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<TradeDatasetResult> getTradeDataset() {
        return tradeDataset;
    }

    public void fetchTradeDataset(String token) {
        Call<TradeDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTradeDataset(token);
        call.enqueue(new Callback<TradeDatasetResult>() {
            @Override
            public void onResponse(Call<TradeDatasetResult> call, Response<TradeDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                tradeDataset.setValue(response.body());
                System.out.println(" _______DATA SET RECEIVED: " + tradeDataset);
            }

            @Override
            public void onFailure(Call<TradeDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
