package com.example.daniel.todo_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        final LinearLayout buttonContainer = findViewById(R.id.categoryContainer);

        // db
        final AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

        try{
            // retrieve live categories
            categories = db.categoryDAO().getAllActive();
            // create and add buttons, setting colors according to completion statuses
            // cannot use foreach loop, since it does not change the base object
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
                setButtonColorAndAlpha(b, categories.get(i), db);

                // click listeners
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCategoryPage(b.getText().toString());
                    }
                });

                b.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {
                        // get item by current name before name change
                        final CategoryEntity cat = db.categoryDAO().findByName(b.getText().toString());
                        final EditText input = new EditText(Overview.this);
                        // set up dialog box
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Overview.this);
                        alertDialog.setView(input);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                b.setText(input.getText().toString());
                                // change item name
                                cat.setName(b.getText().toString());

                                // change in db
                                try{
                                    db.categoryDAO().updateItems(cat);
                                }
                                catch(Exception e){
                                    Toast.makeText(Overview.this, "category update failed", Toast.LENGTH_SHORT).show();
                                    Log.d("UPDATE CATEGORY", "category update failed: " + e.getMessage());
                                }
                            }
                        });
                        // cancel button
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int i) {
                                d.cancel();
                            }
                        });
                        alertDialog.show();

                        return true;
                    }
                });

                // add to layout
                buttonContainer.addView(b);
                buttonsInUse.add(b);
            }

            // 'add' button
            Button addButton = findViewById(R.id.addCategoryButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // insert
                    addItem(buttonContainer, db);

                }
            });
            buttonContainer.addView(addButton);
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
            setButtonColorAndAlpha(buttonsInUse.get(i), db.categoryDAO().findByName(buttonsInUse.get(i).getText().toString()), db);
        }

    }

    /**
     *
     * @param category - the category
     * @param db - database handle
     */
    public boolean isCategoryComplete(CategoryEntity category, AppDatabase db){
        categoryItems = db.itemDAO().getItemsInCategory(category.getCategoryId());
        // if empty category should show incomplete
        //boolean isComplete = !isCategoryEmpty(cat, db);
        boolean isComplete = true;

        for (ItemEntity item : categoryItems) {
            if (item.getComplete() == 0) {
                isComplete = false;
                break;
            }
        }

        if (isComplete) {
            // may not want to change in the list and the db simultaneously
            // may want to either: combine the process or just read from db, eliminating the list
            category.setComplete(1);
            db.categoryDAO().findByName(category.getName()).setComplete(1);
            return true;
        }

        category.setComplete(0);
        return false;
    }

    /**
     *
     * @param category - the category
     * @param db - database handle
     */
    public boolean isCategoryEmpty(CategoryEntity category, AppDatabase db){
        categoryItems = db.itemDAO().getItemsInCategory(category.getCategoryId());
        return categoryItems.size() == 0;
    }

    public void setButtonColorAndAlpha(Button b, CategoryEntity category, AppDatabase db){
        // set color
        if(isCategoryComplete(category, db)){
            b.setBackgroundColor(getResources().getColor(R.color.complete));
        }
        else {
            b.setBackgroundColor(getResources().getColor(R.color.incomplete));
        }
        // lower alpha if empty category, else default to full 255
        if(isCategoryEmpty(category, db)) {
            b.getBackground().setAlpha(150);
        }
    }

    public void addItem(final LinearLayout buttonContainer, final AppDatabase db){
        // dialog box
        // get custom layout with EditText and Spinner
        //LayoutInflater inflater = getLayoutInflater();
        //View dialogLayout = inflater.inflate(R.layout.add_item, null);

        //final EditText input = dialogLayout.findViewById(R.id.new_item_name);
        final EditText input = new EditText(Overview.this);
        AlertDialog.Builder addDialog = new AlertDialog.Builder(Overview.this);
        addDialog.setTitle("add category")
                .setView(input)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // create item
                        final CategoryEntity newCategory = new CategoryEntity(input.getText().toString(), 1);
                        // insert
                        try {
                            db.categoryDAO().insertAll(newCategory);
                            // add new button to layout
                            Button newCategoryButton = new Button(Overview.this);
                            newCategoryButton.setBackgroundColor(getResources().getColor(R.color.incomplete));
                            newCategoryButton.setText(newCategory.getName());
                            newCategoryButton.setHeight(250);
                            newCategoryButton.setTextSize(30);
                            newCategoryButton.setTextColor(-1);

                            buttonContainer.addView(newCategoryButton);

                        } catch (Exception e) {
                            // user-friendly message
                            Toast.makeText(Overview.this, "could not add category", Toast.LENGTH_SHORT).show();
                            Log.d("INSERT ITEM", "category insert failed: " + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    // cancel button
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        d.cancel();
                    }
                });
        addDialog.show();
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
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
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
