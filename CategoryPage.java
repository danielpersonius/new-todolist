package com.example.daniel.todo_list;

import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by daniel on 12/24/17.
 */

public class CategoryPage extends AppCompatActivity{
    //
    final int DEACTIVATE_ID = 1;
    //
    final int ACTIVATE_ID = 2;
    // just the buttons being used
    ArrayList<Button> buttonsInUse;
    //
    protected View pressed_button;
    //
    protected AppDatabase db;
    //
    protected LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title bar
        ActionBar bar = this.getSupportActionBar();
        if(bar != null){
            bar.hide();
        }
        setContentView(R.layout.category_page);

        // get category name
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");

        // db instance
        final AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        this.db = AppDatabase.getAppDatabase(getApplicationContext());

        final int categoryId = db.categoryDAO().findByName(categoryName).getCategoryId();

        // get items in category
        List<ItemEntity> activeItems;
        List<ItemEntity> inactiveItems;
        List<ItemEntity> allItems;

        try {
            activeItems = db.itemDAO().getActiveItemsInCategory(categoryId);
            inactiveItems = db.itemDAO().getInactiveItemsInCategory(categoryId);
            allItems = activeItems;
            allItems.addAll(inactiveItems);

            // initialize buttons in use
            buttonsInUse = new ArrayList<>();

            // linear layout to hold buttons
            buttonContainer = findViewById(R.id.itemContainer);

            // create buttons for all items
            for(ItemEntity item : allItems){
                final Button b = new Button(this);

                // set button features
                setButtonFeatures(item, b);

                // add listeners
                addButtonListeners(b, buttonContainer, db);

                // add to layout
                buttonContainer.addView(b);
                buttonsInUse.add(b);
            }

            // 'add' button
            Button addButton = findViewById(R.id.addItemButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // insert
                    addItem(buttonContainer, categoryId, db);
                }
            });

        }
        catch(Exception e){
            Log.d("BAD GET", "bad get: " + e.getMessage());
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

        Log.d("RESUME", "resumed");

    }

    /**
     * set text, height, text size, text color, alpha, and background color of item buttons
     * @param item - corresponding ItemEntity
     * @param b - current button
     */
    public void setButtonFeatures(ItemEntity item, Button b){
        b.setText(item.getName());
        b.setHeight(250);
        b.setTextSize(30);
        b.setTextColor(-1);
        setButtonColor(item, b);
        // decrease alpha if item is inactive
        if(item.getIsActive() != 1){
            b.setAlpha((float)0.3);
        }
    }

    /**
     *
     * @param item - corresponding itemEntity
     * @param b - button to set color of
     */
    public void setButtonColor(ItemEntity item, Button b){
        int isComplete = item.getComplete();
        if(isComplete == 1){
            b.setBackgroundColor(getResources().getColor(R.color.complete));
        }
        else if(isComplete == 2){
            b.setBackgroundColor(getResources().getColor(R.color.in_progress));
        }
        else {
            b.setBackgroundColor(getResources().getColor(R.color.incomplete));
        }
    }

    /**
     * this function does multiple things, so i want to decompose, but i don't
     * know how to pass the new ItemEntity data out of onClick
     * @param buttonContainer - the layout to add new item's button to
     * @param categoryId - id of parent category to satisfy foreign key constrain upon insertion
     * @param db - database handle
     */
    public void addItem(final LinearLayout buttonContainer, final int categoryId, final AppDatabase db){
        final EditText input = new EditText(CategoryPage.this);
        AlertDialog.Builder addDialog = new AlertDialog.Builder(CategoryPage.this);
        addDialog.setTitle("add item")
                .setView(input)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // create item
                        final ItemEntity newItem = new ItemEntity(input.getText().toString(), categoryId, 1);
                        // insert
                        try {
                            db.itemDAO().insertAll(newItem);
                            // add new button to layout
                            Button newItemButton = new Button(CategoryPage.this);
                            newItemButton.setBackgroundColor(getResources().getColor(R.color.incomplete));
                            newItemButton.setText(newItem.getName());
                            newItemButton.setHeight(250);
                            newItemButton.setTextSize(30);
                            newItemButton.setTextColor(-1);
                            // add listeners
                            addButtonListeners(newItemButton, buttonContainer, db);

                            buttonContainer.addView(newItemButton);

                        } catch (Exception e) {
                            // toast
                            Toast.makeText(CategoryPage.this, "could not add item", Toast.LENGTH_SHORT).show();
                            Log.d("INSERT ITEM", "item insert failed: " + e.getMessage());
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
                int colorId = ((ColorDrawable) button.getBackground()).getColor();
                // can't use switch statement since get color int not resolvable at compile time
                // if not started, set to in progress
                if(colorId == getResources().getColor(R.color.incomplete)){
                    // change color
                    button.setBackgroundColor(getResources().getColor(R.color.in_progress));
                    // change in db
                    db.itemDAO().updateItemCompletion(db.itemDAO().findByName(button.getText().toString()).getItemId(), 2);
                }
                else if(colorId == getResources().getColor(R.color.in_progress)){
                    button.setBackgroundColor(getResources().getColor(R.color.complete));
                    db.itemDAO().updateItemCompletion(db.itemDAO().findByName(button.getText().toString()).getItemId(), 1);
                }
                else {
                    button.setBackgroundColor(getResources().getColor(R.color.incomplete));
                    db.itemDAO().updateItemCompletion(db.itemDAO().findByName(button.getText().toString()).getItemId(), 0);
                }
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
        if(db.itemDAO().findByName(((Button) this.pressed_button).getText().toString()).getIsActive() == 1){
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
        final ItemEntity selected_item = db.itemDAO().findByName(button.getText().toString());
        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.change_item_name:
                final EditText input = new EditText(CategoryPage.this);
                // fill in current name
                input.setText(button.getText());
                // set cursor at end
                input.setSelection(input.getText().length());

                // set up dialog box
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryPage.this);
                alertDialog.setTitle("change name")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                button.setText(input.getText().toString());
                                // change item name
                                selected_item.setName(button.getText().toString());
                                // change in db
                                try{
                                    db.itemDAO().updateItem(selected_item);
                                }
                                catch(Exception e){
                                    // show toast
                                    Toast.makeText(CategoryPage.this, "Could not change name", Toast.LENGTH_SHORT).show();
                                    Log.d("UPDATE", "failure updating "  + selected_item.getItemId() + ": " + e.getMessage());
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
                    db.itemDAO().delete(selected_item);
                    this.buttonContainer.removeView(this.pressed_button);
                    return true;
                }
                catch(Exception e){
                    Toast.makeText(CategoryPage.this, "Could not delete", Toast.LENGTH_SHORT).show();
                }
                return false;
            case DEACTIVATE_ID:
                try{
                    selected_item.setIsActive(0);
                    db.itemDAO().updateItems(selected_item);
                    //move button to bottom
                    this.buttonContainer.removeView(this.pressed_button);
                    this.buttonContainer.addView(this.pressed_button);
                    // decrease alpha
                    this.pressed_button.setAlpha((float)0.3);
                    return true;
                }
                catch (Exception e){
                    Toast.makeText(CategoryPage.this, "Could not deactivate", Toast.LENGTH_SHORT).show();
                }
                return false;
            case ACTIVATE_ID:
                try{
                    selected_item.setIsActive(1);
                    db.itemDAO().updateItems(selected_item);
                    this.pressed_button.setAlpha((float)1.0);
                    return true;
                }
                catch (Exception e){
                    Toast.makeText(CategoryPage.this, "Could not activate", Toast.LENGTH_SHORT).show();
                }
                return false;
            default:
                return false;
        }
    }
}
