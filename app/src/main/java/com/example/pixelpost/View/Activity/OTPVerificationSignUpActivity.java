package com.example.pixelpost.View.Activity;

import static com.example.pixelpost.Utils.SupportClass.OTPGenerator.generateOTP;
import static com.example.pixelpost.Utils.SupportClass.OTPGenerator.isOTPValid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.EmailSending;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OTPVerificationSignUpActivity extends AppCompatActivity {

    private EditText inputCode1;
    private EditText inputCode2;
    private EditText inputCode3;
    private EditText inputCode4;
    String userEmail;
    private CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 60000; // 60 giây
    private static final long INTERVAL = 1000; // 1 giây


    Button btnVerify;
    String OTP = null;
    String OTPInput = "";
    TextView textResendOTP;
    boolean isEnableResend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification_sign_up);
        btnVerify = findViewById(R.id.btnVerify);
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        textResendOTP = findViewById(R.id.textResendOTP);
        getEmail();
        setListener();
    }
    private void getEmail()
    {
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("user_email");
        generateOTPAndSendMail();

    }
    private void setListener()
    {
        //region set Input text change
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nếu chiều dài của nội dung trong ô nhập liệu là 1 ký tự
                if (editable.length() == 1) {
                    // Chuyển focus tới ô nhập liệu tiếp theo (inputCode4)
                    inputCode2.requestFocus();
                }
            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nếu chiều dài của nội dung trong ô nhập liệu là 1 ký tự
                if (editable.length() == 1) {
                    // Chuyển focus tới ô nhập liệu tiếp theo (inputCode4)
                    inputCode3.requestFocus();
                }
            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nếu chiều dài của nội dung trong ô nhập liệu là 1 ký tự
                if (editable.length() == 1) {
                    // Chuyển focus tới ô nhập liệu tiếp theo (inputCode4)
                    inputCode4.requestFocus();
                }
            }
        });

// Thiết lập sự kiện nghe cho ô nhập liệu thứ 4
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nếu chiều dài của nội dung trong ô nhập liệu là 1 ký tự
                if (editable.length() == 1) {
                    // Ẩn bàn phím ảo sau khi đã nhập xong
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputCode4.getWindowToken(), 0);
                    inputCode4.clearFocus();
                }
            }
        });
        //endregion
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTPInput = getOTP();
                if(OTPInput == null)
                {
                    inputCode1.setText("");
                    inputCode2.setText("");
                    inputCode3.setText("");
                    inputCode4.setText("");
                    return;
                }
                if(isOTPValid(OTPInput,OTP))
                {
                    Intent intent = new Intent(OTPVerificationSignUpActivity.this, SignUp02Activity.class);
                    intent.putExtra("user_email",userEmail);
                    startActivity(intent);
                }
                else
                {
                    inputCode1.setText("");
                    inputCode2.setText("");
                    inputCode3.setText("");
                    inputCode4.setText("");
                    Toast.makeText(getApplicationContext(),"Mã OTP không hợp lệ",Toast.LENGTH_SHORT).show();
                }

            }
        });
        textResendOTP.setOnClickListener(v -> {
            if(isEnableResend)
            {
                generateOTPAndSendMail();
                Toast.makeText(getApplicationContext(),"Đã gửi lại thông tin mã OTP",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getOTP()
    {
        StringBuilder otp = new StringBuilder();
        if(inputCode1.getText().toString().isEmpty() || inputCode2.getText().toString().isEmpty()
                ||inputCode3.getText().toString().isEmpty() ||inputCode4.getText().toString().isEmpty())
        {
            return null;
        }
        otp.append(inputCode1.getText().toString());
        otp.append(inputCode2.getText().toString());
        otp.append(inputCode3.getText().toString());
        otp.append(inputCode4.getText().toString());

        return otp.toString();
    }
    private void generateOTPAndSendMail()
    {
        OTP = generateOTP();
        EmailSending.sendMail(userEmail,OTP, "OTP xác thực đăng ký tài khoản - Pixel Post", new EmailSending.OnFinishSendingEmailListener() {
            @Override
            public void onFinishSendEmail(Exception e) {
                if(e!=null)
                {
                    Toast.makeText(getApplicationContext(),"Gửi mã OTP thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimer();

    }
    private void startTimer() {
        isEnableResend = false;
        countDownTimer = new CountDownTimer(TOTAL_TIME, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                textResendOTP.setText(String.format("Gửi lại trong %02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
            }
            @Override
            public void onFinish() {
                textResendOTP.setText("Gửi lại");
                isEnableResend = true;
                // Xử lý khi hết thời gian đếm ngược
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng đếm ngược khi activity bị hủy
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        OTP = null;
    }
}