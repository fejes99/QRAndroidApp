package com.example.qrmenuapp.Admin.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrmenuapp.R;

public class PasswordActivity extends AppCompatActivity {

    private EditText mPassword;
    private Button mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mPassword = findViewById(R.id.PasswordApp);
        mConfirm = findViewById(R.id.passwordAppConfirm);

        mConfirm.setOnClickListener(v -> {
            String password = mPassword.getText().toString();

            if(password.equals("1234")) {
                updateUI();
            } else {
                Toast.makeText(PasswordActivity.this, "Wrong password, get away from admin panel you USER", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUI() {
        startActivity(new Intent(this, EntryActivity.class));
        finish();
    }
}