package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.balance.BalanceItemResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceRepository {

    private MutableLiveData<List<BalanceItemResult>> allBalances = new MutableLiveData<List<BalanceItemResult>>();
    private Application application;

    public BalanceRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<BalanceItemResult>> getAllBalances() {
        return allBalances;
    }

    public void setAllBalances(MutableLiveData<List<BalanceItemResult>> allBalances) {
        this.allBalances = allBalances;
    }

    public void fetchBalances(String token) {
        Call<HashMap<String, String>> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBalance(token);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchBalances - Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                setBalancesList(response.body());
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
//                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBalancesList(HashMap<String, String> result) {
        ArrayList balancesArray = new ArrayList();
        Iterator it = result.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            balancesArray.add(new BalanceItemResult(entry.getKey().toString(), entry.getValue().toString()));
            it.remove();
        }
        allBalances.setValue(balancesArray);
    }
}
