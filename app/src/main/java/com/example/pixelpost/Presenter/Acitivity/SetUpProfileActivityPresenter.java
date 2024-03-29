package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.ISetUpProfileActivityContract;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;

public class SetUpProfileActivityPresenter implements ISetUpProfileActivityContract.Presenter {
    private ISetUpProfileActivityContract.View view;
    private IUserModel userModel;
    public SetUpProfileActivityPresenter(ISetUpProfileActivityContract.View view)
    {
        this.view = view;
        userModel = UserModel.getInstance();
    }
    @Override
    public void onCreatingAccount(User user) {
    }
}
