package com.example.enigmaapp.repository;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.db.BatchedDao;
import com.example.enigmaapp.db.BatchedDatabase;
import com.example.enigmaapp.db.ExecutionTypeDao;
import com.example.enigmaapp.db.ExecutionTypeDatabase;
import com.example.enigmaapp.db.ProductDao;
import com.example.enigmaapp.db.ProductDatabase;
import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.ExecutionType;
import com.example.enigmaapp.web.dataset.Product;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.trade.TradeResult;
import com.example.enigmaapp.web.dataset.DatasetBatched;
import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetExecutionType;
import com.example.enigmaapp.web.dataset.DatasetProduct;
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
    private MutableLiveData<List<DatasetProduct>> productsDataset = new MutableLiveData<List<DatasetProduct>>();
    private MutableLiveData<List<DatasetExecutionType>> executionTypeDataset = new MutableLiveData<List<DatasetExecutionType>>();
    private MutableLiveData<List<DatasetBatched>> batcedDataset = new MutableLiveData<List<DatasetBatched>>();

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts = new MutableLiveData<>();

    private ExecutionTypeDao executionTypeDao;
    private LiveData<List<ExecutionType>> allExecutionTypes = new MutableLiveData<>();

    private BatchedDao batchedDao;
    private LiveData<List<Batched>> allBatched = new MutableLiveData<>();

    public TradeRepository(Application application) {
        this.application = application;

        ProductDatabase productDatabase = ProductDatabase.getInstance(application);
        productDao = productDatabase.productDao();
        allProducts = productDao.getAllProducts();

        ExecutionTypeDatabase executionTypeDatabase = ExecutionTypeDatabase.getInstance(application);
        executionTypeDao = executionTypeDatabase.executionTypeDao();
        allExecutionTypes = executionTypeDao.getAllExecutionTypes();

        BatchedDatabase batchedDatabase = BatchedDatabase.getInstance(application);
        batchedDao = batchedDatabase.batchedDao();
        allBatched = batchedDao.getAllBatched();
    }

    public ArrayList<TradeItemResult> getTrades() {
        return allTrades;
    }

    public void fetchTrades(String token) {

        params.put("items_per_page", "10");
        params.put("sort", "trade_id desc");
        Call<TradeResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTrades(token, params);
        System.out.println("----------------------------------------------------------- fetch call made with params: " + params);
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
        tradeAdapter.notifyDataSetChanged();
    }

    public HashMap<String, String> setParams(HashMap<String, String> paramsReceived) {
        Iterator it = paramsReceived.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            params.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return params;
    }

    public void resetParams() {
        this.params.clear();
    }

    public void resetTradesList() {
        this.allTrades.clear();
    }

    public void removeFromParams(String key) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }

    public MutableLiveData<List<DatasetBatched>> getBatchedDataset() {
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
//                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDatasetLists(TradeDatasetResult dataset) {
        ArrayList productsArray = (ArrayList) dataset.getProduct();
        productsDataset.setValue(productsArray);

        ArrayList executionTypeArray = (ArrayList) dataset.getExecutionType();
        List<DatasetExecutionType> executionList = setExecutionList(executionTypeArray);
        executionTypeDataset.setValue(executionList);

        ArrayList batchedArray = new ArrayList();
        batchedArray.add(new DatasetBatched("All trades", "-1"));
        batchedArray.add(new DatasetBatched("Not batched only", "0"));
        batchedArray.add(new DatasetBatched("Batched only", "1"));
        batcedDataset.setValue(batchedArray);
    }

    private List<DatasetExecutionType> setExecutionList(ArrayList arrayList) {
        List<DatasetExecutionType> result = new ArrayList<DatasetExecutionType>();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = (String) arrayList.get(i);
            DatasetExecutionType item = new DatasetExecutionType(name);
            result.add(item);
        }
        return result;
    }

    // product dataset
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    // execution type dataset
    public LiveData<List<ExecutionType>> getAllExecutionTypes() {
        return allExecutionTypes;
    }

    // batched dataset
    public LiveData<List<Batched>> getAllBatched() {
        return allBatched;
    }
}
