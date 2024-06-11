package com.example.pixelpost.Presenter;

import android.net.Uri;

import com.example.pixelpost.Contract.Activity.IUpdateUserInformationActivityContract;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;
import com.example.pixelpost.View.Activity.UpdateUserInformationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateUserInformationActivityPresenter implements IUpdateUserInformationActivityContract.Presenter {
    private UserModel userModel;
    private IUpdateUserInformationActivityContract.View view;
    public UpdateUserInformationActivityPresenter(IUpdateUserInformationActivityContract.View view){
        this.view = view;
        userModel = UserModel.getInstance();
    }
    @Override
    public void uploadAvatar(byte[] image, User currentUser) {
        userModel.uploadAvatar(image, currentUser, new IUserModel.OnUserOperationListener() {
            @Override
            public void onUserOperationCompleted(User user, Exception e) {
                if(e!=null)
                    view.loadingFailed(e);
                else
                    view.uploadAvatarSuccess(user);
            }
        });
    }

    @Override
    public void updateInformation(User user) {
        userModel.updateUser(user, new IUserModel.OnUserOperationListener() {
            @Override
            public void onUserOperationCompleted(User user, Exception e) {
                if(e!=null){
                    view.loadingFailed(e);
                }
                else
                    view.updateInformationSuccess(user);
            }
        });
    }
}
