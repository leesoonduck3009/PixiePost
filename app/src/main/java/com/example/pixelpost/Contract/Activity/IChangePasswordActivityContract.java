package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.User.User;

public interface IChangePasswordActivityContract {
    interface Presenter{
        void changePassword(User user, String oldpassword, String newpassword);
    }
    interface View {
        void changePasswordSuccess();
        void changePasswordFailed(Exception e);
    }
}
