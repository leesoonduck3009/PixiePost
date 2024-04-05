package com.example.pixelpost.View.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.hbb20.CountryCodePicker;

public class Login01Activity extends AppCompatActivity{
    private EditText emailEditText;
    private boolean isEmailLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login01);
        isEmailLayout = false;
        CountryCodePicker countryCodePicker = findViewById(R.id.countryCodePicker);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        String countryCode = countryCodePicker.getSelectedCountryCode();
        String phoneNumber = phoneNumberEditText.getText().toString();
        emailEditText = findViewById(R.id.emailEditText);
        Button button = findViewById(R.id.button);
        Button btnContinue = findViewById(R.id.btnContinue);
        ImageView btnBack = findViewById(R.id.btnBack);
        final LinearLayout phoneNumberLayout = findViewById(R.id.phoneNumberLayout);
        final LinearLayout emailLayout = findViewById(R.id.emailLayout);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final TextView txtHeader = findViewById(R.id.txtHeader);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumberLayout.getVisibility() == View.VISIBLE) {
                    isEmailLayout = true;
                    phoneNumberLayout.setVisibility(View.GONE);
                    emailLayout.setVisibility(View.VISIBLE);
                    txtHeader.setText("Email của bạn là gì?");
                    button.setText("Sử dụng Số điện thoại cho cách này");
                } else {
                    isEmailLayout = false;
                    phoneNumberLayout.setVisibility(View.VISIBLE);
                    emailLayout.setVisibility(View.GONE);
                    txtHeader.setText("Số điện thoại của bạn là gì?");
                    button.setText("Sử dụng Email cho cách này");
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmailLayout)
                {
                    String email = emailEditText.getText().toString();
                    Intent intent = new Intent(Login01Activity.this, Login02Activity.class);
                    intent.putExtra(User.FIELD_EMAIL,email);
                    startActivity(intent);
                }

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}