package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.ISignUp01ActivityContract;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.UserModel;

public class SignUp01ActivityPresenter implements ISignUp01ActivityContract.Presenter {
    private ISignUp01ActivityContract.View view;
    private IUserModel userModel;
    public SignUp01ActivityPresenter(ISignUp01ActivityContract.View view)
    {
        this.view = view;
        userModel = UserModel.getInstance();
    }
    @Override
    public void checkEmail(String email) {
        userModel.checkEmail(email, new IUserModel.OnCheckingEmailListener() {
            @Override
            public void onCheckingEmail(boolean isExisted, Exception e) {
                if(e!=null)
                {
                    view.checkEmailFailed(e);
                    return;
                }
                if(!isExisted)
                    view.isLegitEmail();
                else
                    view.isNotLegitEmail();
            }
        });
    }
}
