package com.example.touchapp.login.personal_info;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.EditActivity;
import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

public class Personal_infoActivity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    EditText username;
    Button btn;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_infoActivity.this, PermissionsActivity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> {
            username = findViewById(R.id.nameText);
            String name = username.getText().toString();
            Intent intent = new Intent(Personal_infoActivity.this, Personal_info_2Activity.class);
            intent.putExtra("keyname", name);
            startActivity(intent);
        });
        btn = findViewById(R.id.button_skip);
        btn.setOnClickListener(v -> startActivity(new Intent(Personal_infoActivity.this, Personal_info_3Activity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_infoActivity.this, Personal_info_2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}