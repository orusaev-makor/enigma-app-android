package com.example.enigmaapp.web;

import com.example.enigmaapp.web.accounts.AccountsItemResult;
import com.example.enigmaapp.web.login.LoginResult;
import com.example.enigmaapp.web.settlement.SettlementResult;
import com.example.enigmaapp.web.trade.TradeResult;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    @GET("/historial_pnl/{id}")
    Call<ArrayList<com.example.enigmaapp.HistoricalPnl>> getHistoricalPnl(@Header("Authorization") String token,
                                                                          @Path("id") int userId);

    // User Auth :
    @PUT("/auth")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/password")
    Call<Void> executeForgotPassword(@Body HashMap<String, String> map);

    @DELETE("/auth")
    Call<Void> executeLogout(@Header("Authorization") String token);

    // Balance:
    @GET("/balance/248")
    Call<Object> executeGetBalance(@Header("Authorization") String token);

    // Trades:
    @GET("/trade")
    Call<TradeResult> executeGetTrades(@Header("Authorization") String token,
                                       @QueryMap Map<String, String> params);

    // Settlements:
    @GET("/settlement_batch")
    Call<SettlementResult> executeGetBatch(@Header("Authorization") String token,
                                           @QueryMap Map<String, String> params);
    @GET("/settlement")
    Call<SettlementResult> executeGetUnitary(@Header("Authorization") String token,
                                          @QueryMap Map<String, String> params);


    // Accounts:
    @GET("/account/248")
    Call<ArrayList<AccountsItemResult>> executeGetAccounts(@Header("Authorization") String token);

    // Datasets:
    @GET("/dataset/trade")
    Call<TradeDatasetResult> executeGetTradeDataset(@Header("Authorization") String token);
}
