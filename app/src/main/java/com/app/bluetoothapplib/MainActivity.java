package com.app.bluetoothapplib;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.DeviceCallback;

public class MainActivity extends AppCompatActivity {

    Bluetooth bluetooth;
    private final static int REQUEST_ENABLE_BT = 1111;
    private final static String ADDRR = "@ MAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = new Bluetooth(this);

        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override public void onDeviceConnected(BluetoothDevice device) {

            }
            @Override public void onDeviceDisconnected(BluetoothDevice device, String message) {}
            @Override public void onMessage(byte[] message) {
                String s = new String(message);
                Log.d("Recieved Msg ", s);
            }
            @Override public void onError(int errorCode) {}
            @Override public void onConnectError(BluetoothDevice device, String message) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        if(bluetooth.isEnabled()){
            bluetooth.connectToAddress(ADDRR);
        } else {
            bluetooth.showEnableDialog(MainActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetooth.onActivityResult(requestCode, resultCode);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    bluetooth.connectToAddress(ADDRR);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bluetooth.isConnected()){
            bluetooth.disconnect();
        }
    }

    public void sendMessage(String msg){
        bluetooth.send(msg);
    }

}