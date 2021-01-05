package com.example.enigmaapp.web.trade.dataset;

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
    List<TradeDatasetBroker> broker;
    List<TradeDatasetCounterparty> counterparty;
    List<TradeDatasetCompany> companies;
    List<TradeDatasetProvider> provider;
    List<TradeDatasetProduct> product;
//    @SerializedName("execution_type")
//    List<TradeDatasetExecutionType> executionType;

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

    public List<String> getStatus() {
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

    public List<String> getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ArrayList<String> executionType) {
        this.executionType = executionType;
    }

    public List<TradeDatasetBroker> getBroker() {
        return broker;
    }

    public void setBroker(List<TradeDatasetBroker> broker) {
        this.broker = broker;
    }

    public List<TradeDatasetCounterparty> getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(List<TradeDatasetCounterparty> counterparty) {
        this.counterparty = counterparty;
    }

    public List<TradeDatasetCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<TradeDatasetCompany> companies) {
        this.companies = companies;
    }

    public List<TradeDatasetProvider> getProvider() {
        return provider;
    }

    public void setProvider(List<TradeDatasetProvider> provider) {
        this.provider = provider;
    }

    public List<TradeDatasetProduct> getProduct() {
        return product;
    }

    public void setProduct(List<TradeDatasetProduct> product) {
        this.product = product;
    }
}
