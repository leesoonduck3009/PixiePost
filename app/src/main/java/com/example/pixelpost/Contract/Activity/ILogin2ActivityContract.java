package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.User.User;

public interface ILogin2ActivityContract {
    interface Presenter{
        void LoginByEmail(String email, String password);
    }
    interface View{
        void LoginSuccess(User user);
        void LoginFailed(Exception e);
    }
}
