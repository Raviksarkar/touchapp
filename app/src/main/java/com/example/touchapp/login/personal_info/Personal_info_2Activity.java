package com.example.touchapp.login.personal_info;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.touchapp.EditActivity;
import com.example.touchapp.R;
import com.example.touchapp.login.PermissionsActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Personal_info_2Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    private StorageReference storageReference;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri pickedImageUri = result.getData().getData();
                    if (pickedImageUri != null) {
                        ImageView imageView = findViewById(R.id.profile_image);
                        imageView.setImageURI(pickedImageUri);
                        imageView.setTag(pickedImageUri.toString()); // Store URI as tag
                    } else {
                        Log.e("ImagePick", "Failed to pick image or data is null");
                        Toast.makeText(this, "Failed to pick image. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info2);
        ImageView imageView = findViewById(R.id.profile_image);
        FloatingActionButton button = findViewById(R.id.change_profile_btn);
        ImageButton btn_p = findViewById(R.id.button_prev);
        ImageButton btn_n = findViewById(R.id.btn_save);
        Button btn = findViewById(R.id.button_skip);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_2Activity.this, Personal_infoActivity.class)));

        btn_n.setOnClickListener(v -> {
            Uri pickedImageUri = (Uri) imageView.getTag();
            if (pickedImageUri != null) {
                uploadImageToFirebase(pickedImageUri);
            } else {
                navigateToEditActivity(null);
            }
        });

        btn.setOnClickListener(v -> startActivity(new Intent(Personal_info_2Activity.this, Personal_info_3Activity.class)));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.blue)));
        }

        button.setOnClickListener(v -> ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                }));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_2Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference imageRef = storageReference.child("profile_images/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            Log.d("PersonalInfo2Activity", "Image uploaded. URL: " + imageUrl);
            navigateToEditActivity(imageUrl);
        }).addOnFailureListener(exception -> {
            Log.e("PersonalInfo2Activity", "Failed to get image URL", exception);
            Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(exception -> {
            Log.e("PersonalInfo2Activity", "Failed to upload image", exception);
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToEditActivity(String imageUrl) {
        Intent intent = new Intent(Personal_info_2Activity.this, EditActivity.class);
        if (imageUrl != null) {
            intent.putExtra("IMAGE_URL", imageUrl);
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission denied. Unable to access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
