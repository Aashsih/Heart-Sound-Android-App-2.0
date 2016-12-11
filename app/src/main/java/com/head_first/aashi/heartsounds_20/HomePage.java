package com.head_first.aashi.heartsounds_20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_1);
        ImageView testGIF = (ImageView)findViewById(R.id.loadingScreen);
        Glide.with(this).load(R.drawable.loading_screen_1).asGif().crossFade().into(testGIF);
    }

}
