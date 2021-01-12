package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.settlement.dataset.BatchDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettlementViewModel extends AndroidViewModel {

    private SettlementRepository repository;

    public SettlementViewModel(@NonNull Application application) {
        super(application);
        repository = new SettlementRepository(application);
    }

    public void fetchBatch(String token) {
        repository.fetchBatch(token);
    }
    public void fetchUnitary(String token) { repository.fetchUnitary(token); }

    public ArrayList<SettlementRepository.SettlementSummary> getBatch() { return repository.getBatch(); }
    public ArrayList<SettlementRepository.SettlementSummary> getUnitary() { return repository.getUnitary(); }

    public HashMap<String, String> getParams() { return repository.getParams(); }

    public void setParams(HashMap<String, String> params) { repository.setParams(params); }

    public void fetchBatchDataset(String token) { repository.fetchBatchDataset(token); }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() { return repository.getProductsDataset(); }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return repository.getCounterpartyDataset(); }

    public void removeFromParams(String key) { repository.removeFromParams(key); }

    public void resetParams() {
        repository.resetParams();
    }
}
