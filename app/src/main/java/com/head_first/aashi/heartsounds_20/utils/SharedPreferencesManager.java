package com.head_first.aashi.heartsounds_20.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.head_first.aashi.heartsounds_20.HomePage;

/**
 * Created by Aashish Indorewala on 03-Apr-17.
 */

public class SharedPreferencesManager {
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private static final String ACTIVE_USERNAME = "ACTIVE_USERNAME";
    private static final String ACTIVE_USER_ID = "ACTIVE_USER_ID";

    /*
    When user logs in,
    1. Create an entry for userName to accessToken
    2. Create an entry for "Active user" to userName.

    When a user logs out,
    1. Delete the entry for userName to accessToken
    2. Map the entry for "Active user" to null.
     */



    public static void addUsernameAndTokenToSharedPreferences(Activity activity, String username, String accessToken){
        if(username == null || accessToken == null || activity == null){
            return;
        }
        SharedPreferences.Editor sharedPrederencesEditor = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit();
        sharedPrederencesEditor.putString(ACTIVE_USERNAME, username);
        sharedPrederencesEditor.putString(username,accessToken);
        sharedPrederencesEditor.commit();
    }

    public static void addUserIdToSharedPreferences(Activity activity, String username, String userId){
        if(username == null || userId == null || activity == null){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        if(username.equalsIgnoreCase(sharedPreferences.getString(ACTIVE_USERNAME, ""))){
            SharedPreferences.Editor sharedPrederencesEditor = sharedPreferences.edit();
            sharedPrederencesEditor.putString(ACTIVE_USER_ID, userId);
            sharedPrederencesEditor.commit();
        }
    }

    public static void invalidateUserAccessToken(Activity activity){
        if(activity == null){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String username = null;
        if((username = sharedPreferences.getString(ACTIVE_USERNAME, "")) != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(username);
            editor.remove(ACTIVE_USERNAME);
            editor.commit();
            //Ideally this code should not be here
            Intent homePage = new Intent(activity, HomePage.class);
            activity.startActivity(homePage);
        }
    }

    public static String getUserAccessToken(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String activeUser = sharedPreferences.getString(ACTIVE_USERNAME, "");
        if(activeUser == null){
            return null;
        }
        return sharedPreferences.getString(activeUser, "");
    }

    public static String getActiveUserId(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String activeUser = sharedPreferences.getString(ACTIVE_USERNAME, "");
        if(activeUser == null){
            return null;
        }
        return sharedPreferences.getString(ACTIVE_USER_ID, "");
    }
}
