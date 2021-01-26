package com.example.enigmaapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.enigmaapp.web.dataset.ExecutionType;

import java.util.List;

@Dao
public interface ExecutionTypeDao {

    @Insert
    void insert(ExecutionType executionType);

    @Update
    void update(ExecutionType executionType);

    @Delete
    void delete(ExecutionType executionType);

    @Query("DELETE FROM execution_type_table")
    void deleteAllExecutionTypes();

    @Query("SELECT * FROM execution_type_table")
    LiveData<List<ExecutionType>> getAllExecutionTypes();
}
