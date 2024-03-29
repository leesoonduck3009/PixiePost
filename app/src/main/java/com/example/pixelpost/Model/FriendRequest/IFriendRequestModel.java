package com.example.pixelpost.Model.FriendRequest;

import com.google.firebase.firestore.DocumentChange;

import java.util.List;

public interface IFriendRequestModel {
    void sendFriendRequest(FriendRequest friendRequest, OnFinishFriendRequestListener listener);
    interface OnFinishFriendRequestListener{
        void onFinishFriendRequest(FriendRequest friendRequest, Exception e);
    }
    void confirmFriendRequest(FriendRequest friendRequest, boolean isAccepted, OnFinishFriendRequestListener listener);
    void receiveFriendRequest(OnFinishReceivedFriendRequestListener listener);
    interface OnFinishReceivedFriendRequestListener{
        void onFinishReceivedFriendRequest(FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendRequest, Exception e);
    }
    void setFriendRequestChangeListener(OnGettingNumberFriendRequestListener listener);
    interface OnGettingNumberFriendRequestListener{
        void onGetNumber(int number, Exception e);
    }

}
