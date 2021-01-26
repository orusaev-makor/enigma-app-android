package com.example.enigmaapp.web.dataset;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "execution_type_table")
public class ExecutionType {

    @PrimaryKey
    @NonNull
    private String name;
    private boolean isChecked = false;

    public ExecutionType(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }
}
