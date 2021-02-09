package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.SettingsRepository;
import com.example.enigmaapp.web.settings.ExposureLimitItemResult;
import com.example.enigmaapp.web.settings.MaxQtyPerTradeItemResult;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsRepository repository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new SettingsRepository(application);
    }

    public void fetchLimitExposures(String token) {
        repository.fetchLimitExposures(token);
    }

    public void fetchMaxPerTrades(String token) {
        repository.fetchMaxPerTrades(token);
    }

    public MutableLiveData<List<ExposureLimitItemResult>> getAllLimitExposures() {
        return repository.getAllLimitExposures();
    }

    public MutableLiveData<List<MaxQtyPerTradeItemResult>> getAllMaxPerTrades() {
        return repository.getAllMaxPerTrades();
    }

}
