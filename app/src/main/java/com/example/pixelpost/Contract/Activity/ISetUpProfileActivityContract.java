package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.User.User;

public interface ISetUpProfileActivityContract {
    interface View{
        void onLoadingFailed(Exception ex);
        void onCreateAccountSuccess(User user);
    }
    interface Presenter{
        void onCreatingAccount(User user);
    }

}
