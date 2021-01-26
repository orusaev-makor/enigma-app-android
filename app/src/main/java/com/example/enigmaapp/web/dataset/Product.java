package com.example.enigmaapp.web.dataset;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;

    private boolean isChecked = false;

//    public Product(@NonNull String id, String name, boolean isChecked) {
    public Product(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
//        this.isChecked = isChecked;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }

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
