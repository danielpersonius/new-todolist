package com.example.daniel.todo_list;

import java.util.*;
import android.arch.persistence.room.*;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;

import java.util.ArrayList;

/**
 * Data access object class for item
 * Created by daniel on 12/25/17.
 * todo: add error handling maybe?
 */

@Dao
public interface ItemDAO {
    // SELECTs
    @Query("SELECT * FROM item")
    List<ItemEntity> getAll();

    @Query("SELECT * FROM item WHERE name LIKE :name")
    ItemEntity findByName(String name);

    @Query("SELECT COUNT(*) FROM item")
    int countItems();

    // all items under a category
    @Query("SELECT * FROM item WHERE category=:categoryId")
    List<ItemEntity> getItemsInCategory(int categoryId);

    // all active items under a category
    @Query("SELECT * FROM item WHERE category=:categoryId AND isActive=1")
    List<ItemEntity> getActiveItemsInCategory(int categoryId);

    // all inactive items under a category
    @Query("SELECT * FROM item WHERE category=:categoryId AND isActive=0")
    List<ItemEntity> getInactiveItemsInCategory(int categoryId);

    // for a list view of all incomplete items
    @Query("SELECT * FROM item WHERE complete = 0")
    List<ItemEntity> loadAllIncompleteItems();

    // INSERTs
    @Insert
    void insertAll(ItemEntity... items);

    // DELETEs
    @Delete
    void delete(ItemEntity item);

    @Delete
    void deleteAll(List<ItemEntity> items);

    // UPDATEs
    @Update
    void updateItems(ItemEntity... items);

    @Update
    void updateItems(List<ItemEntity> items);

    @Update
    void updateItem(ItemEntity item);

    @Query("UPDATE item SET complete=:isComplete WHERE itemId=:itemId")
    void updateItemCompletion(int itemId, int isComplete);

}
