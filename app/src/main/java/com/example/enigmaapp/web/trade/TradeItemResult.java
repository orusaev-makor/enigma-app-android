package com.example.enigmaapp.web.trade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TradeItemResult {

    @SerializedName("trade_id")
    private int tradeItemId;
    private String date;
    private String side;
    private String counterparty;
    private String type;
    @SerializedName("execution_type")
    private String executionType;
    private String product;
    private String quantity;
    private String price;
    @SerializedName("provider_price")
    private String providerPrice;
    @SerializedName("sales_pl")
    private String salesPl;
    private String nominal;
    private String broker;
    private String status;

    public int getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(int tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProviderPrice() {
        return providerPrice;
    }

    public void setProviderPrice(String providerPrice) {
        this.providerPrice = providerPrice;
    }

    public String getSalesPl() {
        return salesPl;
    }

    public void setSalesPl(String salesPl) {
        this.salesPl = salesPl;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
