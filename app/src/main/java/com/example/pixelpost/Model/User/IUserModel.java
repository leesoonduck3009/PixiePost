package com.example.pixelpost.Model.User;

public interface IUserModel {
    void createUser(User user, OnCreateUserListener listener);
    void updateUser(User user, OnUserOperationListener listener);
    void login(User user, OnUserOperationListener listener);
    void getCurrentUser(OnUserOperationListener listener);
    void changePassword(User user, String oldPassword, String newPassword, OnUserOperationListener listener);
    void changeForgotPassword(String email, String newPassword, OnUserOperationListener listener);
    void logout(User user, OnUserOperationListener listener);
    void uploadAvatar(byte[] avatarImage, User user, OnUserOperationListener listener);
    void checkEmail(String email, OnCheckingEmailListener listener);
    interface OnCheckingEmailListener{
        void onCheckingEmail(boolean isExisted, Exception e);
    }
    interface OnCreateUserListener{
        void onCreateUserComplete(User user, boolean isUserExisted, Exception e);
    }
    interface OnUserOperationListener {
        void onUserOperationCompleted(User user, Exception e);
    }
}
