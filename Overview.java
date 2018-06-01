package com.example.daniel.todo_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

/**
 * landing page to show all categories
 * extends Page, which extends AppCompatActivity
 */

public class Overview extends Page {
    //
    final int DEACTIVATE_ID = 1;
    //
    final int ACTIVATE_ID = 2;
    // just the buttons being used
    List<Button> buttonsInUse;
    List<CategoryEntity> activeCategories;
    List<CategoryEntity> inactiveCategories;
    List<CategoryEntity> allCategories;
    List<ItemEntity> categoryItems;
    // ref to button that is long pressed
    protected View pressed_button;
    // database handle
    protected AppDatabase db;
    //
    protected LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set action bar text
        setTitle("Categories");
        setContentView(R.layout.overview);

        // initialize list of buttons in use
        buttonsInUse = new ArrayList<>();

        // dynamically add buttons
        buttonContainer = findViewById(R.id.categoryContainer);

        // db
        db = AppDatabase.getAppDatabase(getApplicationContext());

        try{
            // active categories
            activeCategories = db.categoryDAO().getAllActive();
            // inactive categories
            inactiveCategories = db.categoryDAO().getAllInactive();
            // all categories together
            allCategories = activeCategories;
            allCategories.addAll(inactiveCategories);

            // create and add buttons, setting colors according to completion statuses
            // cannot use foreach loop, since it does not change the base object
            for(int i = 0; i < allCategories.size(); i++){
                // add button
                final Button b = new Button(this);

                // set text
                b.setText(allCategories.get(i).getName());

                // set button
                setButtonFeatures(allCategories.get(i), b);

                // set color
                setButtonColor(allCategories.get(i), b);

                // click listeners
                addButtonListeners(b, buttonContainer, db);

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
                    addCategory(buttonContainer);
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
        db = AppDatabase.getAppDatabase(getApplicationContext());

        // again, can't use foreach
        for(int i = 0; i < buttonsInUse.size(); i++){
            setButtonColor(db.categoryDAO().findByName(buttonsInUse.get(i).getText().toString()), buttonsInUse.get(i));
        }
    }

    /**
     * set text, height, text size, text color, alpha, and background color of item buttons
     * @param category - corresponding ItemEntity
     * @param b - current button
     */
    public void setButtonFeatures(CategoryEntity category, Button b){
        b.setText(category.getName());
        b.setHeight(350);
        b.setTextSize(30);
        b.setTextColor(-1);
        setButtonColor(category, b);
        // decrease alpha if item is inactive
        if(category.getIsActive() != 1){
            b.setAlpha((float)0.3);
        }
    }

    /**
     *
     * @param category - corresponding category
     * @param b - button to set color of
     */
    public void setButtonColor(CategoryEntity category, Button b){
        // set color
        if(isCategoryComplete(category)){
            b.setBackgroundColor(getResources().getColor(R.color.COLOR_COMPLETE));
        }
        else {
            b.setBackgroundColor(getResources().getColor(R.color.COLOR_INCOMPLETE));
        }
        // lower alpha if empty category, else default to full 255
        if(isCategoryEmpty(category)) {
            b.getBackground().setAlpha(100);
        }
    }

    /**
     *
     * @param category - the category
     */
    public boolean isCategoryComplete(CategoryEntity category){
        categoryItems = db.itemDAO().getItemsInCategory(category.getCategoryId());
        boolean isComplete = true;

        for (ItemEntity item : categoryItems) {
            if (item.getComplete() == 0 || item.getComplete() == 2) {
                isComplete = false;
                break;
            }
        }

        if (isComplete) {
            // may not want to change in the list and the db simultaneously
            // may want to either: combine the process or just read from db, eliminating the list
            category.setIsComplete(1);
            db.categoryDAO().findByName(category.getName()).setIsComplete(1);
            return true;
        }

        category.setIsComplete(0);
        return false;
    }

    /**
     *
     * @param category - the category
     */
    public boolean isCategoryEmpty(CategoryEntity category){
        categoryItems = db.itemDAO().getItemsInCategory(category.getCategoryId());
        return categoryItems.size() == 0;
    }

    /**
     *
     * @param buttonContainer -
     */
    public void addCategory(final LinearLayout buttonContainer){
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
                            // create button and add to layout
                            Button newCategoryButton = new Button(Overview.this);
                            setButtonFeatures(newCategory, newCategoryButton);
                            addButtonListeners(newCategoryButton, buttonContainer, db);
                            buttonsInUse.add(newCategoryButton);
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

    public void addButtonListeners(final Button button, final LinearLayout buttonContainer, final AppDatabase db){
        // short click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryPage(button.getText().toString());
            }
        });

        // long click - allow context menu to show on long press
        // context menu will show on long press
        registerForContextMenu(button);
        button.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_options_menu, menu);
        this.pressed_button = v;
        // if item is currently active, display 'deactivate' option
        // if inactive, show 'activate'
        if(db.categoryDAO().findByName(((Button) this.pressed_button).getText().toString()).getIsActive() == 1){
            //groupId, itemId, order, title
            menu.add(0, DEACTIVATE_ID, 1, R.string.deactivate_item);
        }
        else {
            menu.add(0, ACTIVATE_ID, 1, R.string.activate_item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final Button button = (Button) this.pressed_button;
        // get item to change name or delete
        final CategoryEntity selected_category = db.categoryDAO().findByName(button.getText().toString());
        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.change_item_name:
                final EditText input = new EditText(Overview.this);
                // fill in current name
                input.setText(button.getText());
                // set cursor at end
                input.setSelection(input.getText().length());

                // set up dialog box
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Overview.this);
                alertDialog.setTitle("change name")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                button.setText(input.getText().toString());
                                // change item name
                                selected_category.setName(button.getText().toString());
                                // change in db
                                try{
                                    db.categoryDAO().updateItem(selected_category);
                                }
                                catch(Exception e){
                                    // show toast
                                    Toast.makeText(Overview.this, "Could not change name", Toast.LENGTH_SHORT).show();
                                    Log.d("UPDATE", "failure updating "  + selected_category.getCategoryId() + ": " + e.getMessage());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                alertDialog.create().show();

                return true;
            case R.id.delete_item:
                try {
                    db.categoryDAO().delete(selected_category);
                    this.buttonContainer.removeView(this.pressed_button);
                    return true;
                }
                catch(Exception e){
                    Toast.makeText(Overview.this, "Could not delete", Toast.LENGTH_SHORT).show();
                }
                return false;
            case DEACTIVATE_ID:
                try{
                    selected_category.setIsActive(0);
                    db.categoryDAO().updateItems(selected_category);
                    //move button to bottom
                    this.buttonContainer.removeView(this.pressed_button);
                    this.buttonContainer.addView(this.pressed_button);
                    // decrease alpha
                    this.pressed_button.setAlpha((float)0.3);
                    // deactivate all items contained
                    List<ItemEntity> items = db.itemDAO().getActiveItemsInCategory(selected_category.getCategoryId());
                    for(ItemEntity i : items){
                        i.setIsActive(0);
                    }
                    db.itemDAO().updateItems(items);
                    return true;
                }
                catch (Exception e){
                    Toast.makeText(Overview.this, "Could not deactivate", Toast.LENGTH_SHORT).show();
                }
                return false;
            case ACTIVATE_ID:
                try{
                    selected_category.setIsActive(1);
                    db.categoryDAO().updateItems(selected_category);
                    this.pressed_button.setAlpha((float)1.0);
                    // activate all items contained
                    List<ItemEntity> items = db.itemDAO().getItemsInCategory(selected_category.getCategoryId());
                    for(ItemEntity i : items){
                        i.setIsActive(1);
                    }
                    db.itemDAO().updateItems(items);
                    return true;
                }
                catch (Exception e){
                    Toast.makeText(Overview.this, "Could not activate", Toast.LENGTH_SHORT).show();
                }
                return false;
            default:
                return false;
        }
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