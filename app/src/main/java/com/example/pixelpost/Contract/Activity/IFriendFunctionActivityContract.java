package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.google.firebase.firestore.DocumentChange;

import java.lang.reflect.Type;

public interface IFriendFunctionActivityContract {
    interface View{
        void loadFriendRequest(User user,FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendRequestUser);
        void loadFriend(User user, DocumentChange.Type type, boolean isLastFriendUser);
        void refreshFriend();
        void loadFriendSendingRequest(User user,FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendUser);
        void loadingFailed(Exception e);
    }
    interface Presenter{
        void loadFriendRequest();
        void loadSendingFriendRequest();
        void loadFriend();
        void acceptFriendRequest(FriendRequest friendRequest);
        void denyFriendRequest(FriendRequest friendRequest);
        void removeFriend(User user);
        void cancelSendFriendRequest(FriendRequest friendRequest);

    }
}
