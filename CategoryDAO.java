package com.example.daniel.todo_list;

import java.util.*;
import android.arch.persistence.room.*;

/**
 * Created by daniel on 3/26/18.
 */

@Dao
public interface CategoryDAO {

    // SELECTs
    // get all as entities
    @Query("SELECT * FROM category")
    List<CategoryEntity> getAll();

    // get only active
    @Query("SELECT * FROM category WHERE isActive=1")
    List<CategoryEntity> getAllActive();

    // get only inactive
    @Query("SELECT * FROM category WHERE isActive=0")
    List<CategoryEntity> getAllInactive();

    // just get active names
    @Query("SELECT name FROM category")
    List<String> getAllCategoryNames();

    @Query("SELECT * FROM category WHERE name LIKE :name")
    CategoryEntity findByName(String name);

    // get id by name
    @Query("SELECT categoryId FROM category WHERE name=:name")
    int getCategoryId(String name);

    @Query("SELECT COUNT(*) FROM category")
    int countCategories();

    // for a list view of all incomplete activeCategories
    @Query("SELECT * FROM category WHERE isComplete = 0")
    List<CategoryEntity> loadAllIncompleteCategories();

    // INSERTs
    @Insert
    void insertAll(CategoryEntity... categories);

    // DELETEs
    @Delete
    void delete(CategoryEntity category);

    @Delete
    void deleteAll(List<CategoryEntity> categories);

    // UPDATEs
    @Update
    void updateItems(CategoryEntity... categories);

    @Update
    void updateItem(CategoryEntity category);

}