package com.example.qrmenuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrmenuapp.Admin.Auth.PasswordActivity;
import com.example.qrmenuapp.User.CameraActivity;

public class StartActivity extends AppCompatActivity {

    private ImageView mLogo;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start1);

        mLogo = findViewById(R.id.logo);
        mButton = findViewById(R.id.scan);

        mLogo.setLongClickable(true);

        mLogo.setOnLongClickListener(v -> {
            startActivity(new Intent(this, PasswordActivity.class));
            Toast.makeText(StartActivity.this, "Admin panel", Toast.LENGTH_SHORT).show();
            return true;
        });

        mButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}