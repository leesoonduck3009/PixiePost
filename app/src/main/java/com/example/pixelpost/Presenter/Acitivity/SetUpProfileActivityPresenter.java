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
        userModel.createUser(user,(user1, isUserExisted, e) -> {
            if(e!=null)
            {
                view.onLoadingFailed(e);
            }
            else{
                if(isUserExisted)
                    view.onLoadingFailed(new Exception("Email này đã được sử dụng"));
                else
                {
                    view.onCreateAccountSuccess(user1);
                }
            }
        });
    }
}
