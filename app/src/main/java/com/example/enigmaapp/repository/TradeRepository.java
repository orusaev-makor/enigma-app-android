package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.trade.TradeResult;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeRepository {

    private MutableLiveData<List<TradeItemResult>> allTrades = new MutableLiveData<>();
    private Application application;

    public TradeRepository(Application application) {
        this.application = application;
    }

    public LiveData<List<TradeItemResult>> getTrades() {
        return allTrades;
    }

    public void fetchTrades(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("items_per_page", "5");
        params.put("current_page", "1");
        params.put("sort", "trade_id desc");
        params.put("counterparty_id", "249");
        params.put("product_id", "2");
        params.put("already_batched", "0");

        Call<TradeResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTrades(token, params);
        call.enqueue(new Callback<TradeResult>() {
            @Override
            public void onResponse(Call<TradeResult> call, Response<TradeResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allTrades.setValue(response.body().getItems());
            }

            @Override
            public void onFailure(Call<TradeResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
