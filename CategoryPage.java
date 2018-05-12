package com.example.daniel.todo_list;

import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by daniel on 12/24/17.
 */

public class CategoryPage extends AppCompatActivity{
    // just the buttons being used
    ArrayList<Button> buttonsInUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page);

        // get category name
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");

        // db instance
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "item-database").allowMainThreadQueries().build();

        int categoryId = db.categoryDAO().findByName(categoryName).getCategoryId();

        // get items in category
        List<ItemEntity> items;
        try {
            items = db.itemDAO().getItemsInCategory(categoryId);

            // initialize buttons in use
            buttonsInUse = new ArrayList<>();

            //
            LinearLayout buttonContainer = findViewById(R.id.itemContainer);
            for(ItemEntity item : items){
                Button b = new Button(this);
                b.setText(item.getName());
                b.setTextSize(30);
                b.setTextColor(-1);

                // set color
                int isComplete = item.getComplete();
                if(isComplete == 1){
                    b.setBackgroundColor(getResources().getColor(R.color.complete));
                }
                else {
                    b.setBackgroundColor(getResources().getColor(R.color.incomplete));
                }

                // add to layout
                buttonContainer.addView(b);
                buttonsInUse.add(b);
            }


            for(int i = 0; i < buttonsInUse.size(); i++){
                final int j = i;
                buttonsInUse.get(j).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int colorId = ((ColorDrawable) buttonsInUse.get(j).getBackground()).getColor();
                        if(colorId == getResources().getColor(R.color.incomplete)){
                            // change color
                            buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.complete));
                            // change completion status
                            db.itemDAO().updateItemCompletion(db.itemDAO().findByName(buttonsInUse.get(j).getText().toString()).getItemId(), 1);
                            // log it
                            //Log.d("ITEM COMPLETE", "item complete: " + db.itemDAO().findByName(buttonsInUse.get(j).getText().toString()).getComplete());
                        }
                        else {
                            buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.incomplete));
                            // change completion status
                            db.itemDAO().updateItemCompletion(db.itemDAO().findByName(buttonsInUse.get(j).getText().toString()).getItemId(), 0);
                            // log it
                            //Log.d("ITEM INCOMPLETE", "item incomplete: " + db.itemDAO().findByName(buttonsInUse.get(j).getText().toString()).getComplete());
                        }
                    }
                });
            }
        }
        catch(Exception e){
            Log.d("BAD GET", "bad get: " + e.getMessage());
        }
    }
}
