package com.example.daniel.todo_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String pwd, inputStr;
    EditText pwdBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.overview);

        pwdBox = (EditText) findViewById(R.id.pwdBox);

        // temp hard code password
        pwd = "password";
        inputStr = "";
        int i = 0;

        pwdBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkPassword()){
                    showOverview();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * get input from text field and compare to password
     * @return boolean -
     */
    public boolean checkPassword(){
        return pwdBox.getText().toString().equals(getPwd());
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public String getPwd(){
        return this.pwd;
    }

    public void showOverview(){
        Intent intent = new Intent(this, Overview.class);
        startActivity(intent);
    }

    /*
    public void setInputStr(String newStr){
        this.inputStr = newStr;
    }

    public String getInputString(EditText box){
        return box.getText().toString();
    }
    */
}
