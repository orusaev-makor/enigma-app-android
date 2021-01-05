package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.trade.TradeResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeRepository {
    private HashMap<String, String> params = new HashMap<>();
    private MutableLiveData<List<TradeItemResult>> allTrades = new MutableLiveData<>();
    private Application application;
    private MutableLiveData<TradeDatasetResult> tradeDataset = new MutableLiveData<>();
    private MutableLiveData<List<TradeDatasetProduct>> productsDataset = new MutableLiveData<List<TradeDatasetProduct>>();
    private MutableLiveData<List<TradeDatasetCounterparty>> counterpartyDataset = new MutableLiveData<List<TradeDatasetCounterparty>>();
    private MutableLiveData<List<TradeDatasetExecutionType>> executionTypeDataset = new MutableLiveData<List<TradeDatasetExecutionType>>();
    private MutableLiveData<ArrayList<String>> statusDataset = new MutableLiveData<ArrayList<String>>();

    public TradeRepository(Application application) {
        this.application = application;
    }

    public LiveData<List<TradeItemResult>> getTrades() {
        return allTrades;
    }

    public void fetchTrades(String token) {
        this.params.put("items_per_page", "5");
        this.params.put("current_page", "1");
        this.params.put("sort", "trade_id desc");

//        HashMap<String, String> params = new HashMap<>();
//        params.put("items_per_page", "5");
//        params.put("current_page", "1");
//        params.put("sort", "trade_id desc");
//        params.put("counterparty_id", "249");
//        params.put("product_id", "2");
//        params.put("already_batched", "0");

//        setParams(params);
        Call<TradeResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTrades(token, this.params);
        call.enqueue(new Callback<TradeResult>() {
            @Override
            public void onResponse(Call<TradeResult> call, Response<TradeResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchTrades - Code: " + response.code() + "Error: " + response.message());
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

    public void setParams(HashMap<String, String> params) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            this.params.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    public void resetParams() {
        this.params.clear();
    }

    public HashMap<String, String> getParams() { return params; }

    public LiveData<TradeDatasetResult> getTradeDataset() {
        return tradeDataset;
    }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() { return productsDataset; }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return counterpartyDataset; }

    public MutableLiveData<List<TradeDatasetExecutionType>> getExecutionTypeDataset() { return executionTypeDataset; }

    public MutableLiveData<ArrayList<String>> getStatusDataset() {
        return statusDataset;
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
                setDatasetLists(response.body());
            }

            @Override
            public void onFailure(Call<TradeDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDatasetLists(TradeDatasetResult dataset) {
        ArrayList productsArray = (ArrayList) dataset.getProduct();
        productsDataset.setValue(productsArray);

        ArrayList executionTypeArray = (ArrayList) dataset.getExecutionType();
        List<TradeDatasetExecutionType> executionList = setExecutionList(executionTypeArray);
        executionTypeDataset.setValue(executionList);

        ArrayList statusArray = (ArrayList) dataset.getExecutionType();
        statusDataset.setValue(statusArray);
    }

    private List<TradeDatasetExecutionType> setExecutionList(ArrayList arrayList) {
        List<TradeDatasetExecutionType> result = new ArrayList<TradeDatasetExecutionType>();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = (String) arrayList.get(i);
            TradeDatasetExecutionType item = new TradeDatasetExecutionType(name);
            result.add(item);
        }
        return result;
    }
}
