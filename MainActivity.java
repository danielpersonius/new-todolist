package com.example.daniel.todo_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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

    }

    /**
     * get input from text field and compare to password
     * @return boolean
     */
    public boolean checkPassword(){
        // test
        System.out.println("check");
        return getInputString(pwdBox) == getPwd();
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public String getPwd(){
        return this.pwd;
    }

    public String getInputString(EditText box){
        return box.getText().toString();
    }
}
