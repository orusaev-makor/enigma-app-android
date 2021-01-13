package com.example.enigmaapp.web.dataset;

public class DatasetProduct {
    private String id, name;
    private boolean isChecked = false;

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
