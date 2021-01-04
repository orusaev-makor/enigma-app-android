package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.enigmaapp.repository.TradeFilterRepository;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

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
}
