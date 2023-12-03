package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.chattingapp.databinding.ActivitySplashScreenBinding;


public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        showAnimation();
        showMainActivity();













    }

    private void showAnimation() {
        //Applynig Animations on Icon
        Animation logoAnim= AnimationUtils.loadAnimation(SplashScreenActivity.this,R.anim.logo_comming);
        binding.splashscreenicon.startAnimation(logoAnim);
    }

    private void showMainActivity() {
        // Changing screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash_to_Signup=new Intent(SplashScreenActivity.this, phone_signup_activity.class);
                startActivity(splash_to_Signup);
                finish();
            }
        },10000);

    }

}