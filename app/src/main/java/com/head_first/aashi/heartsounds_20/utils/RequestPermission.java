package com.head_first.aashi.heartsounds_20.utils;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



/**
 * Created by Aashish Indorewala on 03-Mar-17.
 */

public class RequestPermission {
    public static final int RECORD_AUDIO = 1;
    public static final int READ_EXTERNAL_STORAGE = 2;
    public static final int WRITE_EXTERNAL_STORAGE = 3;
    public static final int BLUETOOTH = 4;

    public static final boolean requestUserPermission(Activity activity, final String requestedPermission, final int requestCode){
        if(ContextCompat.checkSelfPermission(activity, requestedPermission) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, requestedPermission)){
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else{
                ActivityCompat.requestPermissions(activity, new String[] {requestedPermission}, requestCode);
            }
            return false;
        }
        return true;
    }

}
