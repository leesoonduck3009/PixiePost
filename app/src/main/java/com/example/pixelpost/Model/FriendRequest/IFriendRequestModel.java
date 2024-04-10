package com.example.pixelpost.Model.FriendRequest;

import com.example.pixelpost.Model.User.User;
import com.google.firebase.firestore.DocumentChange;

import java.util.List;

public interface IFriendRequestModel {
    void sendFriendRequest(FriendRequest friendRequest, OnFinishFriendRequestListener listener);
    void checkFriendRequestSent(String id, OnFinishCheckFriendRequestListener listener);
    void checkFriendRequestReceived(String id, OnFinishCheckFriendRequestListener listener);

    interface OnFinishFriendRequestListener{
        void onFinishFriendRequest(FriendRequest friendRequest, Exception e);
    }
    void confirmFriendRequest(FriendRequest friendRequest, boolean isAccepted, OnFinishFriendRequestListener listener);
    void receiveFriendRequest(OnFinishReceivedFriendRequestListener listener);
    void getSendingFriendRequest(OnFinishReceivedFriendRequestListener listener);

    interface OnFinishReceivedFriendRequestListener{
        void onFinishReceivedFriendRequest(User friendRequestUser,FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendRequest, Exception e);
    }
    interface OnFinishCheckFriendRequestListener{
        void onFinishCheckFriendRequest(User user,FriendRequest friendRequest, boolean isExisted, Exception e);
    }

    void setFriendRequestChangeListener(OnGettingNumberFriendRequestListener listener);
    interface OnGettingNumberFriendRequestListener{
        void onGetNumber(int number, Exception e);
    }

}
