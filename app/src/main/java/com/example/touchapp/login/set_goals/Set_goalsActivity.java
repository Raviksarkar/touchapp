package com.example.touchapp.login.set_goals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.login.pair_device.Pair_deviceActivity;
import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

public class Set_goalsActivity extends AppCompatActivity {
   ImageButton btn_g;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);
        btn=findViewById(R.id.button_skip);
        btn.setOnClickListener(v -> startActivity(new Intent(Set_goalsActivity.this, Pair_deviceActivity.class)));
        btn_g=findViewById(R.id.button_goals);
        btn_g.setOnClickListener(v -> startActivity(new Intent(Set_goalsActivity.this,Set_goals_1Activity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the specific activity
                Intent intent = new Intent(Set_goalsActivity.this, PermissionsActivity.class);
                startActivity(intent);
                // Optionally, finish this activity to remove it from the back stack
                finish();
            }
        });

    }
}