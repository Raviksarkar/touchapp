package com.example.touchapp.login.set_goals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

import java.util.ArrayList;
import java.util.List;

public class Set_goals_1Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private int dailyStepGoal = 0; // Default daily step goal (show "00 steps" initially)
    private final List<Integer> dailySteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals1);

        // Initialize daily step goals
        for (int i = 5000; i <= 30000; i += 1000) {
            dailySteps.add(i);
        }

        TextView dailyStepGoalTextView = findViewById(R.id.dailyStepGoalTextView);
        Button selectDailyStepGoalButton = findViewById(R.id.selectDailyStepGoalButton);

        // Set default step goal
        updateDailyStepGoalTextView(dailyStepGoalTextView); // This will show "00 steps" initially

        selectDailyStepGoalButton.setOnClickListener(v -> showDailyStepGoalSelectionDialog());

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Set_goals_1Activity.this, Set_goalsActivity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Set_goals_1Activity.this, Set_goals_2Activity.class)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the specific activity
                Intent intent = new Intent(Set_goals_1Activity.this, PermissionsActivity.class);
                startActivity(intent);
                // Optionally, finish this activity to remove it from the back stack
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateDailyStepGoalTextView(TextView dailyStepGoalTextView) {
        if (dailyStepGoal == 0) {
            dailyStepGoalTextView.setText("00 steps");
        } else {
            dailyStepGoalTextView.setText(dailyStepGoal + " steps");
        }
    }

    private void showDailyStepGoalSelectionDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_daily_step_picker, null);

        // Initialize Spinner
        Spinner dailyStepsSpinner = dialogView.findViewById(R.id.dailyStepsSpinner);

        ArrayAdapter<Integer> dailyStepsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dailySteps);
        dailyStepsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dailyStepsSpinner.setAdapter(dailyStepsAdapter);

        // Set initial selection
        dailyStepsSpinner.setSelection(dailySteps.indexOf(dailyStepGoal));

        // Handle spinner selection
        dailyStepsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dailyStepGoal = dailySteps.get(position);
                // Do not update immediately; only update on OK
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Show the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Update the step goal display with selected value
                    updateDailyStepGoalTextView(findViewById(R.id.dailyStepGoalTextView));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Reset to default "00 steps" if cancelled
                    dailyStepGoal = 0;
                    updateDailyStepGoalTextView(findViewById(R.id.dailyStepGoalTextView));
                    dialog.dismiss();
                })
                .show();
    }
}
