package com.example.pixelpost.View.Activity.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.Contract.Activity.ISetUpProfileActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.SetUpProfileActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.MainActivity;

public class SetUpProfileActivity extends AppCompatActivity implements ISetUpProfileActivityContract.View {
    Button btnCreateAccount ;
    private EditText edtFirstName;
    private EditText edtLastName;
    private ISetUpProfileActivityContract.Presenter presenter;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_up_profile);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new SetUpProfileActivityPresenter(this);
        setListener();
    }
    private void setListener()
    {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onCreatingAccount(initUser());
            }
        });
    }
    private User initUser()
    {
        Intent intent = getIntent();
        String email = intent.getStringExtra(User.FIELD_EMAIL);
        String password = intent.getStringExtra(User.FIELD_PASSWORD);

        return new User.Builder().setEmail(email).setPassword(password).setFirstName(edtFirstName.getText().toString())
                .setLastName(edtLastName.getText().toString()).build();
    }

    @Override
    public void onLoadingFailed(Exception ex) {
        Log.e("create-account-failed", ex.getMessage());
        Toast.makeText(getApplicationContext(),"Gặp lỗi khi tạo tài khoản", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateAccountSuccess(User user) {
        Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME,user);
        Intent intent = new Intent(SetUpProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}