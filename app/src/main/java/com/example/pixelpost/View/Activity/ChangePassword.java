package com.example.pixelpost.View.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Contract.Activity.IChangePasswordActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.ChangePasswordActivityPresenter;
import com.example.pixelpost.Presenter.UpdateUserInformationActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;

public class ChangePassword extends AppCompatActivity implements IChangePasswordActivityContract.View {
    private ImageView btnBack;
    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtNewPasswordAgain;
    private Button btnSave;
    private User currentUser;
    private boolean isPasswordVisible = false;

    private IChangePasswordActivityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtNewPasswordAgain = findViewById(R.id.edtNewPasswordAgain);
        presenter = new ChangePasswordActivityPresenter(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUser();
        setListener();



    }

    private void loadUser(){
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        currentUser = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);

    }
    private void setListener(){
        btnBack.setOnClickListener(v -> {
            this.finish();
        });

        btnSave.setOnClickListener(v -> {
            String oldPassword = edtOldPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String newPasswordAgain = edtNewPasswordAgain.getText().toString();

            if (!newPassword.equals(newPasswordAgain)) {
                Toast.makeText(ChangePassword.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.changePassword(currentUser, oldPassword, newPassword);
        });
        edtOldPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRightWidth = edtOldPassword.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= (edtOldPassword.getRight() - drawableRightWidth)) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void changePasswordSuccess() {
        Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void changePasswordFailed(Exception e) {
        Toast.makeText(ChangePassword.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            edtOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtOldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_off, 0);
        } else {
            // Hiển thị mật khẩu
            edtOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtOldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_fill, 0);
        }
        edtOldPassword.setSelection(edtOldPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}