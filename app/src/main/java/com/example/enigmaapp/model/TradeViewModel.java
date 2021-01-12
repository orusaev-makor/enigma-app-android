package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.TradeRepository;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetBatched;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
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

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() { return repository.getProductsDataset(); }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return repository.getCounterpartyDataset(); }

    public MutableLiveData<List<TradeDatasetExecutionType>> getExecutionTypeDataset() { return repository.getExecutionTypeDataset(); }

    public MutableLiveData<List<TradeDatasetBatched>> getBatchedDataset() {
        return repository.getBatchedDataset();
    }

    public HashMap<String, String> getParams() { return repository.getParams(); }

    public void setParams(HashMap<String, String> params) { repository.setParams(params); }

    public void resetParams() { repository.resetParams(); }

    public void removeFromParams(String key) {
        repository.removeFromParams(key);
    }
}
