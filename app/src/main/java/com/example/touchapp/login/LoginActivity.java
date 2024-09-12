package com.example.touchapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.R;

public class LoginActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = findViewById(R.id.button_agree);
        btn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PermissionsActivity.class)));

    }
}