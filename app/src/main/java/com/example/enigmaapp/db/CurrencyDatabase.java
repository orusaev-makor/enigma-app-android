package com.example.enigmaapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.Currency;

@Database(entities = Currency.class, version = 1, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    public abstract CurrencyDao currencyDao();

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CurrencyDatabase.class, "currency_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CurrencyDao currencyDao;

        private PopulateDbAsyncTask(CurrencyDatabase db) {
            currencyDao = db.currencyDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
