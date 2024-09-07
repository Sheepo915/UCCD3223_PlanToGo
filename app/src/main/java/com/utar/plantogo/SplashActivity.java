package com.utar.plantogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView splashImage = findViewById(R.id.Splash_Image);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        // Set animation listeners
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Nothing to do here
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashImage.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Nothing to do here
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Nothing to do here
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Move to the main activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash activity
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Nothing to do here
            }
        });

        // Start the animation
        splashImage.startAnimation(fadeIn);
    }
}
