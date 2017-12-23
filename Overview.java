package com.example.daniel.todo_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by daniel on 12/23/17.
 */

public class Overview extends AppCompatActivity{
    // create the buttons - limit to 10 at one time
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;

    // just the buttons being used
    ArrayList<Button> buttonsInUse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        // link buttons to xml buttons
        b1 = findViewById(R.id.cat_1);
        b2 = findViewById(R.id.cat_2);
        b3 = findViewById(R.id.cat_3);
        b4 = findViewById(R.id.cat_4);
        b5 = findViewById(R.id.cat_5);
        b6 = findViewById(R.id.cat_6);
        b7 = findViewById(R.id.cat_7);

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
        buttonsInUse.add(b8);
        buttonsInUse.add(b9);
        buttonsInUse.add(b10);

        //b1.setText("asdfasdf");
        /*
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b1.getBackground().equals(getResources().getColor(R.color.incomplete))){
                    b1.setBackgroundColor(getResources().getColor(R.color.complete));
                }
                else {
                    b1.setBackgroundColor(getResources().getColor(R.color.incomplete));
                }
                //b1.setText("asdfasdf");
            }
        });
        */
        /*
        // add click listener to each button in use to change color on click
        for(int i = 0; i < buttonsInUse.size(); i++){
            final int j = i;
            buttonsInUse.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buttonsInUse.get(j).getBackground().equals(getResources().getColor(R.color.incomplete))){
                        buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.complete));
                    }
                    else {
                        buttonsInUse.get(j).setBackgroundColor(getResources().getColor(R.color.incomplete));
                    }
                }
            });
        }
        */
    }


}
