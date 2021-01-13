package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.trade.dataset.DatasetCurrency;
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

    public ArrayList<SettlementRepository.SettlementSummary> getBatchSettlements() { return repository.getBatchSettlements(); }
    public ArrayList<SettlementRepository.SettlementSummary> getUnitarySettlements() { return repository.getUnitarySettlements(); }

    public HashMap<String, String> getBatchParams() { return repository.getBatchParams(); }
    public HashMap<String, String> getUnitaryParams() { return repository.getUnitaryParams(); }

    public void setBatchParams(HashMap<String, String> params) { repository.setBatchParams(params); }
    public void setUnitaryParams(HashMap<String, String> params) { repository.setUnitaryParams(params); }

    public void fetchBatchDataset(String token) { repository.fetchBatchDataset(token); }
    public void fetchUnitaryDataset(String token) { repository.fetchUnitaryDataset(token); }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() { return repository.getProductsDataset(); }
    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return repository.getCounterpartyDataset(); }

    public void removeFromBatchParams(String key) { repository.removeFromBatchParams(key); }
    public void removeFromUnitaryParams(String key) { repository.removeFromUnitaryParams(key); }

    public void resetBatchParams() { repository.resetBatchParams(); }
    public void resetUnitaryParams() { repository.resetUnitaryParams(); }

    public void resetBatchList() { repository.resetBatchList(); }
    public void resetUnitaryList() { repository.resetUnitaryList(); }

    public ArrayList<DatasetCurrency> getCurrencyDataset() {
        return repository.getCurrencyDataset();
    }
}
