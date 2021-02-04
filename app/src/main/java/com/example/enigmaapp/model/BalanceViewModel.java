package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.BalanceRepository;
import com.example.enigmaapp.web.balance.BalanceItemResult;

import java.util.HashMap;
import java.util.List;

public class BalanceViewModel extends AndroidViewModel {

    private BalanceRepository repository;

    public BalanceViewModel(@NonNull Application app) {
        super(app);
        repository = new BalanceRepository(app);
    }

    public void fetchBalances(String token) {
        repository.fetchBalances(token);
    }

    public MutableLiveData<List<BalanceItemResult>> getBalanceMap() { return repository.getAllBalances(); }
}
