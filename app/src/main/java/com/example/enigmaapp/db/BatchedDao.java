package com.example.enigmaapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.enigmaapp.web.dataset.Batched;

import java.util.List;

@Dao
public interface BatchedDao {

    @Insert
    void insert(Batched batched);

    @Update
    void update(Batched batched);

    @Delete
    void delete(Batched batched);

    @Query("DELETE FROM batched_table")
    void deleteAllBatched();

    @Query("SELECT * FROM batched_table")
    LiveData<List<Batched>> getAllBatched();
}
