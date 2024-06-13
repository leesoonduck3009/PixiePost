package com.example.pixelpost.View.Activity;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Contract.Activity.IUpdateUserInformationActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.UpdateUserInformationActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.Utils.SupportClass.UriToByte;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class UpdateUserInformationActivity extends AppCompatActivity implements IUpdateUserInformationActivityContract.View {
    private ImageView avatarImg;
    private ImageView btnUpdateAvatar;
    private EditText edtLastName;
    private EditText edtFirstName;
    private EditText edtEmail;
    private EditText edtPhoneNumber;
    private boolean isCanChange = false;
    private Button btnSave;
    private User currentUser;
    private ProgressBar progressBar;
    private ImageView btnBack;
    private IUpdateUserInformationActivityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user_information);
        avatarImg = findViewById(R.id.avatarImg);
        btnUpdateAvatar = findViewById(R.id.btnUpdateAvatar);
        edtLastName = findViewById(R.id.edtLastName);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        presenter = new UpdateUserInformationActivityPresenter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            avatarImg.setClipToOutline(true);
            avatarImg.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        }

        setDisableBtnSave();
        loadUser();
        setListener();
    }
    private void loadUser(){
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        currentUser = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
        edtFirstName.setText(currentUser.getFirstName());
        edtLastName.setText(currentUser.getLastName());
        edtEmail.setText(currentUser.getEmail());
        if(currentUser.getAvatarUrl()!=null){
            Glide.with(getApplicationContext()).load(currentUser.getAvatarUrl()).into(avatarImg);
        }
        else {
            Glide.with(getApplicationContext()).load(R.drawable.avatar3).into(avatarImg);
        }
    }
    private void setListener(){
        btnBack.setOnClickListener(v -> {
            this.finish();
        });
        edtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDisable();
            }
        });
        edtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDisable();
            }
        });
        btnSave.setOnClickListener(v -> {
            currentUser.setFirstName(edtFirstName.getText().toString());
            currentUser.setLastName(edtLastName.getText().toString());
            presenter.updateInformation(currentUser);
            showLoading(true);
        });
        btnUpdateAvatar.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

    }
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnSave.setText("");
            btnSave.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnSave.setText("Lưu");
            btnSave.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }
    private void checkDisable(){
        if(edtLastName.getText().toString().equals(currentUser.getLastName()) && edtFirstName.getText().toString().equals(currentUser.getFirstName())){
            setDisableBtnSave();
        }
        else{
            setEnableBtnSave();
        }
    }
    private void setDisableBtnSave(){
        btnSave.setEnabled(false);
        btnSave.setBackgroundResource(R.drawable.disable_button);
    }
    private void setEnableBtnSave(){
        btnSave.setEnabled(true);
        btnSave.setBackgroundResource(R.drawable.custom_button);
    }
    @Override
    public void updateInformationSuccess(User user) {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME, user);
        Toast.makeText(getApplicationContext(), "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
        showLoading(false);
        checkDisable();
    }
    @Override
    public void uploadAvatarSuccess(User user){
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME,user);
        this.currentUser = user;
        Glide.with(getApplicationContext()).load(currentUser.getAvatarUrl()).into(avatarImg);
    }

    @Override
    public void loadingFailed(Exception e) {
        Log.e("UpdateUserInformation", Objects.requireNonNull(e.getMessage()));
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new
                    ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        byte [] image = UriToByte.convertUriToByteArray(getContentResolver(),uri);
                        presenter.uploadAvatar(image,currentUser);
                    } catch (Exception e) {
                        Log.e("RuntimeException", e.getMessage());
                        throw new RuntimeException(e);

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
}