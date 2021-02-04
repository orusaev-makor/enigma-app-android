package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.AccountsRepository;
import com.example.enigmaapp.web.accounts.AccountsItemResult;

import java.util.List;

public class AccountsViewModel extends AndroidViewModel {

    private AccountsRepository repository;

    public AccountsViewModel(@NonNull Application app) {
        super(app);
        repository = new AccountsRepository(app);
    }

    public void fetchAccounts(String token) {
        repository.fetchAccounts(token);
    }

    public MutableLiveData<List<AccountsItemResult>> getAccounts() {
        return repository.getAccounts();
    }

    public LiveData<List<AccountsItemResult>> getFiatAccounts() {
        return repository.getFiatAccounts();
    }

    public LiveData<List<AccountsItemResult>> getCryptoAccounts() {
        return repository.getCryptoAccounts();
    }
}
