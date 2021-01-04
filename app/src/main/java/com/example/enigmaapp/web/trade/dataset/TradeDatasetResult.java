package com.example.enigmaapp.web.trade.dataset;

import com.example.enigmaapp.web.trade.TradeItemResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TradeDatasetResult {

    ArrayList<String> side;
    @SerializedName("trade_type")
    ArrayList<String> tradeType;
    ArrayList<String> status;
    @SerializedName("displaying_side")
    ArrayList<String> displayingSide;
    @SerializedName("execution_type")
    ArrayList<String> executionType;
    List<TradeDatasetBroker> brokers;
    List<TradeDatasetCounterparty> counterparties;
    List<TradeDatasetCompany> companies;
    List<TradeDatasetProvider> providers;
    List<TradeDatasetProduct> products;

    public ArrayList<String> getSide() {
        return side;
    }

    public void setSide(ArrayList<String> side) {
        this.side = side;
    }

    public ArrayList<String> getTradeType() {
        return tradeType;
    }

    public void setTradeType(ArrayList<String> tradeType) {
        this.tradeType = tradeType;
    }

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public ArrayList<String> getDisplayingSide() {
        return displayingSide;
    }

    public void setDisplayingSide(ArrayList<String> displayingSide) {
        this.displayingSide = displayingSide;
    }

    public ArrayList<String> getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ArrayList<String> executionType) {
        this.executionType = executionType;
    }

    public List<TradeDatasetBroker> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<TradeDatasetBroker> brokers) {
        this.brokers = brokers;
    }

    public List<TradeDatasetCounterparty> getCounterparties() {
        return counterparties;
    }

    public void setCounterparties(List<TradeDatasetCounterparty> counterparties) {
        this.counterparties = counterparties;
    }

    public List<TradeDatasetCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<TradeDatasetCompany> companies) {
        this.companies = companies;
    }

    public List<TradeDatasetProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<TradeDatasetProvider> providers) {
        this.providers = providers;
    }

    public List<TradeDatasetProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TradeDatasetProduct> products) {
        this.products = products;
    }
}
