package com.example.daniel.todo_list;

import java.util.*;
import android.arch.persistence.room.*;

/**
 * Created by daniel on 3/26/18.
 */

@Dao
public interface CategoryDAO {

    // SELECTs
    @Query("SELECT * FROM category")
    List<CategoryEntity> getAll();

    @Query("SELECT * FROM category WHERE name LIKE :name")
    CategoryEntity findByName(String name);

    @Query("SELECT COUNT(*) FROM category")
    int countCategories();

    // for a list view of all incomplete categories
    @Query("SELECT * FROM category WHERE complete = 0")
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


}
