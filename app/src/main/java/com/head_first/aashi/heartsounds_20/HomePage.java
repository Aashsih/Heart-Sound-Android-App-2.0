package com.head_first.aashi.heartsounds_20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;
import com.head_first.aashi.heartsounds_20.controller.fragment.LoginFragment;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        String token = null;
        if((token = SharedPreferencesManager.getUserAccessToken(this)) == null || token.isEmpty()){
            LoginFragment loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, loginFragment, LoginFragment.LOGIN_FRAGMENT)
                    .commit();
        }
        else{
            Intent heartSoundMainActivityIntent = new Intent(this, UserPatientActivity.class);
            startActivity(heartSoundMainActivityIntent);
        }

    }

}
