package com.example.touchapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class EditActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView nameEditText;
    private TextView genderTextView, birthdayTextView, weightTextView, heightTextView, walkingStepTextView, runningStepTextView;
    private static final int IMAGE_PICKER_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.nameEditText);
        genderTextView = findViewById(R.id.genderTextView);
        birthdayTextView = findViewById(R.id.birthdayTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        walkingStepTextView = findViewById(R.id.walkingStepTextView);
        runningStepTextView = findViewById(R.id.runningStepTextView);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(profileImageView);
        } else {
            Toast.makeText(this, "Image URL is invalid", Toast.LENGTH_SHORT).show();
        }
        String username = getIntent().getStringExtra("keyname");
        nameEditText.setText(username);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(EditActivity.this, SettingActivity.class));
            finish();
        });
        loadProfileData();
        Button changeProfileImageButton = findViewById(R.id.changeProfileImageButton);
        changeProfileImageButton.setOnClickListener(v -> ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(IMAGE_PICKER_REQUEST_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data != null ? data.getData() : null;
                if (uri != null) {
                    profileImageView.setImageURI(uri);
                } else {
                    Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadProfileData() {
        String gender = getIntent().getStringExtra("USER_GENDER");
        genderTextView.setText(gender != null ? gender : "Unknown");
        String birthday = getIntent().getStringExtra("USER_BIRTHDAY");
        birthdayTextView.setText(birthday != null ? birthday : "Unknown");
        String weight = getIntent().getStringExtra("USER_WEIGHT");
        weightTextView.setText(weight != null ? weight : "Unknown");
        String height = getIntent().getStringExtra("USER_HEIGHT");
        heightTextView.setText(height != null ? height : "Unknown");
        String walkingStep = getIntent().getStringExtra("USER_WALKING_STEP");
        walkingStepTextView.setText(walkingStep != null ? walkingStep : "Unknown");
        String runningStep = getIntent().getStringExtra("USER_RUNNING_STEP");
        runningStepTextView.setText(runningStep != null ? runningStep : "Unknown");
    }
}
