package com.example.accountbook.tools;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by asd on 2/17/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public  <T extends View> T f(int id){
        return (T) findViewById(id);
    }

}
