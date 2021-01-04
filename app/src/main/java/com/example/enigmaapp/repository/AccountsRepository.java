package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.accounts.AccountResult;
import com.example.enigmaapp.web.accounts.AccountsItemResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountsRepository {

    private MutableLiveData<List<AccountsItemResult>> allAccounts = new MutableLiveData<List<AccountsItemResult>>();
    private MutableLiveData<List<AccountsItemResult>> allFiatAccounts = new MutableLiveData<List<AccountsItemResult>>();
    private MutableLiveData<List<AccountsItemResult>> allCryptoAccounts = new MutableLiveData<List<AccountsItemResult>>();
    private Application application;

    public AccountsRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<AccountsItemResult>> getAccounts() {
        return allAccounts;
    }

    public LiveData<List<AccountsItemResult>> getFiatAccounts() {
        return allFiatAccounts;
    }

    public LiveData<List<AccountsItemResult>> getCryptoAccounts() {
        return allCryptoAccounts;
    }

    public void fetchAccounts(String token) {
        Call<ArrayList<AccountsItemResult>> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetAccounts(token);
        call.enqueue(new Callback<ArrayList<AccountsItemResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountsItemResult>> call, Response<ArrayList<AccountsItemResult>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchAccounts - Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allAccounts.setValue(response.body());

                System.out.println(" response.bodY : " + response.body());
                setFiatAndCryptoLists(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<AccountsItemResult>> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFiatAndCryptoLists(ArrayList<AccountsItemResult> accountsList) {
        for (AccountsItemResult account : accountsList) {

            String currency = account.getCurrency();
            String cryptoCurrency = account.getCryptoCurrency();

            if (cryptoCurrency != null) {
                if (allCryptoAccounts.getValue() == null) {
                    // First time.
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(account);
                    allCryptoAccounts.setValue(arrayList);
                } else {
                    // Update operation.
                    allCryptoAccounts.getValue().add(account);
                }
            }
            if (currency != null) {
                if (allFiatAccounts.getValue() == null) {
                    // First time.
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(account);
                    allFiatAccounts.setValue(arrayList);
                    System.out.println(" ");
                } else {
                    // Update operation.
                    allFiatAccounts.getValue().add(account);
                }
            }
        }
    }
}
