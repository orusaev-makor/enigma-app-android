package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeFilterRepository {

    private MutableLiveData<TradeDatasetResult> tradeDataset = new MutableLiveData<>();
    private MutableLiveData<List<TradeDatasetProduct>> productsDataset = new MutableLiveData<List<TradeDatasetProduct>>();
    private MutableLiveData<List<TradeDatasetCounterparty>> counterpartyDataset = new MutableLiveData<List<TradeDatasetCounterparty>>();
//    private MutableLiveData<ArrayList<String>> executionTypeDataset = new MutableLiveData<ArrayList<String>>();
    MutableLiveData<List<TradeDatasetExecutionType>> executionTypeDataset = new MutableLiveData<List<TradeDatasetExecutionType>>();
    private MutableLiveData<ArrayList<String>> statusDataset = new MutableLiveData<ArrayList<String>>();

    private Application application;

    public TradeFilterRepository(Application application) {
        this.application = application;
    }

    public LiveData<TradeDatasetResult> getTradeDataset() {
        return tradeDataset;
    }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() {
        return productsDataset;
    }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() {
        return counterpartyDataset;
    }

    public MutableLiveData<List<TradeDatasetExecutionType>> getExecutionTypeDataset() {
        return executionTypeDataset;
    }

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
                System.out.println(" _______DATA SET RECEIVED: " + response.body().getProduct());
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
        ArrayList productsArrayList = (ArrayList) dataset.getProduct();
        System.out.println("Product array list: "+ productsArrayList);
        productsDataset.setValue(productsArrayList);

        ArrayList executionTypeArray = (ArrayList) dataset.getExecutionType();
        List<TradeDatasetExecutionType> res = setExecutinList(executionTypeArray);
        executionTypeDataset.setValue(res);

        ArrayList statusArrayList = (ArrayList) dataset.getExecutionType();
        statusDataset.setValue(statusArrayList);

        System.out.println("IN TRADE FILTER REPOSITORY : " + productsDataset);
    }

    private List<TradeDatasetExecutionType> setExecutinList(ArrayList arrayList) {
        List<TradeDatasetExecutionType> result = new ArrayList<TradeDatasetExecutionType>();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = (String) arrayList.get(i);
            TradeDatasetExecutionType item = new TradeDatasetExecutionType(name);
            result.add(item);
        }
        return result;
    }
}
