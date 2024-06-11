package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.User.User;

public interface IProfileActivityContract {
    interface View{
        void loadingFailed(Exception e);
        void logoutSuccess();
        void loadingUserSuccess(User user);
    }
    interface Presenter{
        void logout();
        void loadUser();
    }

}
