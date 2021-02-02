package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.TradeRepository;
import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.ExecutionType;
import com.example.enigmaapp.web.dataset.Product;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.dataset.DatasetBatched;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradeViewModel extends AndroidViewModel {

    private TradeRepository repository;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<ExecutionType>> allExecutionTypes;
    private LiveData<List<Batched>> allBatched;

    public TradeViewModel(@NonNull Application application) {
        super(application);
        repository = new TradeRepository(application);
        allProducts = repository.getAllProducts();
        allExecutionTypes = repository.getAllExecutionTypes();
        allBatched = repository.getAllBatched();
    }

    public void fetchTrades(String token) { repository.fetchTrades(token); }

    public ArrayList<TradeItemResult> getTrades() {
        return repository.getTrades();
    }

    public void fetchTradeDataset(String token) {
        repository.fetchTradeDataset(token);
    }

    public void setParams(HashMap<String, String> params) {
        repository.setParams(params); }

    public void resetParams() { repository.resetParams(); }

    public void removeFromParams(String key) {
        repository.removeFromParams(key);
    }

    public void resetTradesList() { repository.resetTradesList(); }

    // product dataset
    public LiveData<List<Product>> getAllProducts() { return allProducts; }

    // execution type dataset
    public LiveData<List<ExecutionType>> getAllExecutionTypes() { return allExecutionTypes; }

    // batched dataset
    public LiveData<List<Batched>> getAllBatched() { return allBatched; }
}
