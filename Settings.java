package com.example.daniel.todo_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.*;

/**
 * Created by daniel on 12/25/17.
 */

public class Settings extends Page {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set action bar title
        setTitle("settings");
        setContentView(R.layout.settings);


    }
}
