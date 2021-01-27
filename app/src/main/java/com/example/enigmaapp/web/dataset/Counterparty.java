package com.example.enigmaapp.web.dataset;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "counterparty_table")
public class Counterparty {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;

    private boolean isChecked = false;

    public Counterparty(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }

    public String getId() {
        return id;
    }

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
