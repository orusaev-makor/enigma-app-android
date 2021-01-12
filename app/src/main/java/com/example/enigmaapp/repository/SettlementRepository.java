package com.example.enigmaapp.repository;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.settlement.SettlementItemResult;
import com.example.enigmaapp.web.settlement.SettlementResult;
import com.example.enigmaapp.web.settlement.dataset.BatchDatasetCounterparty;
import com.example.enigmaapp.web.settlement.dataset.BatchDatasetProduct;
import com.example.enigmaapp.web.settlement.dataset.BatchDatasetResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

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
    private HashMap<String, String> params = new HashMap<>();
    private ArrayList<SettlementSummary> allBatch = new ArrayList<>();
    private ArrayList<SettlementSummary> allUnitary = new ArrayList<>();

    private MutableLiveData<ArrayList<String>> statusDataset = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<List<TradeDatasetProduct>> productsDataset = new MutableLiveData<List<TradeDatasetProduct>>();
    private MutableLiveData<List<TradeDatasetCounterparty>> counterpartyDataset = new MutableLiveData<List<TradeDatasetCounterparty>>();

    public SettlementRepository(Application application) {
        this.application = application;
    }

    public ArrayList<SettlementSummary> getUnitary() {
        return allUnitary;
    }
    public ArrayList<SettlementSummary> getBatch() { return allBatch; }

    public void fetchBatch(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("items_per_page", "5");
        params.put("sort", "settlement_batch_id desc");
//        params.put("product_id", "1");
//        params.put("counterparty_id", "99");
        params.put("status[0]", "pending");
        params.put("status[1]", "confirmed");
        params.put("status[2]", "settled");
        params.put("status[3]", "rejected");

        Call<SettlementResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatch(token, params);
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
        HashMap<String, String> params = new HashMap<>();
        params.put("items_per_page", "5");
        params.put("sort", "settlement_id desc");
//        params.put("start_date", "2020-05-07");
//        params.put("end_date", "2020-05-08");
        params.put("currency", "EUR");
        params.put("side", "to send");
//        params.put("counterparty_id", "64");
        params.put("status[0]", "confirmed");
        params.put("status[1]", "pending");
        params.put("status[2]", "settled");
        params.put("status[3]", "rejected");
//        params.put("counterparty_id_list[0]", "14");
//        params.put("counterparty_id_list[1]", "99");
        params.put("currency_list[0]", "EUR");
        params.put("currency_list[1]", "BTC");
        params.put("currency_list[3]", "USD");
        Call<SettlementResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetUnitary(token, params);
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
            allBatch.add(summary);
        }
    }

    private void unitaryToSummaryList(List<SettlementItemResult> list) {
        for (int i = 0; i < list.size(); i++) {
            SettlementItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getCurrency(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), false);
            allUnitary.add(summary);
        }
    }

    public HashMap<String, String> setParams(HashMap<String, String> paramsReceived) {
        Iterator it = paramsReceived.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println("setting paramsReceived in repository: " + pair.getKey() + " = " + pair.getValue());
            params.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return params;
    }

    public HashMap<String, String> getParams() { return params; }

    public void fetchBatchDataset(String token) {
        Call<BatchDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatchDataset(token);

        call.enqueue(new Callback<BatchDatasetResult>() {
            @Override
            public void onResponse(Call<BatchDatasetResult> call, Response<BatchDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                setDatasetLists(response.body());
            }

            @Override
            public void onFailure(Call<BatchDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDatasetLists(BatchDatasetResult dataset) {
        System.out.println("Plain _________________ vdataset - getProducts :  " + dataset.getProducts());
        System.out.println("Plain _________________ vdataset   - getStatus :  " + dataset.getStatus());
        ArrayList productsArray = (ArrayList) dataset.getProducts();
        productsDataset.setValue(productsArray);

        ArrayList counterpartyArray = (ArrayList) dataset.getCounterparty();
        counterpartyDataset.setValue(counterpartyArray);
        System.out.println("Dataset in Repository - productsArray: " + productsArray);
        System.out.println("Dataset in Repository - counterpartyArray: " + counterpartyArray);
    }

    public MutableLiveData<List<TradeDatasetProduct>> getProductsDataset() {
        return productsDataset;
    }

    public void removeFromParams(String key) {
        System.out.println("removeFromParams in repository - key received:  " + key);
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                System.out.println("removeFromParams in repository - found key! -> " + key);
                it.remove();
            }
        }
    }

    public MutableLiveData<List<TradeDatasetCounterparty>> getCounterpartyDataset() { return counterpartyDataset; }

    public void resetParams() { this.params.clear(); }

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

        public void setCounterparty(String counterparty) { this.counterparty = counterparty; }

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

        public int getId() { return id; }

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
