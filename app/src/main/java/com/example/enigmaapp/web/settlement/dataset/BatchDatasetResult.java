package com.example.enigmaapp.web.settlement.dataset;

import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;

import java.util.ArrayList;
import java.util.List;

public class BatchDatasetResult {
    ArrayList<String> status;
    List<TradeDatasetCounterparty> counterparty;
    List<TradeDatasetProduct> product;

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public List<TradeDatasetCounterparty> getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(List<TradeDatasetCounterparty> counterparty) {
        this.counterparty = counterparty;
    }

    public List<TradeDatasetProduct> getProducts() {
        return product;
    }

    public void setProducts(List<TradeDatasetProduct> products) {
        this.product = products;
    }
}
