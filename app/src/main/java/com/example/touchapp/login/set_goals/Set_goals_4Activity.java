package com.example.touchapp.login.set_goals;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.login.pair_device.Pair_deviceActivity;
import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Set_goals_4Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private int sleepHoursGoal = 0; // Default sleep hours goal set to 0
    private int sleepMinutesGoal = 0; // Default sleep minutes goal set to 0

    private final List<Integer> sleepHours24 = new ArrayList<>();
    private final List<Integer> sleepHours12 = new ArrayList<>();
    private final List<Integer> sleepMinutes = new ArrayList<>();
    private boolean is24HourFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals4);

        // Check the device's time format
        is24HourFormat = DateFormat.is24HourFormat(this);

        // Initialize sleep hours and minutes
        for (int i = 0; i <= 23; i++) {
            sleepHours24.add(i);
        }
        for (int i = 1; i <= 12; i++) {
            sleepHours12.add(i);
        }
        for (int i = 0; i < 60; i += 5) {
            sleepMinutes.add(i);
        }

        TextView sleepTimeTextView = findViewById(R.id.sleepTimeTextView);
        Button selectSleepTimeButton = findViewById(R.id.selectSleepTimeButton);

        // Display initial default text: "0 hours 0 minutes"
        updateSleepTimeTextView(sleepTimeTextView);

        selectSleepTimeButton.setOnClickListener(v -> showSleepTimeSelectionDialog());

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Set_goals_4Activity.this, Set_goals_3Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Set_goals_4Activity.this, Pair_deviceActivity.class)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the specific activity
                Intent intent = new Intent(Set_goals_4Activity.this, PermissionsActivity.class);
                startActivity(intent);
                // Optionally, finish this activity to remove it from the back stack
                finish();
            }
        });
    }

    private void updateSleepTimeTextView(TextView sleepTimeTextView) {
        String format;
        if (sleepHoursGoal == 0 && sleepMinutesGoal == 0) {
            format = getString(R.string.sleep_time_default);
        } else {
            if (is24HourFormat) {
                format = getString(R.string.sleep_time_format_24);
                sleepTimeTextView.setText(String.format(Locale.getDefault(), format, sleepHoursGoal, sleepMinutesGoal));
            } else {
                // Convert 24-hour format to 12-hour format for display
                int displayHours = sleepHoursGoal % 12;
                if (displayHours == 0) displayHours = 12; // Handle midnight/noon
                String period = (sleepHoursGoal >= 12) ? "PM" : "AM";
                format = getString(R.string.sleep_time_format_12);
                sleepTimeTextView.setText(String.format(Locale.getDefault(), format, displayHours, sleepMinutesGoal, period));
            }
            return;
        }
        sleepTimeTextView.setText(format);
    }

    private void showSleepTimeSelectionDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_sleep_time_picker, null);

        // Initialize Spinners
        Spinner sleepHoursSpinner = dialogView.findViewById(R.id.sleepHoursSpinner);
        Spinner sleepMinutesSpinner = dialogView.findViewById(R.id.sleepMinutesSpinner);

        ArrayAdapter<Integer> hoursAdapter;
        if (is24HourFormat) {
            hoursAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sleepHours24);
        } else {
            hoursAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sleepHours12);
        }
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepHoursSpinner.setAdapter(hoursAdapter);

        ArrayAdapter<Integer> minutesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sleepMinutes);
        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepMinutesSpinner.setAdapter(minutesAdapter);

        // Set initial selections
        sleepHoursSpinner.setSelection(is24HourFormat ? sleepHours24.indexOf(sleepHoursGoal) : sleepHours12.indexOf(sleepHoursGoal % 12));
        sleepMinutesSpinner.setSelection(sleepMinutes.indexOf(sleepMinutesGoal));

        // Handle spinner selections
        sleepHoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedHour = (is24HourFormat) ? sleepHours24.get(position) : sleepHours12.get(position);
                sleepHoursGoal = is24HourFormat ? selectedHour : (selectedHour + 12) % 24;
                updateSleepTimeTextView(findViewById(R.id.sleepTimeTextView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sleepMinutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sleepMinutesGoal = sleepMinutes.get(position);
                updateSleepTimeTextView(findViewById(R.id.sleepTimeTextView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Show the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    // Update the sleep time display with selected values
                    updateSleepTimeTextView(findViewById(R.id.sleepTimeTextView));
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    // Reset to default "0 hours 0 minutes" if cancelled
                    sleepHoursGoal = 0;
                    sleepMinutesGoal = 0;
                    updateSleepTimeTextView(findViewById(R.id.sleepTimeTextView));
                    dialog.dismiss();
                })
                .show();
    }
}
