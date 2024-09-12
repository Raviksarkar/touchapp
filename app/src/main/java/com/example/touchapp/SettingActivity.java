package com.example.touchapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private ImageView bluetoothStatusImage;
    private TextView bluetoothStatusText;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice connectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bluetoothStatusImage = findViewById(R.id.bluetooth_status_image);
        bluetoothStatusText = findViewById(R.id.bluetooth_status_text);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (checkBluetoothPermissions()) {
            // Register for Bluetooth connection state change broadcasts
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(bluetoothReceiver, filter);

            updateUI();
        } else {
            requestBluetoothPermissions();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @SuppressLint("ObsoleteSdkInt")
    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                    }, PERMISSION_REQUEST_CODE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateUI();
            } else {
                bluetoothStatusImage.setImageResource(R.drawable.bluetooth_not_connected);
                bluetoothStatusText.setText(R.string.permission_denied_message);
            }
        }
    }

    private void updateUI() {
        if (checkBluetoothPermissions()) {
            try {
                if (bluetoothAdapter.isEnabled() && connectedDevice != null) {
                    bluetoothStatusImage.setImageResource(R.drawable.bluetooth_connected);
                    bluetoothStatusText.setText(connectedDevice.getName());
                } else {
                    bluetoothStatusImage.setImageResource(R.drawable.bluetooth_not_connected);
                    bluetoothStatusText.setText(R.string.connect_smart_device);
                }
            } catch (SecurityException e) {
                bluetoothStatusImage.setImageResource(R.drawable.bluetooth_not_connected);
                bluetoothStatusText.setText(R.string.permission_denied_message);
            }
        }
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                connectedDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                updateUI();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                connectedDevice = null;
                updateUI();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }
}
