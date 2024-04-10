package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.R;
import com.google.firebase.auth.FirebaseAuth;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null)
                    startActivity(new Intent(AnimationActivity.this, OnboardingActivity.class));
                else
                    startActivity(new Intent(AnimationActivity.this, MainActivity.class));
                finish();
            }
        },3000);
    }
}