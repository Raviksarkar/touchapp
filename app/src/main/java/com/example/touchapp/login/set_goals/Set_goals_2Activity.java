package com.example.touchapp.login.set_goals;

import android.annotation.SuppressLint;
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

public class Set_goals_2Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private double distanceGoal = 0.0; // Default distance goal
    private String distanceUnits = "units"; // Default units
    private final String[] distanceUnitOptions = {"km", "miles"};
    private final List<Double> distanceGoals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals2);

        // Initialize distance goals
        for (double i = 1.0; i <= 20.0; i += 1.0) {
            distanceGoals.add(i);
        }

        TextView distanceGoalTextView = findViewById(R.id.distanceGoalTextView);
        Button selectDistanceGoalButton = findViewById(R.id.selectDistanceGoalButton);

        // Set default display text
        updateDistanceGoalTextView(distanceGoalTextView);

        selectDistanceGoalButton.setOnClickListener(v -> showDistanceGoalSelectionDialog());

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Set_goals_2Activity.this, Set_goals_1Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Set_goals_2Activity.this, Set_goals_3Activity.class)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the specific activity
                Intent intent = new Intent(Set_goals_2Activity.this, PermissionsActivity.class);
                startActivity(intent);
                // Optionally, finish this activity to remove it from the back stack
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateDistanceGoalTextView(TextView distanceGoalTextView) {
        if (distanceGoal == 0.0 && distanceUnits.equals("units")) {
            distanceGoalTextView.setText("00 units");
        } else {
            distanceGoalTextView.setText(String.format(Locale.getDefault(), "%.1f %s", distanceGoal, distanceUnits));
        }
    }

    private void showDistanceGoalSelectionDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_distance_picker, null);

        // Initialize Spinners
        Spinner distanceSpinner = dialogView.findViewById(R.id.distanceSpinner);
        Spinner distanceUnitsSpinner = dialogView.findViewById(R.id.distanceUnitsSpinner);

        ArrayAdapter<Double> distanceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, distanceGoals);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(distanceAdapter);

        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, distanceUnitOptions);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceUnitsSpinner.setAdapter(unitsAdapter);

        // Set initial selections
        distanceSpinner.setSelection(distanceGoals.indexOf(distanceGoal));
        distanceUnitsSpinner.setSelection(findIndexOfDistanceUnit(distanceUnits));

        // Handle distance selection
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distanceGoal = distanceGoals.get(position);
                updateDistanceGoalTextView(findViewById(R.id.distanceGoalTextView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Handle unit selection
        distanceUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUnit = distanceUnitOptions[position];
                if (!distanceUnits.equals(selectedUnit)) {
                    if (selectedUnit.equals("miles")) {
                        distanceGoal *= 0.621371; // Convert km to miles
                    } else {
                        distanceGoal /= 0.621371; // Convert miles to km
                    }
                    distanceUnits = selectedUnit;
                    updateDistanceGoalTextView(findViewById(R.id.distanceGoalTextView));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Show the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Update the distance goal display with selected values
                    updateDistanceGoalTextView(findViewById(R.id.distanceGoalTextView));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Reset to default "00 units" if cancelled
                    distanceGoal = 0.0;
                    distanceUnits = "units";
                    updateDistanceGoalTextView(findViewById(R.id.distanceGoalTextView));
                    dialog.dismiss();
                })
                .show();
    }

    private int findIndexOfDistanceUnit(String unit) {
        for (int i = 0; i < distanceUnitOptions.length; i++) {
            if (distanceUnitOptions[i].equals(unit)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }
}
