package com.unipi.nikolastyl.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goRegister(View v){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));

    }

    public void loginBtn(View v){

    }
}