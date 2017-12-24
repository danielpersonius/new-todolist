package com.example.daniel.todo_list;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String pwd, inputStr;
    EditText pwdBox;
    // temp test button
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.overview);

        pwdBox = (EditText) findViewById(R.id.pwdBox);
        // temp hard code password
        pwd = "password";
        inputStr = "";

        // link buttons to xml buttons
        b1 = findViewById(R.id.cat_1);
        b2 = findViewById(R.id.cat_2);
        b3 = findViewById(R.id.cat_3);
        b4 = findViewById(R.id.cat_4);
        b5 = findViewById(R.id.cat_5);
        b6 = findViewById(R.id.cat_6);
        b7 = findViewById(R.id.cat_7);

        final ArrayList<Button> al = new ArrayList<>();
        // fill by default
        al.add(b1);
        al.add(b2);
        al.add(b3);
        al.add(b4);
        al.add(b5);
        al.add(b6);
        al.add(b7);

        for(int i = 0; i < al.size(); i++){
            final int j = i;
            al.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int colorId = ((ColorDrawable) al.get(j).getBackground()).getColor();
                    if(colorId == getResources().getColor(R.color.incomplete)){
                        al.get(j).setBackgroundColor(getResources().getColor(R.color.complete));
                        al.get(j).setText("Complete");
                    }
                    else {
                        al.get(j).setBackgroundColor(getResources().getColor(R.color.incomplete));
                        al.get(j).setText("Incomplete");
                    }
                }
            });

        }



    }

    /**
     * get input from text field and compare to password
     * @return boolean -
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
