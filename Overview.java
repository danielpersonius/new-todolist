package com.example.daniel.todo_list;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.*;

/**
 * Created by daniel on 12/23/17
 */

public class Overview extends AppCompatActivity{
    // just the buttons being used
    List<Button> buttonsInUse;
    List<CategoryEntity> categories;
    List<ItemEntity> categoryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        // initialize list of buttons in use
        buttonsInUse = new ArrayList<>();

        // dynamically add buttons
        LinearLayout buttonContainer = findViewById(R.id.categoryContainer);

        // db
        //AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "item-database").allowMainThreadQueries().build();
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

        try{
            // retrieve live categories
            categories = db.categoryDAO().getAll();

            // create and add buttons, setting colors according to completion statuses

            // cannot use foreach loop, since it does not change the base object
            //for(CategoryEntity cat : categories){
            for(int i = 0; i < categories.size(); i++){
                // add button
                final Button b = new Button(this);

                // set text
                b.setText(categories.get(i).getName());

                // set button attributes
                b.setHeight(350);
                b.setTextColor(-1);
                b.setTextSize(30);

                // set color
                setButtonColor(b, categories.get(i), db);

                // add click listener
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCategoryPage(b.getText().toString());
                    }
                });

                // add to layout
                buttonContainer.addView(b);
                buttonsInUse.add(b);
            }
        }
        catch(Exception e){
            Log.d("BAD CAT", "BAD CAT " + e.getMessage());
        }

    }

    /**
     * this is called when the activity resumes, e.g. on back button press, since onCreate is not called then
     * it is called so that button colors are updated
     */
    @Override
    public void onResume(){
        super.onResume();
        // db
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

        // again, can't use foreach
        for(int i = 0; i < buttonsInUse.size(); i++){
            setButtonColor(buttonsInUse.get(i), db.categoryDAO().findByName(buttonsInUse.get(i).getText().toString()), db);
        }

    }

    /**
     *
     * @param cat - the category
     * @param db - database handle
     */
    public boolean isCategoryComplete(CategoryEntity cat, AppDatabase db){
        //for(int i = 0; i < categories.size(); i++) {
        boolean isComplete = true;
        //categoryItems = db.itemDAO().getItemsInCategory(categories.get(i).getCategoryId());
        categoryItems = db.itemDAO().getItemsInCategory(cat.getCategoryId());
        for (ItemEntity item : categoryItems) {
            //Log.d("ITEM COMPLETE", item.getName() + ": " + item.getComplete());
            if (item.getComplete() == 0) {
                isComplete = false;
                break;
            }
        }
        Log.d("CAT COMPLETION", cat.getName() + " is complete: " + isComplete);
        if (isComplete) {
            // may not want to change in the list and the db simultaneously
            // may want to either: combine the process or just read from db, eliminating the list
            cat.setComplete(1);
            db.categoryDAO().findByName(cat.getName()).setComplete(1);
            return true;
        }

        cat.setComplete(0);
        return false;
    }

    public void setButtonColor(Button b, CategoryEntity category, AppDatabase db){
        if(isCategoryComplete(category, db)){
            b.setBackgroundColor(getResources().getColor(R.color.complete));
        }
        else {
            b.setBackgroundColor(getResources().getColor(R.color.incomplete));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * view the items in a category
     * @param catName - the name of the category
     */
    public void showCategoryPage(String catName){
        Intent intent = new Intent(this, CategoryPage.class);
        intent.putExtra("CATEGORY_NAME", catName);
        startActivity(intent);
    }
}
