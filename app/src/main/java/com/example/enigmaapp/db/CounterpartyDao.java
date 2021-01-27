package com.example.enigmaapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.enigmaapp.web.dataset.Counterparty;

import java.util.List;

@Dao
public interface CounterpartyDao {

    @Insert
    void insert(Counterparty counterparty);

    @Update
    void update(Counterparty counterparty);

    @Delete
    void delete(Counterparty counterparty);

    @Query("DELETE FROM counterparty_table")
    void deleteAllCounterparties();

    @Query("SELECT * FROM counterparty_table")
    LiveData<List<Counterparty>> getAllCounterparties();
}
