package com.changeyourself.KGBlood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    protected ImageView imageViewLogo;
    protected TextView textViewnameapp;
    protected TextView textViewgreetings;
    private Animation imageanimation;
    private Animation textanimations;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewLogo = findViewById(R.id.imagelogo);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewnameapp = findViewById(R.id.textviewLogo);
        textViewgreetings = findViewById(R.id.textviewGreetings);
        imageanimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        textanimations = AnimationUtils.loadAnimation(this, R.anim.animationforthetext);
        imageViewLogo.setAnimation(imageanimation);
        textViewnameapp.setAnimation(textanimations);
        textViewgreetings.setAnimation(textanimations);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }
}
