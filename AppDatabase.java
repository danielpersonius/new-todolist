package com.example.daniel.todo_list;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Singleton instance of db
 * Created by daniel on 12/25/17.
 */

@Database(entities = {ItemEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;

    abstract ItemDAO itemDAO();

    public static AppDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "item-database")
                    // allow queries on the main thread
                    // don't do this long term
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

    private static ItemEntity addItem(final AppDatabase db, ItemEntity item){
        db.itemDAO().insertAll(item);
        return item;
    }

    /**
     * test method
     * @param db AppDatabase object
     */
    private static void populateWithTestData(AppDatabase db){
        ItemEntity item = new ItemEntity();
        item.setName("Music");
        item.setComplete(false);
        addItem(db, item);

        item.setName("Film");
        item.setComplete(false);
        addItem(db, item);

        item.setName("CS");
        item.setComplete(false);
        addItem(db, item);

        item.setName("Psych");
        item.setComplete(false);
        addItem(db, item);

        item.setName("wr");
        item.setComplete(false);
        addItem(db, item);
    }
}
