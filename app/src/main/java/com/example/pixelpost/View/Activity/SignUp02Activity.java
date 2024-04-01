package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;

public class SignUp02Activity extends AppCompatActivity {
    ImageView btnBack;
    Button btnContinue;
    EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up02);
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        passwordEditText = findViewById(R.id.passwordEditText);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordEditText.getText().toString().length()>=8)
                {
                    Intent intent = new Intent(SignUp02Activity.this, SetUpProfileActivity.class);
                    intent.putExtra(User.FIELD_EMAIL,getIntent().getStringExtra(User.FIELD_EMAIL));
                    intent.putExtra(User.FIELD_PASSWORD,passwordEditText.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password không hợp lệ",Toast.LENGTH_SHORT).show();;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}