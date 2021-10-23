package com.example.qrmenuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrmenuapp.Admin.Products;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void menuItems (View view) {
        startActivity(new Intent(this, Products.class));
    }

    public void entryApp (View view) {
        startActivity(new Intent(this, StartActivity.class));
        //startActivity(new Intent(this, ProductCatalog.class));
    }
}