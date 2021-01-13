package com.example.enigmaapp.web.trade.dataset;

import com.example.enigmaapp.web.dataset.DatasetBroker;
import com.example.enigmaapp.web.dataset.DatasetCompany;
import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetProduct;
import com.example.enigmaapp.web.dataset.DatasetProvider;
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
    List<DatasetBroker> broker;
    List<DatasetCounterparty> counterparty;
    List<DatasetCompany> companies;
    List<DatasetProvider> provider;
    List<DatasetProduct> product;

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

    public List<DatasetBroker> getBroker() {
        return broker;
    }

    public void setBroker(List<DatasetBroker> broker) {
        this.broker = broker;
    }

    public List<DatasetCounterparty> getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(List<DatasetCounterparty> counterparty) {
        this.counterparty = counterparty;
    }

    public List<DatasetCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<DatasetCompany> companies) {
        this.companies = companies;
    }

    public List<DatasetProvider> getProvider() {
        return provider;
    }

    public void setProvider(List<DatasetProvider> provider) {
        this.provider = provider;
    }

    public List<DatasetProduct> getProduct() {
        return product;
    }

    public void setProduct(List<DatasetProduct> product) {
        this.product = product;
    }
}
