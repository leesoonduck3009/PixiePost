package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.ILogin2ActivityContract;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;

public class Login2ActivityPresenter implements ILogin2ActivityContract.Presenter {
    ILogin2ActivityContract.View view;
    IUserModel userModel;
    public Login2ActivityPresenter(ILogin2ActivityContract.View view)
    {
        this.view = view;
        userModel = UserModel.getInstance();
    }
    @Override
    public void LoginByEmail(String email, String password) {
        User user = new User.Builder().setEmail(email).setPassword(password).build();
        userModel.login(user,(user1, e) -> {
            if(e!=null)
                view.LoginFailed(e);
            else
                view.LoginSuccess(user1);
        });
    }
}
