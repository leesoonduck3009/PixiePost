package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IChangePasswordActivityContract;
import com.example.pixelpost.Model.Conversation.ConversationModel;
import com.example.pixelpost.Model.Message.MessageModel;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;

public class ChangePasswordActivityPresenter implements IChangePasswordActivityContract.Presenter {
    IChangePasswordActivityContract.View view;
    IUserModel userModel;

    public ChangePasswordActivityPresenter(IChangePasswordActivityContract.View view){
        this.view = view;
        this.userModel = UserModel.getInstance();
    }

    @Override
    public void changePassword(User user, String oldpassword, String newpassword) {
        userModel.changePassword(user, oldpassword, newpassword, new IUserModel.OnUserOperationListener() {
            @Override
            public void onUserOperationCompleted(User user, Exception e) {
                if(e!=null){
                    view.changePasswordFailed(e);
                }
                else{
                    view.changePasswordSuccess();
                }
            }
        });
    }
}
