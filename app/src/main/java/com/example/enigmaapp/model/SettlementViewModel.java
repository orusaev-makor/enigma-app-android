package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.dataset.Currency;
import com.example.enigmaapp.web.dataset.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettlementViewModel extends AndroidViewModel {

    private SettlementRepository repository;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<Currency>> allCurrencies;

    public SettlementViewModel(@NonNull Application app) {
        super(app);
        repository = new SettlementRepository(app);
        allProducts = repository.getAllProducts();
        allCurrencies = repository.getAllCurrencies();
    }

    public void fetchBatch(String token) {
        repository.fetchBatch(token);
    }
    public void fetchUnitary(String token) { repository.fetchUnitary(token); }

    public ArrayList<SettlementRepository.SettlementSummary> getBatchSettlements() { return repository.getBatchSettlements(); }
    public ArrayList<SettlementRepository.SettlementSummary> getUnitarySettlements() { return repository.getUnitarySettlements(); }

    public void setBatchParams(HashMap<String, String> params) { repository.setBatchParams(params); }
    public void setUnitaryParams(HashMap<String, String> params) { repository.setUnitaryParams(params); }

    public void removeFromBatchParams(String key) { repository.removeFromBatchParams(key); }
    public void removeFromUnitaryParams(String key) { repository.removeFromUnitaryParams(key); }

    public void removeFromUnitaryParamsContainsKey(String containsKey) {
        repository.removeFromUnitaryParamsContainsKey(containsKey);
    }

    public void resetBatchParams() { repository.resetBatchParams(); }
    public void resetUnitaryParams() { repository.resetUnitaryParams(); }

    public void resetBatchList() { repository.resetBatchList(); }
    public void resetUnitaryList() { repository.resetUnitaryList(); }

    // product dataset
    public LiveData<List<Product>> getAllProducts() { return allProducts; }

    // currency dataset
    public LiveData<List<Currency>> getAllCurrencies() {
        return allCurrencies;
    }
}
