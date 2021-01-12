package com.example.enigmaapp.repository;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.trade.TradeResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetBatched;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.enigmaapp.activity.fragment.TradeFragment.progressBarTrade;
import static com.example.enigmaapp.activity.fragment.TradeFragment.tradeAdapter;

public class TradeRepository {

    private Application application;
    private HashMap<String, String> params = new HashMap<>();
    private ArrayList<TradeItemResult> allTrades = new ArrayList<>();
    private MutableLiveData<TradeDatasetResult> tradeDataset = new MutableLiveData<>();
    private MutableLiveData<List<TradeDatasetProduct>> productsDataset = new MutableLiveData<List<TradeDatasetProduct>>();
    private MutableLiveData<List<TradeDatasetCounterparty>> counterpartyDataset = new MutableLiveData<List<TradeDatasetCounterparty>>();
    private MutableLiveData<List<TradeDatasetExecutionType>> executionTypeDataset = new MutableLiveData<List<TradeDatasetExecutionType>>();
    private MutableLiveData<List<TradeDatasetBatched>> batcedDataset = new MutableLiveData<List<TradeDatasetBatched>>();

    public TradeRepository(Application application) {
        this.application = application;
    }

    public ArrayList<TradeItemResult> getTrades() {
        return allTrades;
    }

    public void fetchTrades(String token) {
//        List<String> itemsPerPageValues = new ArrayList<>();
//        itemsPerPageValues.add("5");
//
//        HashMap<String, Object> mapToSend = new HashMap<>();
//        mapToSend.put("items_per_page", itemsPerPageValues);
//
//        ProxyRetrofitQueryMap map = new ProxyRetrofitQueryMap(mapToSend);
//
//        List<String> currentPageValues = new ArrayList<>();
//        currentPageValues.add("1");
//        map.put("current_page", currentPageValues);
//
//        List<String> sortValues = new ArrayList<>();
//        sortValues.add("trade_id desc");
//        map.put("sort", sortValues);
//
//        List<String> productIdsValues = new ArrayList<>();
//        productIdsValues.add("17");
//        productIdsValues.add("6");
//        map.put("product_id", productIdsValues);
//
//        System.out.println(" MAP : " + map);
        params.put("items_per_page", "5");
        params.put("sort", "trade_id desc");
//        params1.put("trade_id", "431308");
//        params1.put("start_date", "2020-05-07");
//        params1.put("end_date", "2020-05-08");
//        params1.put("status[0]", "rejected");
//        params1.put("status[1]", "booked");
        System.out.println("params1 ______________ " + params);
        Call<TradeResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTrades(token, params);
        call.enqueue(new Callback<TradeResult>() {
            @Override
            public void onResponse(Call<TradeResult> call, Response<TradeResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBarTrade.setVisibility(View.INVISIBLE);
                    List<TradeItemResult> results = response.body().getItems();
                    parseTradeResult(results);
                    return;
                }
                System.out.println("fetchTrades - Code: " + response.code() + "Error: " + response.message());
            }

            @Override
            public void onFailure(Call<TradeResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseTradeResult(List<TradeItemResult> list) {
        for (TradeItemResult item : list) {
            allTrades.add(item);
        }
        System.out.println("______ in repo - all trade size: " + allTrades.size());
        tradeAdapter.notifyDataSetChanged();
    }

    public HashMap<String, String> setParams(HashMap<String, String> paramsReceived) {
        Iterator it = paramsReceived.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println("setting paramsReceived in repository: " + pair.getKey() + " = " + pair.getValue());
            params.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return params;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void resetParams() {
        this.params.clear();
    }

    public void resetTradesList() {
        this.allTrades.clear();
    }

    public void removeFromParams(String key) {
        System.out.println("removeFromParams in repository - key received:  " + key);
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                System.out.println("removeFromParams in repository - found key! -> " + key);
                it.remove();
            }
        }
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

    public MutableLiveData<List<TradeDatasetBatched>> getBatchedDataset() {
        return batcedDataset;
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

        ArrayList batchedArray = new ArrayList();
        batchedArray.add(new TradeDatasetBatched("All trades", "-1"));
        batchedArray.add(new TradeDatasetBatched("Not batched only", "0"));
        batchedArray.add(new TradeDatasetBatched("Batched only", "1"));
        batcedDataset.setValue(batchedArray);
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
