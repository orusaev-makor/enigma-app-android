package com.example.enigmaapp.web.dataset;

public class DatasetExecutionType {
    private String name;
    private boolean isChecked = false;

    public DatasetExecutionType(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }
}
