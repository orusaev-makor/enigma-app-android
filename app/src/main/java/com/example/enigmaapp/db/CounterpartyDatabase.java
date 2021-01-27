package com.example.enigmaapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.enigmaapp.web.dataset.Counterparty;

@Database(entities = Counterparty.class, version = 1, exportSchema = false)
public abstract class CounterpartyDatabase extends RoomDatabase {

    private static CounterpartyDatabase instance;

    public abstract CounterpartyDao counterpartyDao();

    public static synchronized CounterpartyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CounterpartyDatabase.class, "counterparty_database")
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
        private CounterpartyDao counterpartyDao;

        private PopulateDbAsyncTask(CounterpartyDatabase db) {
            counterpartyDao = db.counterpartyDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
