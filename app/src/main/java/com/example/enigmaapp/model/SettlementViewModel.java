package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.settlement.SettlementItemResult;

import java.util.List;

public class SettlementViewModel extends AndroidViewModel {

    private SettlementRepository repository;

    public SettlementViewModel(@NonNull Application application) {
        super(application);
        repository = new SettlementRepository(application);
    }

    public void fetchBatchSettlements(String token) {
        repository.fetchBatchSettlements(token);
    }

    public LiveData<List<SettlementRepository.SettlementSummary>> getSettlements() {
        return repository.getAllSettlements();
    }

    public void fetchUnitarySettlements(String token) {
        repository.fetchUnitarySettlements(token);
    }

}
