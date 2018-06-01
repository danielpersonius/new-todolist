package com.example.daniel.todo_list;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.*;

/**
 * initialize database
 * Created by daniel on 12/25/17.
 */

public class DatabaseInitializer {
    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static ItemEntity addItem(final AppDatabase db, ItemEntity item) {
        db.itemDAO().insertAll(item);
        return item;
    }

    private static void populateWithTestData(AppDatabase db) {
        ItemEntity item = new ItemEntity();
        item.setName("France");
        item.setComplete(0);
        addItem(db, item);

        List<ItemEntity> itemList = db.itemDAO().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + itemList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }
    }
}