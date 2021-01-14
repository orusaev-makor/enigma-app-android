package com.example.enigmaapp.repository;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.settlement.SettlementItemResult;
import com.example.enigmaapp.web.settlement.SettlementResult;
import com.example.enigmaapp.web.settlement.dataset.BatchDatasetResult;
import com.example.enigmaapp.web.settlement.dataset.UnitaryDatasetResult;
import com.example.enigmaapp.web.dataset.DatasetCurrency;
import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.enigmaapp.activity.fragment.SettlementFragment.progressBarSettlement;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.settlementAdapter;

public class SettlementRepository {

    private Application application;
    private HashMap<String, String> batchParams = new HashMap<>();
    private HashMap<String, String> unitaryParams = new HashMap<>();
    private ArrayList<SettlementSummary> allBatchSettlements = new ArrayList<>();
    private ArrayList<SettlementSummary> allUnitarySettlements = new ArrayList<>();

    private MutableLiveData<ArrayList<String>> statusDataset = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<List<DatasetProduct>> productsDataset = new MutableLiveData<List<DatasetProduct>>();
    private MutableLiveData<List<DatasetCounterparty>> counterpartyDatasetBatch = new MutableLiveData<List<DatasetCounterparty>>();
    private ArrayList<DatasetCounterparty> counterpartyDataset = new ArrayList<>();
    private ArrayList<DatasetCurrency> currenciesDataset = new ArrayList<>();

    public SettlementRepository(Application application) {
        this.application = application;
    }

    public ArrayList<SettlementSummary> getBatchSettlements() {
        return allBatchSettlements;
    }

    public ArrayList<SettlementSummary> getUnitarySettlements() {
        return allUnitarySettlements;
    }

    public void fetchBatch(String token) {
        batchParams.put("items_per_page", "5");
        batchParams.put("sort", "settlement_batch_id desc");

//        System.out.println("params1 fetching batch ______________ " + batchParams);
        Call<SettlementResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatch(token, batchParams);
        call.enqueue(new Callback<SettlementResult>() {
            @Override
            public void onResponse(Call<SettlementResult> call, Response<SettlementResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBarSettlement.setVisibility(View.INVISIBLE);
                    List<SettlementItemResult> results = response.body().getItems();
                    batchToSummaryList(results);
                    settlementAdapter.notifyDataSetChanged();
                    return;
                }
                System.out.println("Code: " + response.code() + "Error: " + response.message());
            }

            @Override
            public void onFailure(Call<SettlementResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchUnitary(String token) {
        unitaryParams.put("items_per_page", "5");
        unitaryParams.put("sort", "settlement_id desc");

        System.out.println("params1 fetching unitary ______________ " + unitaryParams);

        Call<SettlementResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetUnitary(token, unitaryParams);
        call.enqueue(new Callback<SettlementResult>() {
            @Override
            public void onResponse(Call<SettlementResult> call, Response<SettlementResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBarSettlement.setVisibility(View.INVISIBLE);
                    List<SettlementItemResult> results = response.body().getItems();
                    unitaryToSummaryList(results);
                    settlementAdapter.notifyDataSetChanged();
                    return;
                }
                System.out.println("Code: " + response.code() + "Error: " + response.message());
            }

            @Override
            public void onFailure(Call<SettlementResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void batchToSummaryList(List<SettlementItemResult> list) {
        for (int i = 0; i < list.size(); i++) {
            SettlementItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getProduct(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), true);
            allBatchSettlements.add(summary);
        }
    }

    private void unitaryToSummaryList(List<SettlementItemResult> list) {
        for (int i = 0; i < list.size(); i++) {
            SettlementItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getCurrency(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), false);
            allUnitarySettlements.add(summary);
        }
    }

    public HashMap<String, String> setBatchParams(HashMap<String, String> paramsReceived) {
        Iterator it = paramsReceived.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            batchParams.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return batchParams;
    }

    public HashMap<String, String> setUnitaryParams(HashMap<String, String> paramsReceived) {
        Iterator it = paramsReceived.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            unitaryParams.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return unitaryParams;
    }

    public HashMap<String, String> getBatchParams() {
        return batchParams;
    }

    public HashMap<String, String> getUnitaryParams() {
        return unitaryParams;
    }

    public void fetchBatchDataset(String token) {
        Call<BatchDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatchDataset(token);

        call.enqueue(new Callback<BatchDatasetResult>() {
            @Override
            public void onResponse(Call<BatchDatasetResult> call, Response<BatchDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                setBatchDatasetLists(response.body());
            }

            @Override
            public void onFailure(Call<BatchDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchUnitaryDataset(String token) {
        Call<UnitaryDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetUnitaryDataset(token);

        call.enqueue(new Callback<UnitaryDatasetResult>() {
            @Override
            public void onResponse(Call<UnitaryDatasetResult> call, Response<UnitaryDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                setUnitaryDatasetLists(response.body());
            }

            @Override
            public void onFailure(Call<UnitaryDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBatchDatasetLists(BatchDatasetResult dataset) {
        ArrayList productsArray = (ArrayList) dataset.getProducts();
        productsDataset.setValue(productsArray);

        ArrayList counterpartyArray = (ArrayList) dataset.getCounterparty();
        System.out.println(")))))))))))))) caounterparty for batch :))))))))))))))  " + counterpartyArray.size());
        counterpartyDatasetBatch.setValue(counterpartyArray);
    }

    private void setUnitaryDatasetLists(UnitaryDatasetResult dataset) {
        ArrayList fiatArray = (ArrayList) dataset.getCurrency();
        ArrayList cryptoArray = (ArrayList) dataset.getCryptoCurrency();
        ArrayList<DatasetCounterparty> counterpartyArray = (ArrayList) dataset.getCounterparty();

//        ArrayList combined = new ArrayList<DatasetCurrency>();

        for (int i = 0; i < fiatArray.size(); i++) {
            DatasetCurrency coin = new DatasetCurrency(fiatArray.get(i).toString());
            currenciesDataset.add(coin);
        }
        for (int i = 0; i < cryptoArray.size(); i++) {
            DatasetCurrency coin = new DatasetCurrency(cryptoArray.get(i).toString());
            currenciesDataset.add(coin);
        }

        for (int i = 0; i < counterpartyArray.size(); i++) {
            counterpartyDataset.add(new DatasetCounterparty(counterpartyArray.get(i).getName(), counterpartyArray.get(i).getId()));
        }

    }

    public MutableLiveData<List<DatasetProduct>> getProductsDataset() {
        return productsDataset;
    }

    public void removeFromBatchParams(String key) {
        Iterator it = batchParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }

    public void removeFromUnitaryParams(String key) {
        Iterator it = unitaryParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }

    public void removeFromUnitaryParamsContainsKey(String containsKey) {
        Iterator it = unitaryParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().toString().contains(containsKey)) {
                it.remove();
            }
        }
    }

    public ArrayList<DatasetCounterparty> getCounterpartyDataset() { return this.counterpartyDataset; }
    public ArrayList<DatasetCurrency> getCurrencyDataset() { return this.currenciesDataset; }

    public void resetBatchParams() { this.batchParams.clear(); }

    public void resetUnitaryParams() { this.unitaryParams.clear(); }

    public void resetBatchList() { this.allBatchSettlements.clear(); }

    public void resetUnitaryList() { this.allUnitarySettlements.clear(); }

    public MutableLiveData<List<DatasetCounterparty>> getCounterpartyDatasetBatch() {
        return this.counterpartyDatasetBatch;
    }


    public class SettlementSummary {
        private String name;
        private String counterparty;
        private String date;
        private String status;
        private int id;
        private int batchId;
        private boolean isBatch;

        public SettlementSummary(String name, String counterparty, String date, String status, int id, int batchId, boolean isBatch) {
            this.name = name;
            this.counterparty = counterparty;
            this.date = date;
            this.status = status;
            this.id = id;
            this.batchId = batchId;
            this.isBatch = isBatch;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCounterparty() {
            return counterparty;
        }

        public void setCounterparty(String counterparty) {
            this.counterparty = counterparty;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBatchId() {
            return batchId;
        }

        public void setBatchId(int batchId) {
            this.batchId = batchId;
        }

        public boolean isBatch() {
            return isBatch;
        }

        public void setBatch(boolean batch) {
            isBatch = batch;
        }
    }
}
