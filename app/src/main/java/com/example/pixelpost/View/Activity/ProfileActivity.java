package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Contract.Activity.IProfileActivityContract;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.ProfileActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.Login.Login01Activity;
import com.example.pixelpost.View.Activity.QR.QRProfileActivity;
import com.example.pixelpost.View.Dialog.CustomDialog;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity implements IProfileActivityContract.View {
    LinearLayout btnUpdateUserProfile;

    LinearLayout btnChangePassword;

    LinearLayout btnWidgetGuide;
    LinearLayout btnReportIssue;
    private LinearLayout btnLogout;
    private LinearLayout btnQRCode;
    private IProfileActivityContract.Presenter presenter;
    private TextView txtViewName;
    private User currentUser;
    BottomSheetDialog reportIssueDialog;
    private ShapeableImageView avatarImg;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        presenter = new ProfileActivityPresenter(this);
        btnUpdateUserProfile = findViewById(R.id.btnUpdateUserProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnWidgetGuide = findViewById(R.id.btnWidgetGuide);

        btnReportIssue = findViewById(R.id.btnReportIssue);
        btnLogout = findViewById(R.id.btnLogout);
        reportIssueDialog = new BottomSheetDialog(this);
        txtViewName = findViewById(R.id.txtViewName);
        btnBack = findViewById(R.id.btnBack);
        avatarImg = findViewById(R.id.avatarImg);
        btnQRCode = findViewById(R.id.btnQRCode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            avatarImg.setClipToOutline(true);
            avatarImg.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        }
        createDialog();
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        User user = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
//        FriendRequestDialog.showDialog(this, user, FriendRequestDialog.FriendRequestDialogType.IS_FRIEND, new FriendRequestDialog.DialogClickListener() {
//            @Override
//            public void onAcceptFriendClick() {
//            }
//        });

        btnUpdateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateUserInformationActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(v -> {
            this.finish();
        });
        btnQRCode.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), QRProfileActivity.class);
            startActivity(intent);
        });
        btnReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportIssueDialog.show();
            }
        });

        btnWidgetGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WidgetGuilds.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        reportIssueDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        btnLogout.setOnClickListener(v -> {
            CustomDialog.showDialog(this, "Bạn có muốn đăng xuất ?", CustomDialog.DialogType.YES_NO, new CustomDialog.DialogClickListener() {
                @Override
                public void onYesClick() {
                    presenter.logout();
                }

                @Override
                public void onNoClick() {

                }

                @Override
                public void onOkClick() {

                }
            });
        });
    }
    private void initData(){
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        currentUser = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
        if(currentUser!=null){
            this.txtViewName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            if(currentUser.getAvatarUrl()!=null){
                Glide.with(getApplicationContext()).load(currentUser.getAvatarUrl()).into(avatarImg);
            }
            else {
                Glide.with(getApplicationContext()).load(R.drawable.avatar3).into(avatarImg);
            }
        }
        else{
            presenter.loadUser();
        }
    }
    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_report_issues, null, false);
        reportIssueDialog.setContentView(view);
    }

    @Override
    public void loadingFailed(Exception e) {
        Log.e("profile-activity",e.getMessage());
    }

    @Override
    public void logoutSuccess() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.removeKey(User.FIREBASE_COLLECTION_NAME);
        Intent intent = new Intent(this, AnimationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void loadingUserSuccess(User user) {
        this.currentUser = user;
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putSerializable(User.FIREBASE_COLLECTION_NAME, user);
        this.txtViewName.setText(user.getFirstName() + " " + user.getLastName());
        if(user.getAvatarUrl()!=null){
            Glide.with(getApplicationContext()).load(user.getAvatarUrl()).into(avatarImg);
        }
        else {
            Glide.with(getApplicationContext()).load(R.drawable.avatar3).into(avatarImg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
