package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.TradeFilterRepository;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.List;

public class TradeFilterViewModel extends AndroidViewModel {

    private TradeFilterRepository repository;

    public TradeFilterViewModel(@NonNull Application application) {
        super(application);
        repository = new TradeFilterRepository(application);
    }

    public void fetchTradeDataset(String token) {
        repository.fetchTradeDataset(token);
    }

    public LiveData<TradeDatasetResult> getTradeDataset() {
        return repository.getTradeDataset();
    }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() { return repository.getProductsDataset(); }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return repository.getCounterpartyDataset(); }

    public MutableLiveData<List<TradeDatasetExecutionType>> getExecutionTypeDataset() { return repository.getExecutionTypeDataset(); }

    public MutableLiveData<ArrayList<String>> getStatusDataset() { return repository.getStatusDataset(); }
}
