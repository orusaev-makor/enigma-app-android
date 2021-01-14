package com.example.enigmaapp.web.settlement;

import com.google.gson.annotations.SerializedName;

public class BatchItemResult {

    // Unitary:
    @SerializedName("settlement_id")
    private String settlementId;
    private String type;
    private String amount;
    @SerializedName("settled_amount")
    private String settledAmount;
    private String currency;
    @SerializedName("rejection_reason")
    private String rejectionReason;
    private String comment;
    @SerializedName("enigma_account")
    private String enigmaAccount;
    @SerializedName("counterparty_account")
    private String counterpartyAccount;
    private String side;

    // Batch:
    @SerializedName("settlement_batch_id")
    private String batchId;
    @SerializedName("sent_at")
    private String sentAt;
    private String counterparty;
    private String product;
    private String status;

    public String getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEnigmaAccount() {
        return enigmaAccount;
    }

    public void setEnigmaAccount(String enigmaAccount) {
        this.enigmaAccount = enigmaAccount;
    }

    public String getCounterpartyAccount() {
        return counterpartyAccount;
    }

    public void setCounterpartyAccount(String counterpartyAccount) {
        this.counterpartyAccount = counterpartyAccount;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
