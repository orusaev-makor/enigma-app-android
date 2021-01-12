package com.example.enigmaapp.web.settlement.dataset;

import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UnitaryDatasetResult {
    ArrayList<String> side;

    ArrayList<String> type;

    ArrayList<String> status;

    ArrayList<String> currency;

    @SerializedName("crypto_currency")
    ArrayList<String> cryptoCurrency;

    List<TradeDatasetCounterparty> counterparty;

    List<TradeDatasetProduct> product;

    public ArrayList<String> getSide() {
        return side;
    }

    public void setSide(ArrayList<String> side) {
        this.side = side;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public ArrayList<String> getCurrency() {
        return currency;
    }

    public void setCurrency(ArrayList<String> currency) {
        this.currency = currency;
    }

    public ArrayList<String> getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(ArrayList<String> cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public List<TradeDatasetCounterparty> getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(List<TradeDatasetCounterparty> counterparty) {
        this.counterparty = counterparty;
    }

    public List<TradeDatasetProduct> getProduct() {
        return product;
    }

    public void setProduct(List<TradeDatasetProduct> product) {
        this.product = product;
    }
}
