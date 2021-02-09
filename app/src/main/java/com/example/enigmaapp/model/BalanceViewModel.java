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

    public BalanceViewModel(@NonNull Application application) {
        super(application);
        repository = new BalanceRepository(application);
    }

    public void fetchBalances(String token) {
        repository.fetchBalances(token);
    }

    public MutableLiveData<List<BalanceItemResult>> getBalanceMap() { return repository.getAllBalances(); }
}
