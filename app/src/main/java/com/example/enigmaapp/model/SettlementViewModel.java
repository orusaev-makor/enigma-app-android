package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.enigmaapp.repository.SettlementRepository;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void setParams(HashMap<String, String> params) { repository.setParams(params); }
}
