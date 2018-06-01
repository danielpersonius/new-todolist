package com.example.daniel.todo_list;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Entity class for items
 * Created by daniel on 12/25/17.
 */

@Entity(tableName = "item", foreignKeys = @ForeignKey(entity = CategoryEntity.class,
                                                      parentColumns = "categoryId",
                                                      childColumns = "category",
                                                      onDelete = CASCADE))
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    private int itemId;


    @ColumnInfo(name = "name")
    private String name;

    /**
     * 0 - incomplete, not started
     * 1 - complete
     * 2 - incomplete, in progress
     */
    @ColumnInfo(name = "complete")
    private int complete;

    @ColumnInfo(name = "category")
    private int category;

    @ColumnInfo(name = "isActive")
    private int isActive;

    ItemEntity(){

    }

    ItemEntity(String name, int categoryId, int isActive){
        this.name = name;
        this.category = categoryId;
        this.isActive = isActive;
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public int getItemId(){
        return itemId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setComplete(int complete){
        this.complete = complete;
    }

    public int getComplete(){
        return complete;
    }

    public void setCategory(int category){
        this.category = category;
    }

    public int getCategory(){ return category; }

    public void setIsActive(int isActive){
        this.isActive = isActive;
    }

    public int getIsActive(){
        return this.isActive;
    }

}