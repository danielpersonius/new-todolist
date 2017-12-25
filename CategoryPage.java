package com.example.daniel.todo_list;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by daniel on 12/24/17.
 */

public class CategoryPage extends AppCompatActivity{
    // create the buttons - limit to 10 at one time
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;

    // just the buttons being used
    ArrayList<Button> buttonsInUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page);

        // link buttons to xml buttons
        b1  = findViewById(R.id.item_1);
        b2  = findViewById(R.id.item_2);
        b3  = findViewById(R.id.item_3);
        b4  = findViewById(R.id.item_4);
        b5  = findViewById(R.id.item_5);
        b6  = findViewById(R.id.item_6);
        b7  = findViewById(R.id.item_7);

        // initialize buttons in use
        buttonsInUse = new ArrayList<Button>();

        // fill by default
        buttonsInUse.add(b1);
        buttonsInUse.add(b2);
        buttonsInUse.add(b3);
        buttonsInUse.add(b4);
        buttonsInUse.add(b5);
        buttonsInUse.add(b6);
        buttonsInUse.add(b7);

        for(int i = 0; i < buttonsInUse.size(); i++){
            final int j = i;
            buttonsInUse.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int colorId = ((ColorDrawable) buttonsInUse.get(j).getBackground()).getColor();
                    if(colorId == getResources().getColor(R.color.incomplete)){
                        buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.complete));

                    }
                    else {
                        buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.incomplete));
                    }
                }
            });
        }
    }
}
