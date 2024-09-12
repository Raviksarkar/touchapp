package com.example.touchapp.login.personal_info;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

/**
 * @noinspection ALL
 */
public class Personal_info_3Activity extends AppCompatActivity {

    ImageButton btn_p;
    ImageButton btn_n;

    private ImageView maleImageView, femaleImageView;
    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info3);

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_3Activity.this, Personal_info_2Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Personal_info_3Activity.this, Personal_info_4Activity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_3Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        maleImageView = findViewById(R.id.maleImageView);
        femaleImageView = findViewById(R.id.femaleImageView);
        maleImageView.setOnClickListener(v -> selectGender("Male"));
        femaleImageView.setOnClickListener(v -> selectGender("Female"));
    }

    private void selectGender(String gender) {
        selectedGender = gender;
        if (gender.equals("Male")) {
            maleImageView.setColorFilter(ContextCompat.getColor(this, R.color.blue));
            femaleImageView.clearColorFilter();
        } else if (gender.equals("Female")) {
            femaleImageView.setColorFilter(ContextCompat.getColor(this, R.color.pink));
            maleImageView.clearColorFilter();
        }
        Toast.makeText(this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
    }
}
