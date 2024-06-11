package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IProfileActivityContract;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;

public class ProfileActivityPresenter implements IProfileActivityContract.Presenter {
    private IProfileActivityContract.View view;
    private IUserModel userModel;
    public ProfileActivityPresenter(IProfileActivityContract.View view)
    {
        userModel = UserModel.getInstance();
        this.view = view;
    }
    @Override
    public void logout() {
        userModel.logout((user, e) -> {
            if(e!=null)
                view.loadingFailed(e);
            else
                view.logoutSuccess();
        });
    }

    @Override
    public void loadUser() {
        userModel.getCurrentUser(new IUserModel.OnUserOperationListener() {
            @Override
            public void onUserOperationCompleted(User user, Exception e) {
                if(e!=null){
                    view.loadingFailed(e);
                }
                else {
                    view.loadingUserSuccess(user);
                }
            }
        });
    }
}
