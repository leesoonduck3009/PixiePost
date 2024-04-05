package com.example.pixelpost.View.Activity.ForgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.R;
import com.example.pixelpost.View.Activity.Login.Login01Activity;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        Button btnContinueAfterResetPass = findViewById(R.id.btnContinueAfterResetPass);
        btnContinueAfterResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, Login01Activity.class);
                startActivity(intent);
            }
        });
    }
}