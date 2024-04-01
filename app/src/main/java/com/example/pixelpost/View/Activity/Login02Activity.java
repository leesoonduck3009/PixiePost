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

import com.example.pixelpost.Contract.Activity.ILogin2ActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.Login2ActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;

public class Login02Activity extends AppCompatActivity implements ILogin2ActivityContract.View {

    private PreferenceManager preferenceManager;
    private ILogin2ActivityContract.Presenter presenter;
    private ImageView btnBack;
    private Button btnForgotPassWord;
    private Button btnContinue;
    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login02);
        btnBack = findViewById(R.id.btnBack);
        btnForgotPassWord = findViewById(R.id.btnForgotPassWord);
        btnContinue = findViewById(R.id.btnContinue);
        passwordEditText = findViewById(R.id.passwordEditText);
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new Login2ActivityPresenter(this);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                presenter.LoginByEmail(intent.getStringExtra(User.FIELD_EMAIL),passwordEditText.getText().toString());
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
    @Override
    public void LoginSuccess(User user) {
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME,user);
        Intent intent = new Intent(Login02Activity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void LoginFailed(Exception e) {
        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}