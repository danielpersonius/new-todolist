package com.example.daniel.todo_list;

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


    }


}
