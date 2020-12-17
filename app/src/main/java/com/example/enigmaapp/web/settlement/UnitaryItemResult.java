package com.example.enigmaapp.web.settlement;

import com.google.gson.annotations.SerializedName;

public class UnitaryItemResult {

    @SerializedName("settlement_id")
    private int unitaryId;
    private String type;
    private String amount;
    @SerializedName("settled_amount")
    private String settledAmount;
    private String currency;
    @SerializedName("sent_at")
    private String sentAt;
    @SerializedName("rejection_reason")
    private String rejectionReason;
    private String counterparty;
    private String comment;
    @SerializedName("enigma_account")
    private String enigmaAccount;
    @SerializedName("counterparty_account")
    private String counterpartyAccount;
    @SerializedName("settlement_batch_id")
    private int settlementBatchId;
    private String status;
    private String side;

    public int getUnitaryId() {
        return unitaryId;
    }

    public void setUnitaryId(int unitaryId) {
        this.unitaryId = unitaryId;
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

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
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

    public int getSettlementBatchId() {
        return settlementBatchId;
    }

    public void setSettlementBatchId(int settlementBatchId) {
        this.settlementBatchId = settlementBatchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
