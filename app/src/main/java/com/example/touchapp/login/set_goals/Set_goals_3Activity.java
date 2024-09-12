package com.example.touchapp.login.set_goals;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.touchapp.login.PermissionsActivity;
import com.example.touchapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Set_goals_3Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private int calorieGoal = 0; // Default calorie goal set to 0
    private final List<Integer> calorieGoals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals3);

        // Initialize calorie goals
        for (int i = 1000; i <= 4000; i += 100) {
            calorieGoals.add(i);
        }

        TextView calorieGoalTextView = findViewById(R.id.calorieGoalTextView);
        Button selectCalorieGoalButton = findViewById(R.id.selectCalorieGoalButton);

        // Set default display text to "00 kcal"
        updateCalorieGoalTextView(calorieGoalTextView);

        selectCalorieGoalButton.setOnClickListener(v -> showCalorieGoalSelectionDialog());

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Set_goals_3Activity.this, Set_goals_2Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Set_goals_3Activity.this, Set_goals_4Activity.class)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the specific activity
                Intent intent = new Intent(Set_goals_3Activity.this, PermissionsActivity.class);
                startActivity(intent);
                // Optionally, finish this activity to remove it from the back stack
                finish();
            }
        });
    }

    private void updateCalorieGoalTextView(TextView calorieGoalTextView) {
        if (calorieGoal == 0) {
            calorieGoalTextView.setText(getString(R.string.calorie_goal_default));
        } else {
            calorieGoalTextView.setText(String.format(Locale.getDefault(), getString(R.string.calorie_goal_format), calorieGoal));
        }
    }

    private void showCalorieGoalSelectionDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_calorie_picker, null);

        // Initialize Spinner
        Spinner calorieSpinner = dialogView.findViewById(R.id.calorieSpinner);

        ArrayAdapter<Integer> calorieAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, calorieGoals);
        calorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calorieSpinner.setAdapter(calorieAdapter);

        // Set initial selection
        calorieSpinner.setSelection(calorieGoals.indexOf(calorieGoal));

        // Handle spinner selection
        calorieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calorieGoal = calorieGoals.get(position);
                updateCalorieGoalTextView(findViewById(R.id.calorieGoalTextView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Show the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    // Update the calorie goal display with selected values
                    updateCalorieGoalTextView(findViewById(R.id.calorieGoalTextView));
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    // Reset to default "00 kcal" if cancelled
                    calorieGoal = 0;
                    updateCalorieGoalTextView(findViewById(R.id.calorieGoalTextView));
                    dialog.dismiss();
                })
                .show();
    }
}
