package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.settlement.SettlementItemResult;
import com.example.enigmaapp.web.settlement.SettlementResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettlementRepository {

    private MutableLiveData<List<SettlementSummary>> allSettlements = new MutableLiveData<>();
    private Application application;

    public SettlementRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<SettlementSummary>> getAllSettlements() {
        return allSettlements;
    }

    public void fetchBatchSettlements(String token) {
        HashMap<String, String> params = new HashMap<>();
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
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allSettlements.setValue(batchToSummaryList(response.body().getItems()));
            }

            @Override
            public void onFailure(Call<SettlementResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchUnitarySettlements(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("items_per_page", "5");
        params.put("current_page", "1");
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
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allSettlements.setValue(unitaryToSummaryList(response.body().getItems()));
            }

            @Override
            public void onFailure(Call<SettlementResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<SettlementSummary> batchToSummaryList(List<SettlementItemResult> list) {
        List<SettlementSummary> result = new ArrayList<SettlementSummary>();
        for (int i = 0; i < list.size(); i++) {
            SettlementItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getProduct(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(),  item.getSettlementId(),
                    item.getBatchId(), true);
            result.add(summary);
        }
        return result;
    }

    private List<SettlementSummary> unitaryToSummaryList(List<SettlementItemResult> list) {
        List<SettlementSummary> result = new ArrayList<SettlementSummary>();
        for (int i = 0; i < list.size(); i++) {
            SettlementItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getCurrency(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), false);
            result.add(summary);
        }
        return result;
    }

    public class SettlementSummary {
        private String name;
        private String counterparty;
        private String date;
        private String status;
        private int id;
        private int batchId;
        private boolean isBatch;

        public SettlementSummary(String name, String counterparty, String date, String status, int id, int batchId,  boolean isBatch) {
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
