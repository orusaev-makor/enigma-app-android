package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.TradeRepository;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.dataset.DatasetBatched;
import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetExecutionType;
import com.example.enigmaapp.web.dataset.DatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradeViewModel extends AndroidViewModel {

    private TradeRepository repository;

    public TradeViewModel(@NonNull Application application) {
        super(application);
        repository = new TradeRepository(application);
    }

    public void fetchTrades(String token) { repository.fetchTrades(token); }

    public ArrayList<TradeItemResult> getTrades() {
        return repository.getTrades();
    }

    public void fetchTradeDataset(String token) {
        repository.fetchTradeDataset(token);
    }

    public LiveData<TradeDatasetResult> getTradeDataset() { return repository.getTradeDataset(); }

    public MutableLiveData<List<DatasetProduct>> getProductsDataset() { return repository.getProductsDataset(); }

    public MutableLiveData<List<DatasetCounterparty>> getCounterpartyDataset() { return repository.getCounterpartyDataset(); }

    public MutableLiveData<List<DatasetExecutionType>> getExecutionTypeDataset() { return repository.getExecutionTypeDataset(); }

    public MutableLiveData<List<DatasetBatched>> getBatchedDataset() {
        return repository.getBatchedDataset();
    }

    public HashMap<String, String> getParams() { return repository.getParams(); }

    public void setParams(HashMap<String, String> params) { repository.setParams(params); }

    public void resetParams() { repository.resetParams(); }

    public void removeFromParams(String key) {
        repository.removeFromParams(key);
    }

    public void resetTradesList() { repository.resetTradesList(); }
}
