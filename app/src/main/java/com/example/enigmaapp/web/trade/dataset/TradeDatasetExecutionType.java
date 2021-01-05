package com.example.enigmaapp.web.trade.dataset;

public class TradeDatasetExecutionType {
    private String name;
    private boolean isChecked = false;

    public TradeDatasetExecutionType(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }
}
