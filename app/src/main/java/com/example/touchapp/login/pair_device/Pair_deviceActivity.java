package com.example.touchapp.login.pair_device;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.MainActivity;
import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

public class Pair_deviceActivity extends AppCompatActivity {

    ImageButton btn_c;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device);
        btn = findViewById(R.id.button_skip);
        btn.setOnClickListener(v -> startActivity(new Intent(Pair_deviceActivity.this, MainActivity.class)));
        btn_c = findViewById(R.id.button_Connect);
        btn_c.setOnClickListener(v -> startActivity(new Intent(Pair_deviceActivity.this, Pairing_deviceActivity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Pair_deviceActivity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}