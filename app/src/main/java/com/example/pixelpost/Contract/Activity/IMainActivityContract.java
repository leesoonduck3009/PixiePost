package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;

public class IMainActivityContract {
    interface View{
        void getFriendRequestSuccess(User user, boolean isFriend);
        void loadingFailed(Exception e);
        void sendFriendRequestSuccess(FriendRequest friendRequest);
    }
    interface Presenter{
        void getUserFriendRequest(String id);
        void sendFriendRequest(String id);
    }
}
