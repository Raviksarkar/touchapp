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
public class Personal_info_5Activity extends AppCompatActivity {
    private final String[] units = {"kgs", "lbs"};
    private final List<Integer> kgs = new ArrayList<>();
    private final List<Double> lbs = new ArrayList<>();
    ImageButton btn_p;
    ImageButton btn_n;
    private int selectedWeight = 0;
    private String selectedUnits = "units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info5);
        for (int i = 20; i <= 250; i++) {
            kgs.add(i);
            lbs.add(i * 2.20462);
        }
        TextView weightTextView = findViewById(R.id.weightTextView);
        Button selectButton = findViewById(R.id.selectButton);
        updateWeightTextView(weightTextView);
        selectButton.setOnClickListener(v -> showSelectionDialog());
        btn_p = findViewById(R.id.button_prev);
        btn_p.setOnClickListener(v -> startActivity(new Intent(Personal_info_5Activity.this, Personal_info_4Activity.class)));
        btn_n = findViewById(R.id.button_next);
        btn_n.setOnClickListener(v -> startActivity(new Intent(Personal_info_5Activity.this, Personal_info_6Activity.class)));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Personal_info_5Activity.this, PermissionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateWeightTextView(TextView weightTextView) {
        if (selectedWeight == 0 && selectedUnits.equals("units")) {
            weightTextView.setText("00 units");
        } else {
            weightTextView.setText(selectedWeight + " " + selectedUnits);
        }
    }

    private void showSelectionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.coustomize, null);
        Spinner weightSpinner = dialogView.findViewById(R.id.weightSpinner);
        Spinner unitsSpinner = dialogView.findViewById(R.id.unitsSpinner);
        ArrayAdapter<Integer> weightAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kgs);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitsSpinner.setAdapter(unitsAdapter);
        weightSpinner.setSelection(kgs.indexOf(selectedWeight));
        unitsSpinner.setSelection(findIndexOfUnit(selectedUnits));
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedWeightInKgs = kgs.get(position);
                if (selectedUnits.equals("lbs")) {
                    selectedWeight = (int) (selectedWeightInKgs * 2.20462);
                } else {
                    selectedWeight = selectedWeightInKgs;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newUnit = units[position];
                if (newUnit.equals("kgs")) {
                    selectedWeight = (int) (selectedWeight / 2.20462);
                } else {
                    selectedWeight = (int) (selectedWeight * 2.20462);
                }
                selectedUnits = newUnit;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    updateWeightTextView(findViewById(R.id.weightTextView));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    selectedWeight = 0;
                    selectedUnits = "units";
                    updateWeightTextView(findViewById(R.id.weightTextView));
                    dialog.dismiss();
                })
                .show();
    }

    private int findIndexOfUnit(String unit) {
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(unit)) {
                return i;
            }
        }
        return 0;
    }
}
