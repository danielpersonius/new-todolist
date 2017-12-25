package com.example.daniel.todo_list;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

/**
 * Data access object class for item
 * Created by daniel on 12/25/17.
 */

public interface ItemDAO {
    @Query("SELECT * FROM item")
    ArrayList<ItemEntity> getAll();

    @Query("SELECT * FROM item WHERE name LIKE :name")
    ItemEntity findByName(String name);

    @Query("SELECT COUNT(*) FROM item")
    int countItems();

    @Insert
    void insertAll(ItemEntity... items);

    @Delete
    void delete(ItemEntity item);
}
