package com.example.pixelpost.View.Activity;

import static com.example.pixelpost.Utils.SupportClass.EmailSending.sendMail;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.ValidateData;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignUp01Activity extends AppCompatActivity {
    Button btnContinue;
    ImageView btnBack;
    EditText editTextTextEmailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up01);
        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        editTextTextEmailAddress = this.findViewById(R.id.editTextTextEmailAddress);
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
                    Intent intent = new Intent(SignUp01Activity.this, OTPVerificationSignUpActivity.class);
                    intent.putExtra("user_email", email);
                    startActivity(intent);
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
}