package com.example.enigmaapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.enigmaapp.web.dataset.Currency;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CurrencyDao {

    @Insert
    void insert(Currency currency);

    @Update
    void update(Currency currency);

    @Delete
    void delete(Currency currency);

    @Query("DELETE FROM currency_table")
    void deleteAllCurrencies();

    @Query("SELECT * FROM currency_table")
    LiveData<List<Currency>> getAllCurrencies();
}
