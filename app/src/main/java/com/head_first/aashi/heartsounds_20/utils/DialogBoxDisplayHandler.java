package com.head_first.aashi.heartsounds_20.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;

import com.head_first.aashi.heartsounds_20.R;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public class DialogBoxDisplayHandler {
    private static ProgressDialog progressDialog;
    private static String message;
    private static Activity activity;

    //---------------------------------------------------
    //Progress Dialog Box methods
    public static void showIndefiniteProgressDialog(final Activity activity){
        if(activity != null){
            DialogBoxDisplayHandler.activity = activity;
            DialogBoxDisplayHandler.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(DialogBoxDisplayHandler.activity);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setContentView(R.layout.dialog_indeterminate_progress_bar);
                }
            });
        }
    }

    public static void showIndefiniteProgressDialog(Activity activity, String message){
        if(activity != null && message != null){
            DialogBoxDisplayHandler.message = message;
            DialogBoxDisplayHandler.activity = activity;
            DialogBoxDisplayHandler.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(DialogBoxDisplayHandler.activity);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    if(DialogBoxDisplayHandler.message != null){
                        progressDialog.setMessage(DialogBoxDisplayHandler.message);
                    }
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                }
            });
        }
    }

    public static void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
    //---------------------------------------------------
    //Alert Dialog Box methods
    public static void displayInformationAlertDialogBox(Activity activity, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    //---------------------------------------------------

}
