package com.example.pixelpost.View.Activity.ForgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.R;

public class OTPVerificationForgorPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification_forgor_password);
        Button btnVerifyForgotPass = findViewById(R.id.btnVerifyForgotPass);
        btnVerifyForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPVerificationForgorPasswordActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}