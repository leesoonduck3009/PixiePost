package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;

public interface IMainActivityContract {
    interface View{
        void getFriendRequestSuccess(User user,FriendRequest friendRequest, FriendRequestDialog.FriendRequestDialogType type);
        void loadingFailed(Exception e);
        void sendFriendRequestSuccess(FriendRequest friendRequest);
        void confirmFriendRequestSucess();
    }
    interface Presenter{
        void getUserFriendRequest(String id);
        void sendFriendRequest(String id);
        void acceptFriendRequest(FriendRequest friendRequest);
    }
}
