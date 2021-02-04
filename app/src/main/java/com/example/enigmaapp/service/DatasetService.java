package com.example.enigmaapp.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.db.BatchedDao;
import com.example.enigmaapp.db.BatchedDatabase;
import com.example.enigmaapp.db.CurrencyDao;
import com.example.enigmaapp.db.CurrencyDatabase;
import com.example.enigmaapp.db.ExecutionTypeDao;
import com.example.enigmaapp.db.ExecutionTypeDatabase;
import com.example.enigmaapp.db.ProductDao;
import com.example.enigmaapp.db.ProductDatabase;
import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.Currency;
import com.example.enigmaapp.web.dataset.DatasetCurrency;
import com.example.enigmaapp.web.dataset.ExecutionType;
import com.example.enigmaapp.web.dataset.Product;
import com.example.enigmaapp.web.settlement.dataset.SettlementDatasetResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatasetService extends JobIntentService {
    private static final String TAG = "ExampleJobIntentService";
    public static final int JOB_ID = 1;

    private String passedToken;

    private MutableLiveData<TradeDatasetResult> tradeDataset = new MutableLiveData<>();
    private MutableLiveData<SettlementDatasetResult> settlementDataset = new MutableLiveData<>();

    private ProductDao productDao;
    private ExecutionTypeDao executionTypeDao;
    private BatchedDao batchedDao;
    // TODO: add back after dataset returns counterparty list
//    private CounterpartyDao counterpartyDao;
    private CurrencyDao currencyDao;

    private Handler mainHandler = new Handler();

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DatasetService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        Application app = getApplication();

        ProductDatabase productDatabase = ProductDatabase.getInstance(app);
        productDao = productDatabase.productDao();

        ExecutionTypeDatabase executionTypeDatabase = ExecutionTypeDatabase.getInstance(app);
        executionTypeDao = executionTypeDatabase.executionTypeDao();

        BatchedDatabase batchedDatabase = BatchedDatabase.getInstance(app);
        batchedDao = batchedDatabase.batchedDao();

        // TODO: add back after dataset returns counterparty list
//        CounterpartyDatabase counterpartyDatabase = CounterpartyDatabase.getInstance(app);
//        counterpartyDao = counterpartyDatabase.counterpartyDao();

        CurrencyDatabase currencyDatabase = CurrencyDatabase.getInstance(app);
        currencyDao = currencyDatabase.currencyDao();
    }

    // this method will run on a background thread automatically without us having to create our own thread:
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");
        passedToken = intent.getStringExtra("tokenExtra");
        fetchTradeDataset(passedToken);
        fetchSettlementDataset(passedToken);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

    class SortTradeDatasetRunnable implements Runnable {
        MutableLiveData<TradeDatasetResult> dataset;

        SortTradeDatasetRunnable(MutableLiveData<TradeDatasetResult> dataset) {
            this.dataset = dataset;
        }

        @Override
        public void run() {
            ArrayList<Product> productArray = (ArrayList) dataset.getValue().getProduct();

            // reset old products info
            if (productDao.getAllProducts() != null) {
                productDao.deleteAllProducts();
            }
            for (Product p : productArray) {
                productDao.insert(p);
            }

            ArrayList<ExecutionType> executionTypeArray = setExecutionList(dataset.getValue().getExecutionType());

            // reset old execution types info
            if (executionTypeDao.getAllExecutionTypes() != null) {
                executionTypeDao.deleteAllExecutionTypes();
            }
            for (ExecutionType e : executionTypeArray) {
                executionTypeDao.insert(e);
            }


            ArrayList<Batched> batchedArray = new ArrayList();
            batchedArray.add(new Batched("All trades", "-1"));
            batchedArray.add(new Batched("Not batched only", "0"));
            batchedArray.add(new Batched("Batched only", "1"));

            // reset old batched info
            if (batchedDao.getAllBatched() != null) {
                batchedDao.deleteAllBatched();
            }
            for (Batched b : batchedArray) {
                batchedDao.insert(b);
            }
        }
    }

    class SortSettlementDatasetRunnable implements Runnable {
        MutableLiveData<SettlementDatasetResult> dataset;

        public SortSettlementDatasetRunnable(MutableLiveData<SettlementDatasetResult> dataset) {
            this.dataset = dataset;
        }

        @Override
        public void run() {
            // TODO: add back after dataset returns counterparty list
//            ArrayList<Counterparty> counterpartyArray = (ArrayList) dataset.getValue().getCounterparty();
//            Log.d(TAG, "startThread: ______________________ inserting counterparties ______________________");
//            Log.d(TAG, "startThread: ______________________  counterpartyArray ______________________" + counterpartyArray);
//
//            // reset old counterparty info
//            if (counterpartyDao.getAllCounterparties() != null) {
//                counterpartyDao.deleteAllCounterparties();
//            }
//            for (Counterparty c : counterpartyArray) {
//                counterpartyDao.insert(c);
//            }

            ArrayList fiatArray = dataset.getValue().getCurrency();
            ArrayList cryptoArray = dataset.getValue().getCryptoCurrency();

            // reset old currency info
            if (currencyDao.getAllCurrencies() != null) {
                currencyDao.deleteAllCurrencies();
            }

            for (Object f : fiatArray) {
                Currency currency = new Currency(f.toString());
                currencyDao.insert(currency);
            }
            for (Object c : cryptoArray) {
                Currency currency = new Currency(c.toString());
                currencyDao.insert(currency);
            }
        }
    }

    // Trade dataset:
    public void fetchTradeDataset(String token) {
        Call<TradeDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetTradeDataset(token);
        call.enqueue(new Callback<TradeDatasetResult>() {
            @Override
            public void onResponse(Call<TradeDatasetResult> call, Response<TradeDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                tradeDataset.setValue(response.body());

                // sort & save received data sets :
                SortTradeDatasetRunnable runnable = new SortTradeDatasetRunnable(tradeDataset);
                new Thread(runnable).start();
            }

            @Override
            public void onFailure(Call<TradeDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
            }
        });
    }

    private ArrayList<ExecutionType> setExecutionList(ArrayList arrayList) {
        ArrayList<ExecutionType> result = new ArrayList<ExecutionType>();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = (String) arrayList.get(i);
            ExecutionType item = new ExecutionType(name);
            result.add(item);
        }
        return result;
    }

    // Settlement datasets:
    public void fetchSettlementDataset(String token) {
        Call<SettlementDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetUnitaryDataset(token);

        call.enqueue(new Callback<SettlementDatasetResult>() {
            @Override
            public void onResponse(Call<SettlementDatasetResult> call, Response<SettlementDatasetResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                settlementDataset.setValue(response.body());

                // sort & save received data sets :
                SortSettlementDatasetRunnable runnable = new SortSettlementDatasetRunnable(settlementDataset);
                new Thread(runnable).start();
            }

            @Override
            public void onFailure(Call<SettlementDatasetResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
            }
        });
    }

    private void setSettlementDatasetLists(SettlementDatasetResult dataset) {
        ArrayList fiatArray = (ArrayList) dataset.getCurrency();
        ArrayList cryptoArray = (ArrayList) dataset.getCryptoCurrency();

//        ArrayList combined = new ArrayList<DatasetCurrency>();

        for (int i = 0; i < fiatArray.size(); i++) {
            DatasetCurrency coin = new DatasetCurrency(fiatArray.get(i).toString());
//            currenciesDataset.add(coin);
        }
        for (int i = 0; i < cryptoArray.size(); i++) {
            DatasetCurrency coin = new DatasetCurrency(cryptoArray.get(i).toString());
//            currenciesDataset.add(coin);
        }
    }

//    public void fetchBatchDataset(String token) {
//        Call<BatchDatasetResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeGetBatchDataset(token);
//
//        call.enqueue(new Callback<BatchDatasetResult>() {
//            @Override
//            public void onResponse(Call<BatchDatasetResult> call, Response<BatchDatasetResult> response) {
//                if (!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code() + "Error: " + response.message());
//                    return;
//                }
//                setBatchDatasetLists(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<BatchDatasetResult> call, Throwable t) {
//                System.out.println("t.getMessage(): " + t.getMessage());
//            }
//        });
//    }

//    private void setBatchDatasetLists(BatchDatasetResult dataset) {
//        ArrayList productsArray = (ArrayList) dataset.getProducts();
//        productsDataset.setValue(productsArray);
//
//        ArrayList counterpartyArray = (ArrayList) dataset.getCounterparty();
//        counterpartyDatasetBatch.setValue(counterpartyArray);
//    }

}
