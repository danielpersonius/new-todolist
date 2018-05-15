package com.example.daniel.todo_list;

import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
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

        final int categoryId = db.categoryDAO().findByName(categoryName).getCategoryId();

        // get items in category
        List<ItemEntity> items;
        try {
            items = db.itemDAO().getItemsInCategory(categoryId);

            // initialize buttons in use
            buttonsInUse = new ArrayList<>();

            //
            final LinearLayout buttonContainer = findViewById(R.id.itemContainer);
            for(ItemEntity item : items){
                final Button b = new Button(this);
                b.setText(item.getName());
                b.setHeight(250);
                b.setTextSize(30);
                b.setTextColor(-1);

                // set color
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
                        final ItemEntity newItem = new ItemEntity(input.getText().toString(), categoryId);
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
        // set edit listeners
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

        //
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                // get item to change name or delete
                final ItemEntity item = db.itemDAO().findByName(button.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryPage.this);
                builder.setTitle("options")
                        //
                        .setPositiveButton("change name", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                                item.setName(button.getText().toString());
                                                // change in db
                                                try{
                                                    db.itemDAO().updateItem(item);
                                                }
                                                catch(Exception e){
                                                    // show toast
                                                    Toast.makeText(CategoryPage.this, "Could not change name", Toast.LENGTH_SHORT).show();
                                                    Log.d("UPDATE", "failure updating "  + item.getItemId() + ": " + e.getMessage());
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
                            }
                        })
                        .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.itemDAO().delete(item);
                                // remove button
                                buttonContainer.removeView(button);
                            }
                        })
                        .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();

                return true;
            }
        });
    }
}
