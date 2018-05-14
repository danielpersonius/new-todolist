package com.example.daniel.todo_list;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "complete")
    private int complete;

    @ColumnInfo(name = "isActive")
    private int isActive;

    CategoryEntity(){

    }

    CategoryEntity(String name, int isActive){
        this.name = name;
        this.isActive = isActive;
    }

    public void setCategoryId(int itemId){
        this.categoryId = itemId;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIsActive(int isActive){ this.isActive = isActive; }

    public String getName(){
        return name;
    }

    public void setComplete(int complete){
        this.complete = complete;
    }

    public int getComplete(){
        return complete;
    }

    public int getIsActive() { return isActive; }

}
