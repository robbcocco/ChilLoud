package com.starkoverflow.chilloud.Main;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.starkoverflow.chilloud.R;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ImageView splashscreenView = (ImageView) findViewById(R.id.splashscreen);
        AnimatedVectorDrawable splashscreen = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_splashscreen);
        splashscreenView.setImageDrawable(splashscreen);
        splashscreen.start();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                finish();

            }
        },1000);
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }
}
