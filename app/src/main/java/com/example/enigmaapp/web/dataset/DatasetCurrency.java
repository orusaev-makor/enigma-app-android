package com.example.enigmaapp.web.dataset;

public class DatasetCurrency {
    private String name;
    private boolean isChecked = false;

    public DatasetCurrency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }
}
