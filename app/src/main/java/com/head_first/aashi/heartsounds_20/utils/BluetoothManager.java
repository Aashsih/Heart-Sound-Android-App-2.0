package com.head_first.aashi.heartsounds_20.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Aashish Indorewala on 04-Mar-17.
 */

public class BluetoothManager {
    private static boolean bluetoothOn;
    private static List<BluetoothDevice> pairedDevices = new ArrayList<>();
    private static BluetoothDevice bluetoothDevice;
    private static BluetoothClass bluetoothClass;
    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public static boolean turnBluetoothOn(Activity activity){
        if(!bluetoothAdapter.isEnabled()){
            if(RequestPermission.requestUserPermission(activity, Manifest.permission.BLUETOOTH, RequestPermission.BLUETOOTH)
                    && RequestPermission.requestUserPermission(activity, Manifest.permission.BLUETOOTH_ADMIN, RequestPermission.BLUETOOTH_ADMIN)){
                bluetoothAdapter.enable();
            }
            else{
                Toast.makeText(activity.getBaseContext(), "Bluetooth Could not be turned on as permission was denied", Toast.LENGTH_SHORT);
            }
        }
        return bluetoothAdapter.isEnabled();
    }

    public static Set<BluetoothDevice> getPairedBluetoothDevices(){
        if(bluetoothAdapter != null)
            return bluetoothAdapter.getBondedDevices();
        return null;
    }

}
