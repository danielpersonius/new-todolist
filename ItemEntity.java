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

    @ColumnInfo(name = "complete")
    private int complete;

    @ColumnInfo(name = "category")
    private int category;

    ItemEntity(){

    }

    public ItemEntity(String name, int complete){
        this.name = name;
        this.complete = complete;
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

}
