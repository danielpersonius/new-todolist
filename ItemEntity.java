package com.example.daniel.todo_list;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Entity class for items
 * Created by daniel on 12/25/17.
 */

@Entity(tableName = "item")
public class ItemEntity {
    /*
    private String ID, name, complete;

    public void setID(String ID){
        this.ID = ID;
    }

    public String getID(){
        return ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setComplete(String complete){
        this.complete = complete;
    }

    public String getComplete(){
        return complete;
    }
    */

    @PrimaryKey(autoGenerate = true)
    private int item_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "complete")
    private boolean complete;

    public void setItemId(int itemId){
        item_id = itemId;
    }

    public int getItemId(){
        return item_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setComplete(boolean complete){
        this.complete = complete;
    }

    public boolean getComplete(){
        return complete;
    }
}
