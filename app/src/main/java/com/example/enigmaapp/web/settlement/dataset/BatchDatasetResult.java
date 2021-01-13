package com.example.enigmaapp.web.settlement.dataset;

import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetProduct;

import java.util.ArrayList;
import java.util.List;

public class BatchDatasetResult {
    ArrayList<String> status;
    List<DatasetCounterparty> counterparty;
    List<DatasetProduct> product;

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public List<DatasetCounterparty> getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(List<DatasetCounterparty> counterparty) {
        this.counterparty = counterparty;
    }

    public List<DatasetProduct> getProducts() {
        return product;
    }

    public void setProducts(List<DatasetProduct> products) {
        this.product = products;
    }
}
