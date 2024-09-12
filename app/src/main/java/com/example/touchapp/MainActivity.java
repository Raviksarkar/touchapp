package com.example.touchapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.touchapp.login.set_goals.Set_goals_1Activity;
import com.example.touchapp.login.set_goals.Set_goals_2Activity;
import com.example.touchapp.login.set_goals.Set_goals_3Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private TextView statusTextView;
    private TextView batteryPercentageTextView;
    private TextView currentDayTextView;
    private TextView currentDateTextView;
    private TextView stepsGoalAchievedTextView;
    private TextView distanceGoalAchievedTextView;
    private TextView caloriesGoalAchievedTextView;
    private ProgressBar stepsProgressBar;
    private ProgressBar distanceProgressBar;
    private ProgressBar caloriesProgressBar;
    private BluetoothAdapter bluetoothAdapter;
    private Calendar calendar;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
    private static final String SMARTWATCH_MAC_ADDRESS = "00:11:22:33:44:55"; // Replace with your smartwatch MAC address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton navButton4 = findViewById(R.id.nav_button_4);
        navButton4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        statusTextView = findViewById(R.id.status_text_view);
        batteryPercentageTextView = findViewById(R.id.battery_percentage_text_view);
        currentDayTextView = findViewById(R.id.current_day);
        currentDateTextView = findViewById(R.id.current_date);
        stepsGoalAchievedTextView = findViewById(R.id.steps_goal_achieved);
        distanceGoalAchievedTextView = findViewById(R.id.distance_goal_achieved);
        caloriesGoalAchievedTextView = findViewById(R.id.calories_goal_achieved);
        stepsProgressBar = findViewById(R.id.steps_progress);
        distanceProgressBar = findViewById(R.id.distance_progress);
        caloriesProgressBar = findViewById(R.id.calories_progress);
        calendar = Calendar.getInstance();
        updateDateDisplay();
        if (arePermissionsGranted()) {
            initializeBluetooth();
        } else {
            requestPermissions();
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bluetoothReceiver, filter);
        FrameLayout firstCircle = findViewById(R.id.first_circle);
        FrameLayout secondCircle = findViewById(R.id.second_circle);
        FrameLayout thirdCircle = findViewById(R.id.third_circle);
        firstCircle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Set_goals_1Activity.class);
            startActivity(intent);
        });
        secondCircle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Set_goals_2Activity.class);
            startActivity(intent);
        });

        thirdCircle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Set_goals_3Activity.class);
            startActivity(intent);
        });
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton forwardButton = findViewById(R.id.forward_button);
        backButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
        });
        forwardButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateDisplay();
        });
    }

    private boolean arePermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void initializeBluetooth() {
        try {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            }
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                checkSmartwatchConnection();
            } else {
                statusTextView.setText(R.string.not_connected);
                batteryPercentageTextView.setText(R.string.battery_watch);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Bluetooth initialization failed", e);
            statusTextView.setText(R.string.not_connected);
            batteryPercentageTextView.setText(R.string.battery_watch);
        }
    }

    private void checkSmartwatchConnection() {
        if (bluetoothAdapter == null) return;
        if (!arePermissionsGranted()) {
            statusTextView.setText(R.string.not_connected);
            batteryPercentageTextView.setText(R.string.battery_watch);
            return;
        }
        try {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (SMARTWATCH_MAC_ADDRESS.equals(device.getAddress())) {
                    statusTextView.setText(R.string.connected);
                    updateSmartwatchData();
                    return;
                }
            }
            statusTextView.setText(R.string.not_connected);
            batteryPercentageTextView.setText(R.string.battery_watch);
        } catch (SecurityException e) {
            Log.e(TAG, "Error checking smartwatch connection", e);
            statusTextView.setText(R.string.not_connected);
            batteryPercentageTextView.setText(R.string.battery_watch);
        }
    }

    private void updateSmartwatchData() {
        try {
            int stepsAchieved = 5000;
            double distanceAchieved = 3.5;
            int caloriesBurned = 1200;
            stepsGoalAchievedTextView.setText(String.valueOf(stepsAchieved));
            distanceGoalAchievedTextView.setText(String.format(Locale.getDefault(), "%.2f km", distanceAchieved));
            caloriesGoalAchievedTextView.setText(String.valueOf(caloriesBurned));
            updateProgressBars(stepsAchieved, distanceAchieved, caloriesBurned);
        } catch (Exception e) {
            Log.e(TAG, "Error updating smartwatch data", e);
        }
    }

    private void updateProgressBars(int steps, double distance, int calories) {
        try {
            int dailyStepsGoal = 10000;
            double distanceGoal = 5.0;
            int caloriesGoal = 2000;
            int stepsProgress = (steps * 100) / dailyStepsGoal;
            int distanceProgress = (int) ((distance * 100) / distanceGoal);
            int caloriesProgress = (calories * 100) / caloriesGoal;
            setProgressBarColor(stepsProgressBar, stepsProgress);
            setProgressBarColor(distanceProgressBar, distanceProgress);
            setProgressBarColor(caloriesProgressBar, caloriesProgress);
        } catch (Exception e) {
            Log.e(TAG, "Error updating progress bars", e);
        }
    }

    private void setProgressBarColor(ProgressBar progressBar, int progress) {
        if (progress == 100) {
            progressBar.setProgressDrawable(AppCompatResources.getDrawable(this, R.drawable.progress_completed));
        } else {
            progressBar.setProgressDrawable(AppCompatResources.getDrawable(this, R.drawable.progress_pending));
        }
        progressBar.setProgress(progress);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                checkSmartwatchConnection();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                statusTextView.setText(R.string.not_connected);
                batteryPercentageTextView.setText(R.string.battery_watch);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeBluetooth();
            } else {
                statusTextView.setText(R.string.not_connected);
                batteryPercentageTextView.setText(R.string.battery_watch);
            }
        }
    }

    private void updateDateDisplay() {
        String day = dayFormat.format(calendar.getTime());
        String date = dateFormat.format(calendar.getTime());
        currentDayTextView.setText(day);
        currentDateTextView.setText(date);
    }
}
