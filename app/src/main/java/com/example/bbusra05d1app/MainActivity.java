package com.example.bbusra05d1app;

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
    public void store(View view) {
        Intent add = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(add);
    }
}