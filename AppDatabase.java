package com.example.daniel.todo_list;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Singleton instance of db
 * Created by daniel on 12/25/17.
 */

@Database(entities = {ItemEntity.class, CategoryEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;

    // DAOs
    public abstract ItemDAO itemDAO();
    public abstract CategoryDAO categoryDAO();

    public static AppDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            //INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "item").allowMainThreadQueries().build();
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "item-database")
                    // allow queries on the main thread
                    // don't do this long term
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
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

    private static void addCategoryItem(final AppDatabase db, CategoryEntity category){
        db.categoryDAO().insertAll(category);
    }

    /**
     * test method
     * @param db AppDatabase object
     */
    public static void populateCategoryWithTestData(AppDatabase db){
        try {
            // clear table
            db.categoryDAO().deleteAll(db.categoryDAO().getAll());

            CategoryEntity cat1 = new CategoryEntity();
            cat1.setName("music");
            cat1.setComplete(0);

            CategoryEntity cat2 = new CategoryEntity();
            cat2.setName("cs");
            cat2.setComplete(0);

            CategoryEntity cat3 = new CategoryEntity();
            cat3.setName("self");
            cat3.setComplete(0);

            addCategoryItem(db, cat1);
            addCategoryItem(db, cat2);
            addCategoryItem(db, cat3);
        }
        catch(Exception e) {
            Log.d("CAT DEL", e.getMessage());
        }
    }

    /**
     *
     * @param db - database instance
     */
    public static void populateItemWithTestData(AppDatabase db){
        try{
            // clear table
            db.itemDAO().deleteAll(db.itemDAO().getAll());

            // manually insert a few items into each category
            ItemEntity item1 = new ItemEntity();
            item1.setCategory(db.categoryDAO().findByName("music").getCategoryId());
            item1.setName("flashcards");
            item1.setComplete(0);

            ItemEntity item2 = new ItemEntity();
            item2.setCategory(db.categoryDAO().findByName("music").getCategoryId());
            item2.setName("mix");
            item2.setComplete(1);

            ItemEntity item3 = new ItemEntity();
            item3.setCategory(db.categoryDAO().findByName("cs").getCategoryId());
            item3.setName("java guide");
            item3.setComplete(1);

            ItemEntity item4 = new ItemEntity();
            item4.setCategory(db.categoryDAO().findByName("self").getCategoryId());
            item4.setName("wr");
            item4.setComplete(0);

            // add items
            addItem(db, item1);
            addItem(db, item2);
            addItem(db, item3);
            addItem(db, item4);
        }
        catch(Exception e) {
            Log.d("BAD POP", "bad populate: " + e.getMessage());
        }
    }
}
