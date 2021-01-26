package com.example.enigmaapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.enigmaapp.web.dataset.Batched;

@Database(entities = Batched.class, version = 1, exportSchema = false)
public abstract class BatchedDatabase extends RoomDatabase {

    private static BatchedDatabase instance;

    public abstract BatchedDao batchedDao();

    public static synchronized BatchedDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BatchedDatabase.class, "batched_database")
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
        private BatchedDao batchedDao;

        private PopulateDbAsyncTask(BatchedDatabase db) {
            batchedDao = db.batchedDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
