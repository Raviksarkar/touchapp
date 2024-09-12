package com.example.touchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    private TextView calendarTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarTextView = findViewById(R.id.calendar_text_view);
        backButton = findViewById(R.id.back_button);
        calendarTextView.setText("Calendar Page");
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
