package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.enigmaapp.repository.TradeRepository;
import com.example.enigmaapp.web.trade.TradeItemResult;

import java.util.List;

public class TradeViewModel extends AndroidViewModel {

    private TradeRepository repository;

    public TradeViewModel(@NonNull Application application) {
        super(application);
        repository = new TradeRepository(application);
    }

    public void fetchTrades() {
        repository.fetchTrades();
    }

    public LiveData<List<TradeItemResult>> getTrades() {
        return repository.getTrades();
    }
}
