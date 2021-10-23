package com.example.qrmenuapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrmenuapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button mButton;
    private Button SignOut;
    private Button test;
    private TextView mTextView;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.Products);
        SignOut = findViewById(R.id.SignOutButton);
        test = findViewById(R.id.test);

        if(mUser != null) {
            String email = mUser.getEmail();

            mTextView.setText("Hello " + email);
        }


        mButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Products.class));
        });

        SignOut.setOnClickListener(v -> {
            mAuth.signOut();
            finish();
        });

        test.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}