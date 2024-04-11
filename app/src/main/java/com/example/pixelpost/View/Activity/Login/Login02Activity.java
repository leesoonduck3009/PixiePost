package com.example.pixelpost.View.Activity.Login;

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

import com.example.pixelpost.Contract.Activity.ILogin2ActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.Login2ActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.MainActivity;
import com.example.pixelpost.View.Activity.ForgotPassword.OTPVerificationForgorPasswordActivity;

public class Login02Activity extends AppCompatActivity implements ILogin2ActivityContract.View {

    private PreferenceManager preferenceManager;
    private ILogin2ActivityContract.Presenter presenter;
    private ImageView btnBack;
    private Button btnForgotPassWord;
    private Button btnContinue;
    private EditText passwordEditText;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login02);
        btnBack = findViewById(R.id.btnBack);
        btnForgotPassWord = findViewById(R.id.btnForgotPassWord);
        btnContinue = findViewById(R.id.btnContinue);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new Login2ActivityPresenter(this);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                presenter.LoginByEmail(intent.getStringExtra(User.FIELD_EMAIL),passwordEditText.getText().toString());
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login02Activity.this, OTPVerificationForgorPasswordActivity.class);
                startActivity(intent);
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

    @Override
    public void LoginSuccess(User user) {
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME,user);
        Intent intent = new Intent(Login02Activity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void LoginFailed(Exception e) {
        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}