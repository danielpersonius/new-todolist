package com.example.daniel.todo_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * todo - fix positioning of new items
 * todo - create related database tables
 */
public class BujoDayPage extends Page {
    //
    protected RelativeLayout sectionOne;
    protected RelativeLayout sectionTwo;
    protected RelativeLayout sectionThree;
    //
    protected TextView sectionOneHeader;
    protected TextView sectionTwoHeader;
    protected TextView sectionThreeHeader;
    //
    protected LinearLayout sectionOneItemLayout;
    protected LinearLayout sectionTwoItemLayout;
    protected LinearLayout sectionThreeItemLayout;
    //
    protected View pressedTextView;
    //
    protected ViewParent pressedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set action bar text
        setTitle("Bullet Journal");
        setContentView(R.layout.bujo_day);

        // get layouts
        sectionOne = findViewById(R.id.sectionOne);
        sectionTwo = findViewById(R.id.sectionTwo);
        sectionThree = findViewById(R.id.sectionThree);
        sectionOneItemLayout = findViewById(R.id.sectionOneItemLayout);
        sectionTwoItemLayout = findViewById(R.id.sectionTwoItemLayout);
        sectionThreeItemLayout = findViewById(R.id.sectionThreeItemLayout);

        // get headers
        sectionOneHeader = findViewById(R.id.sectionOneHeader);
        sectionTwoHeader = findViewById(R.id.sectionTwoHeader);
        sectionThreeHeader = findViewById(R.id.sectionThreeHeader);

        // add listeners
        addHeaderClickListener(sectionOneHeader);
        addHeaderClickListener(sectionTwoHeader);
        addHeaderClickListener(sectionThreeHeader);

        // get buttons
        Button addWorkItemButton = findViewById(R.id.addSectionOneItemButton);
        Button addTodoItemButton = findViewById(R.id.addSectionTwoItemButton);
        Button addStudyItemButton = findViewById(R.id.addSectionThreeItemButton);

        // add listeners
        addAddButtonClickListener(addWorkItemButton, sectionOne);
        addAddButtonClickListener(addTodoItemButton, sectionTwo);
        addAddButtonClickListener(addStudyItemButton, sectionThree);

    }

    /**
     * @param b - button to add listener to
     */
    public void addAddButtonClickListener(Button b, final RelativeLayout layout) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog box
                final EditText input = new EditText(BujoDayPage.this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BujoDayPage.this);
                alertDialog.setTitle("add item")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addItem(input.getText().toString(), layout);
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
            }
        });
    }

    /**
     *
     * @param header - the TextView pressed
     */
    public void addHeaderClickListener(final TextView header){
        header.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText input = new EditText(BujoDayPage.this);
                // fill in current title
                input.setText(header.getText().toString());
                // set cursor at end
                input.setSelection(input.getText().length());

                // set up dialog box
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BujoDayPage.this);
                alertDialog.setTitle("change section")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                header.setText(input.getText().toString());
                                // change in db

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                alertDialog.create().show();
                return false;
            }
        });
    }

    /**
     *
     * @param name - name of new item
     * @param layout - layout to add new item to
     */
    public void addItem(String name, RelativeLayout layout){
        TextView item = new TextView(BujoDayPage.this);
        item.setText(name);

        registerForContextMenu(item);
        item.setOnCreateContextMenuListener(this);
        // each section's item layout is first in the section's RelativeLayout, so just add to that
        ((LinearLayout) layout.getChildAt(0)).addView(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bujo_item_options_menu, menu);
        // save refs here so can access in AlertDialog
        this.pressedTextView = v;
        this.pressedLayout = v.getParent();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final TextView text = (TextView) this.pressedTextView;
        final LinearLayout layout = (LinearLayout) this.pressedLayout;

        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.change_item_name:
                final EditText input = new EditText(BujoDayPage.this);
                // fill in current name
                input.setText(text.getText().toString());
                // set cursor at end
                input.setSelection(input.getText().length());

                // set up dialog box
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BujoDayPage.this);
                alertDialog.setTitle("change name")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                text.setText(input.getText().toString());

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
                layout.removeView(text);
        }
        return false;
    }
}
