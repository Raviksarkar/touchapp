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

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class Personal_info_6Activity extends AppCompatActivity {
    ImageButton btn_p;
    ImageButton btn_n;
    private int selectedHeight = 0; // Default height
    private String selectedHeightUnits = "units"; // Default unit
    private final String[] heightUnits = {"cm", "in"};
    private final List<Integer> cmHeights = new ArrayList<>();
    private final List<Double> inHeights = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info6);

        for (int i = 100; i <= 250; i++) {
            cmHeights.add(i);
            inHeights.add(i * 0.393701);
        }

        TextView heightTextView = findViewById(R.id.heightTextView);
        Button selectButton = findViewById(R.id.selectButton);

        updateHeightTextView(heightTextView);
        selectButton.setOnClickListener(v -> showHeightSelectionDialog());

        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_6Activity.this, Personal_info_5Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Personal_info_6Activity.this, Personal_info_7Activity.class)));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_6Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateHeightTextView(TextView heightTextView) {
        if (selectedHeight == 0 && selectedHeightUnits.equals("units")) {
            heightTextView.setText("00 units");
        } else {
            heightTextView.setText(selectedHeight + " " + selectedHeightUnits);
        }
    }

    private void showHeightSelectionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_height_picker, null);

        Spinner heightSpinner = dialogView.findViewById(R.id.heightSpinner);
        Spinner heightUnitsSpinner = dialogView.findViewById(R.id.heightUnitsSpinner);

        ArrayAdapter<Integer> heightAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cmHeights);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);

        ArrayAdapter<String> heightUnitsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, heightUnits);
        heightUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightUnitsSpinner.setAdapter(heightUnitsAdapter);

        heightSpinner.setSelection(cmHeights.indexOf(selectedHeight));
        heightUnitsSpinner.setSelection(findIndexOfUnit(selectedHeightUnits));

        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedHeightInCm = cmHeights.get(position);
                if (selectedHeightUnits.equals("in")) {
                    selectedHeight = (int) (selectedHeightInCm * 0.393701);
                } else {
                    selectedHeight = selectedHeightInCm;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        heightUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newUnit = heightUnits[position];
                if (newUnit.equals("cm")) {
                    selectedHeight = (int) (selectedHeight / 0.393701);
                } else {
                    selectedHeight = (int) (selectedHeight * 0.393701);
                }
                selectedHeightUnits = newUnit;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    updateHeightTextView(findViewById(R.id.heightTextView));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    selectedHeight = 0;
                    selectedHeightUnits = "units";
                    updateHeightTextView(findViewById(R.id.heightTextView));
                    dialog.dismiss();
                })
                .show();
    }

    private int findIndexOfUnit(String unit) {
        for (int i = 0; i < heightUnits.length; i++) {
            if (heightUnits[i].equals(unit)) {
                return i;
            }
        }
        return 0;
    }
}
