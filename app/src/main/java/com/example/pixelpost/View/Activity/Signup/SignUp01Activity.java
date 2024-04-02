package com.example.pixelpost.View.Activity.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.Contract.Activity.ISignUp01ActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.SignUp01ActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.ValidateData;

public class SignUp01Activity extends AppCompatActivity implements ISignUp01ActivityContract.View {
    Button btnContinue;
    ImageView btnBack;
    EditText editTextTextEmailAddress;
    ISignUp01ActivityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up01);
        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        editTextTextEmailAddress = this.findViewById(R.id.editTextTextEmailAddress);
        presenter = new SignUp01ActivityPresenter(this);
        setListener();
    }

    private void setListener()
    {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextTextEmailAddress.getText().toString();

                if(ValidateData.isValidEmail(email))
                {
                    presenter.checkEmail(email);
                }
                else{
                    Toast.makeText(SignUp01Activity.this, "Vui lòng kiểm tra lại địa chỉ email", Toast.LENGTH_SHORT).show();
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

    @Override
    public void isLegitEmail() {
        String email = editTextTextEmailAddress.getText().toString();

        Intent intent = new Intent(SignUp01Activity.this, OTPVerificationSignUpActivity.class);
        intent.putExtra(User.FIELD_EMAIL, email);
        startActivity(intent);
    }

    @Override
    public void isNotLegitEmail() {
        Toast.makeText(getApplicationContext(),"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkEmailFailed(Exception e) {
        Log.e("check-email-failed",e.getMessage());
        Toast.makeText(getApplicationContext(),"Lỗi đăng ký tài khoản", Toast.LENGTH_SHORT).show();
    }
}