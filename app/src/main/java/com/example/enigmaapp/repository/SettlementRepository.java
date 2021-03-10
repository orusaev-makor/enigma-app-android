package com.example.enigmaapp.repository;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.db.CurrencyDao;
import com.example.enigmaapp.db.CurrencyDatabase;
import com.example.enigmaapp.db.ProductDao;
import com.example.enigmaapp.db.ProductDatabase;
import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.dataset.Currency;
import com.example.enigmaapp.web.dataset.Product;
import com.example.enigmaapp.web.settlement.BatchItemResult;
import com.example.enigmaapp.web.settlement.BatchResult;

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

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts = new MutableLiveData<>();

    private CurrencyDao currencyDao;
    private LiveData<List<Currency>> allCurrencies = new MutableLiveData<>();

    public SettlementRepository(Application application) {
        this.application = application;

        ProductDatabase productDatabase = ProductDatabase.getInstance(application);
        productDao = productDatabase.productDao();
        allProducts = productDao.getAllProducts();

        CurrencyDatabase currencyDatabase = CurrencyDatabase.getInstance(application);
        currencyDao = currencyDatabase.currencyDao();
        allCurrencies = currencyDao.getAllCurrencies();
    }

    public ArrayList<SettlementSummary> getBatchSettlements() {
        return allBatchSettlements;
    }

    public ArrayList<SettlementSummary> getUnitarySettlements() {
        return allUnitarySettlements;
    }

    public void fetchBatch(String token) {
        batchParams.put("items_per_page", "10");
        batchParams.put("sort", "settlement_batch_id desc");
        Call<BatchResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatch(token, batchParams);
        call.enqueue(new Callback<BatchResult>() {
            @Override
            public void onResponse(Call<BatchResult> call, Response<BatchResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBarSettlement.setVisibility(View.INVISIBLE);
                    List<BatchItemResult> results = response.body().getItems();
                    batchToSummaryList(results);
                    settlementAdapter.notifyDataSetChanged();
                    return;
                }
                System.out.println("Code: " + response.code() + "Error: " + response.message());
            }

            @Override
            public void onFailure(Call<BatchResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
//                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchUnitary(String token) {
        unitaryParams.put("items_per_page", "10");
        unitaryParams.put("sort", "settlement_id desc");

//        System.out.println("params1 fetching unitary ______________ " + unitaryParams);
        Call<BatchResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetUnitary(token, unitaryParams);
        call.enqueue(new Callback<BatchResult>() {
            @Override
            public void onResponse(Call<BatchResult> call, Response<BatchResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBarSettlement.setVisibility(View.INVISIBLE);
                    List<BatchItemResult> results = response.body().getItems();
                    unitaryToSummaryList(results);
                    settlementAdapter.notifyDataSetChanged();
                    return;
                }
                System.out.println("Code: " + response.code() + "Error: " + response.message());
            }

            @Override
            public void onFailure(Call<BatchResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
//                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void batchToSummaryList(List<BatchItemResult> list) {
        for (int i = 0; i < list.size(); i++) {
            BatchItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getProduct(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), true, item.getSide(), item.getType(), item.getAmount(),
                    item.getSettledAmount(), item.getCounterpartyAccount(),item.getOpenedAmount(), item.getEnigmaAccount());
            allBatchSettlements.add(summary);
        }
    }

    private void unitaryToSummaryList(List<BatchItemResult> list) {
        for (int i = 0; i < list.size(); i++) {
            BatchItemResult item = list.get(i);
            SettlementSummary summary = new SettlementSummary(item.getCurrency(),
                    item.getCounterparty(), item.getSentAt(), item.getStatus(), item.getSettlementId(),
                    item.getBatchId(), false, item.getSide(), item.getType(), item.getAmount(),
                    item.getSettledAmount(), item.getCounterpartyAccount(),item.getOpenedAmount(), item.getEnigmaAccount());
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

    public void resetBatchParams() {
        this.batchParams.clear();
    }

    public void resetUnitaryParams() {
        this.unitaryParams.clear();
    }

    public void resetBatchList() {
        this.allBatchSettlements.clear();
    }

    public void resetUnitaryList() {
        this.allUnitarySettlements.clear();
    }

    public class SettlementSummary {
        private String name;
        private String counterparty;
        private String date;
        private String status;
        private String id;
        private String batchId;
        private boolean isBatch;

        private String side;
        private String type;
        private String amount;
        private String settleAmount;
        private String openAmount;
        private String counterpartyAccount;
        private String enigmaAccount;
//        private TextView openAmount;


        public SettlementSummary(String name, String counterparty, String date, String status,
                                 String id, String batchId, boolean isBatch, String side, String type,
                                 String amount, String settledAmount, String counterpartyAccount,String openAmount, String enigmaAccount) {
            this.name = name;
            this.counterparty = counterparty;
            this.date = date;
            this.status = status;
            this.id = id;
            this.batchId = batchId;
            this.isBatch = isBatch;
            this.side = side;
            this.type = type;
            this.amount = amount;
            this.settleAmount = settledAmount;
            this.counterpartyAccount = counterpartyAccount;
            this.openAmount = openAmount;
            this.enigmaAccount = enigmaAccount;
        }

        public String getSettleAmount() {
            return settleAmount;
        }

        public void setSettleAmount(String settleAmount) {
            this.settleAmount = settleAmount;
        }

        public String getEnigmaAccount() {
            return enigmaAccount;
        }

        public void setEnigmaAccount(String enigmaAccount) {
            this.enigmaAccount = enigmaAccount;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOpenAmount() {
            return openAmount;
        }

        public void setOpenAmount(String openAmount) {
            this.openAmount = openAmount;
        }

        public String getCounterpartyAccount() {
            return counterpartyAccount;
        }

        public void setCounterpartyAccount(String counterpartyAccount) {
            this.counterpartyAccount = counterpartyAccount;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public boolean isBatch() {
            return isBatch;
        }

        public void setBatch(boolean batch) {
            isBatch = batch;
        }
    }


    // product dataset
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    // currency dataset
    public LiveData<List<Currency>> getAllCurrencies() {
        return allCurrencies;
    }
}
