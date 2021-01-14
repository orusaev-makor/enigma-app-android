package com.example.enigmaapp.web.trade;

import com.google.gson.annotations.SerializedName;

public class TradeItemResult {

    @SerializedName("trade_id")
    private String tradeId;
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
    @SerializedName("fx_rate")
    private String fxRate;
    private String status;
    @SerializedName("settlement_batch_id")
    private String batchId;
    @SerializedName("exec_by")
    private String execBy;
    @SerializedName("sent_at")
    private String comment;
    private String sentAt;
    @SerializedName("execution_provider")
    private String executionProvider;
    @SerializedName("execution_fee")
    private String executionFee;
    @SerializedName("settlement_date")
    private String settlementDate;

    public String getFxRate() {
        return fxRate;
    }

    public void setFxRate(String fxRate) {
        this.fxRate = fxRate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getExecBy() {
        return execBy;
    }

    public void setExecBy(String execBy) {
        this.execBy = execBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getExecutionProvider() {
        return executionProvider;
    }

    public void setExecutionProvider(String executionProvider) {
        this.executionProvider = executionProvider;
    }

    public String getExecutionFee() {
        return executionFee;
    }

    public void setExecutionFee(String executionFee) {
        this.executionFee = executionFee;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
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

    public void setPrice(String price) { this.price = price; }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
