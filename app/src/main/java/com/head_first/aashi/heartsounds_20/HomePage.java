package com.head_first.aashi.heartsounds_20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_1);
        ImageView testGIF = (ImageView)findViewById(R.id.loadingScreen);
        Glide.with(this).load(R.drawable.loading_screen_1).asGif().crossFade().into(testGIF);

        //Navigate to heart_sound_ui_experiments experiment
        Intent heartSoundMainActivityIntent = new Intent(this, UserPatientActivity.class);
        startActivity(heartSoundMainActivityIntent);
    }

}
