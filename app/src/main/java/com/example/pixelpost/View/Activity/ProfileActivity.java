package com.example.pixelpost.View.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;

public class ProfileActivity extends AppCompatActivity {
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        preferenceManager = new PreferenceManager(getApplicationContext());
        User user = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
        FriendRequestDialog.showDialog(this, user, FriendRequestDialog.FriendRequestDialogType.IS_FRIEND, new FriendRequestDialog.DialogClickListener() {
            @Override
            public void onAcceptFriendClick() {

            }
        });
    }

}