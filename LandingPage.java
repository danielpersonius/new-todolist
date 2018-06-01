package com.example.daniel.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {
    Class c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title bar
        ActionBar bar = this.getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
        setContentView(R.layout.landing_page);

        // get buttons
        Button OverviewButton = findViewById(R.id.overview_page_button);
        Button settingsPageButton = findViewById(R.id.settings_page_button);
        Button bujoPageButton = findViewById(R.id.bujo_page_button);

        // add listeners
        OverviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, Overview.class);
                startActivity(intent);
            }
        });

        settingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, Settings.class);
                startActivity(intent);
            }
        });

        bujoPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, BujoDayPage.class);
                startActivity(intent);
            }
        });

    }
}
