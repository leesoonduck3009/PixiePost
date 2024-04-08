package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.Login.Login01Activity;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout btnUpdateUserProfile;
    LinearLayout btnReportIssue;

    BottomSheetDialog reportIssueDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        btnUpdateUserProfile = findViewById(R.id.btnUpdateUserProfile);
        btnReportIssue = findViewById(R.id.btnReportIssue);
        reportIssueDialog = new BottomSheetDialog(this);
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
        btnReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportIssueDialog.show();
            }
        });
        reportIssueDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_report_issues, null, false);
        reportIssueDialog.setContentView(view);
    }

}
