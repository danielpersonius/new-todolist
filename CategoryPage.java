package com.example.daniel.todo_list;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
                else {
                    b.setBackgroundColor(getResources().getColor(R.color.incomplete));
                }

                // set edit listeners
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int colorId = ((ColorDrawable) b.getBackground()).getColor();
                        if(colorId == getResources().getColor(R.color.incomplete)){
                            // change color
                            b.setBackgroundColor(getResources().getColor(R.color.complete));
                            // change completion status
                            db.itemDAO().updateItemCompletion(db.itemDAO().findByName(b.getText().toString()).getItemId(), 1);
                        }
                        else {
                            b.setBackgroundColor(getResources().getColor(R.color.incomplete));
                            // change completion status
                            db.itemDAO().updateItemCompletion(db.itemDAO().findByName(b.getText().toString()).getItemId(), 0);
                        }
                    }
                });

                b.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(final View view) {
                         // get item by current name before name change
                         final ItemEntity item = db.itemDAO().findByName(b.getText().toString());
                         final EditText input = new EditText(CategoryPage.this);
                         // set up dialog box
                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryPage.this);
                         alertDialog.setTitle("change name")
                                 .setView(input)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 b.setText(input.getText().toString());
                                 // change item name
                                 item.setName(b.getText().toString());

                                 // change in db
                                 try{
                                     db.itemDAO().updateItem(item);
                                 }
                                 catch(Exception e){
                                     // show toast
                                     Log.d("UPDATE", "failure inserting "  + item.getItemId() + ": " + e.getMessage());
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
     * this function does multiple things, so i want to decompose, but i don't
     * know how to pass the new ItemEntity data out of onClick
     * @param buttonContainer - the layout to add new item's button to
     * @param categoryId - id of parent category to satisfy foreign key constrain upon insertion
     * @param db - database handle
     */
    public void addItem(final LinearLayout buttonContainer, final int categoryId, final AppDatabase db){
        // below is to be able to choose category in the dialog

        // dialog box
        // get custom layout with EditText and Spinner
        //LayoutInflater inflater = getLayoutInflater();
        //View dialogLayout = inflater.inflate(R.layout.add_item, null);
//
//        // name text input and dropdown
//        //final EditText input = new EditText(CategoryPage.this);
//        final EditText input = dialogLayout.findViewById(R.id.new_item_name);
//
//        // Spinner
//        final Spinner spinner = dialogLayout.findViewById(R.id.category_spinner);
//        List<String> categoryNames = db.categoryDAO().getAllCategoryNames();
//
//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.category_spinner_item, categoryNames);
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.category_spinner_item);
//
//        spinner.setAdapter(spinnerArrayAdapter);
//
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryPage.this);
//        alertDialog.setTitle("add item")
//                .setView(dialogLayout)
//                .setPositiveButton("add", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // create item
//                        final ItemEntity newItem = new ItemEntity(input.getText().toString(), db.categoryDAO().getCategoryId(spinner.getSelectedItem().toString()));
//                        // insert
//                        try {
//                            db.itemDAO().insertAll(newItem);
//                        } catch (Exception e) {
//                            // toast
//                            Toast.makeText(CategoryPage.this, "could not add item", Toast.LENGTH_SHORT).show();
//                            Log.d("INSERT ITEM", "item insert failed: " + e.getMessage());
//                        }
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    // cancel button
//                    @Override
//                    public void onClick(DialogInterface d, int i) {
//                        d.cancel();
//                    }
//        });
//        alertDialog.show();

        //final EditText input = dialogLayout.findViewById(R.id.new_item_name);
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
}
