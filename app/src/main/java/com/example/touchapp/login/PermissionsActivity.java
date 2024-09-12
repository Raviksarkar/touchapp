package com.example.touchapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.R;
import com.example.touchapp.login.personal_info.Personal_infoActivity;

public class PermissionsActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        btn = findViewById(R.id.button_ok);
        btn.setOnClickListener(v -> startActivity(new Intent(PermissionsActivity.this, Personal_infoActivity.class)));

    }
}