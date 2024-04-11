package com.example.pixelpost.View.Activity.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;

public class SignUp02Activity extends AppCompatActivity {
    ImageView btnBack;
    Button btnContinue;
    private boolean isPasswordVisible = false;

    EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up02);
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRightWidth = passwordEditText.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= (passwordEditText.getRight() - drawableRightWidth)) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_off, 0);
        } else {
            // Hiển thị mật khẩu
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_fill, 0);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}