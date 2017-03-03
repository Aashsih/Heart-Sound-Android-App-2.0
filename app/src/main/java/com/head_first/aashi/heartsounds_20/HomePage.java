package com.head_first.aashi.heartsounds_20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;
import com.head_first.aashi.heartsounds_20.controller.fragment.LoginFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.MurmerRatingFragment;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, loginFragment, LoginFragment.LOGIN_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

}
