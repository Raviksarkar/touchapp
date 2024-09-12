package com.example.touchapp.login.personal_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

public class Personal_info_4Activity extends AppCompatActivity {
    private TextView selectedDateTextView;
    ImageButton btn_p;
    ImageButton btn_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info4);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        View showDatePickerButton = findViewById(R.id.showDatePickerButton);
        showDatePickerButton.setOnClickListener(v -> showCupertinoDatePicker());
        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_4Activity.this, Personal_info_3Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Personal_info_4Activity.this, Personal_info_5Activity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_4Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showCupertinoDatePicker() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_cupertino_date_picker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.cupertinoDatePicker);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        cancelButton.setOnClickListener(v -> alertDialog.dismiss());
        confirmButton.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            selectedDateTextView.setText(day + "/" + month + "/" + year);
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}

