package com.example.pixelpost.Contract.Activity;

import android.net.Uri;

import com.example.pixelpost.Model.User.User;

public interface IUpdateUserInformationActivityContract {
    interface View{
        void updateInformationSuccess(User user);
        void loadingFailed(Exception e);
        void uploadAvatarSuccess(User user);

    }
    interface Presenter{
        void uploadAvatar(byte[] image, User currentUser);
        void updateInformation(User user);
    }

}
