package com.example.touchapp.login.personal_info;

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
import com.example.touchapp.login.set_goals.Set_goalsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class Personal_info_8Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private int selectedStepLength = 0;
    private String selectedStepLengthUnits = "units";
    private final String[] stepLengthUnits = {"cm", "in"};
    private final List<Integer> cmStepLengths = new ArrayList<>();
    private final List<Double> inStepLengths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info8);
        for (int i = 50; i <= 80; i++) {
            cmStepLengths.add(i);
            inStepLengths.add(i * 0.393701);
        }
        TextView stepLengthTextView = findViewById(R.id.stepLengthTextView);
        Button selectButton = findViewById(R.id.selectButton);
        updateStepLengthTextView(stepLengthTextView);
        selectButton.setOnClickListener(v -> showStepLengthSelectionDialog());
        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_8Activity.this, Personal_info_7Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Personal_info_8Activity.this, Set_goalsActivity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_8Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateStepLengthTextView(TextView stepLengthTextView) {
        if (selectedStepLength == 0 && selectedStepLengthUnits.equals("units")) {
            stepLengthTextView.setText("00 units");
        } else {
            stepLengthTextView.setText(selectedStepLength + " " + selectedStepLengthUnits);
        }
    }

    private void showStepLengthSelectionDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_step_length_picker, null);

        // Initialize Spinners
        Spinner stepLengthSpinner = dialogView.findViewById(R.id.stepLengthSpinner);
        Spinner stepLengthUnitsSpinner = dialogView.findViewById(R.id.stepLengthUnitsSpinner);

        ArrayAdapter<Integer> stepLengthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cmStepLengths);
        stepLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stepLengthSpinner.setAdapter(stepLengthAdapter);

        ArrayAdapter<String> stepLengthUnitsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stepLengthUnits);
        stepLengthUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stepLengthUnitsSpinner.setAdapter(stepLengthUnitsAdapter);

        // Set initial selections
        stepLengthSpinner.setSelection(cmStepLengths.indexOf(selectedStepLength));
        stepLengthUnitsSpinner.setSelection(findIndexOfUnit(selectedStepLengthUnits));

        // Handle step length selection
        stepLengthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedStepLengthInCm = cmStepLengths.get(position);
                if (selectedStepLengthUnits.equals("in")) {
                    selectedStepLength = (int) (selectedStepLengthInCm * 0.393701); // Convert cm to inches
                } else {
                    selectedStepLength = selectedStepLengthInCm; // cm is the default
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Handle units selection
        stepLengthUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newUnit = stepLengthUnits[position];
                if (newUnit.equals("cm")) {
                    selectedStepLength = (int) (selectedStepLength / 0.393701); // Convert inches to cm
                } else {
                    selectedStepLength = (int) (selectedStepLength * 0.393701); // Convert cm to inches
                }
                selectedStepLengthUnits = newUnit;
                updateStepLengthTextView(findViewById(R.id.stepLengthTextView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Show the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Update the step length display with selected values
                    updateStepLengthTextView(findViewById(R.id.stepLengthTextView));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Reset to default "00 units" if cancelled
                    selectedStepLength = 0;
                    selectedStepLengthUnits = "units";
                    updateStepLengthTextView(findViewById(R.id.stepLengthTextView));
                    dialog.dismiss();
                })
                .show();
    }

    private int findIndexOfUnit(String unit) {
        for (int i = 0; i < stepLengthUnits.length; i++) {
            if (stepLengthUnits[i].equals(unit)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }
}
