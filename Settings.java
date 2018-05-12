package com.example.daniel.todo_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.*;

/**
 * Created by daniel on 12/25/17.
 */

public class Settings extends AppCompatActivity {

    Button                 saveBtn;
    //String               name1, name2, name3, name4, name5, name6, name7, name8, name9, name10;
    //EditText             et1, et2, et3, et4, et5, et6, et7, et8, et9, et10;
    List<ItemEntity>  items;
    ArrayList<EditText>    textBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        saveBtn   = findViewById(R.id.save_button);
        items     = (AppDatabase.getAppDatabase(this)).itemDAO().getAll();
        textBoxes = new ArrayList<>();
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_1));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_2));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_3));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_4));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_5));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_6));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_7));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_8));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_9));
        textBoxes.add((EditText) findViewById(R.id.edit_cat_name_10));


        for(int i = 0; i < items.size(); i++){
            // set text from db
            textBoxes.get(i).setText(items.get(i).getName().trim().toLowerCase());
        }
    }

    public void saveConfig(){
        for(int i = 0; i < textBoxes.size(); i++){
            // update only if name changed
            if(!textBoxes.get(i)
                    .getText()
                    .toString()
                    .trim()
                    .toLowerCase()
                    .equals(items.get(i)
                            .getName()
                            .trim()
                            .toLowerCase())){
                AppDatabase.getAppDatabase(this).itemDAO()
                        .updateItems(items.get(i));
                        //.updateItems(items[i]
                        //        .getName()
                        //        .trim()
                        //        .toLowerCase(), textBoxes.get(i)
                        //        .getText()
                        //        .toString()
                        //        .toLowerCase());
            }
        }
    }
}
