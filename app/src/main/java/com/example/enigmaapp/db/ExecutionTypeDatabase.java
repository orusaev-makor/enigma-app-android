package com.example.enigmaapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.enigmaapp.web.dataset.ExecutionType;


@Database(entities = ExecutionType.class, version = 1, exportSchema = false)
public abstract class ExecutionTypeDatabase extends RoomDatabase {

    private static ExecutionTypeDatabase instance;

    public abstract ExecutionTypeDao executionTypeDao();

    public static synchronized ExecutionTypeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExecutionTypeDatabase.class, "executionType_database")
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
        private ExecutionTypeDao executionTypeDao;

        private PopulateDbAsyncTask(ExecutionTypeDatabase db) {
            executionTypeDao = db.executionTypeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
