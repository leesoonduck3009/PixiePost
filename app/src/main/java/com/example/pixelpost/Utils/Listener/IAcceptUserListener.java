package com.example.pixelpost.Utils.Listener;

import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;

public interface IAcceptUserListener {
    void OnAcceptUserClick (User user, FriendRequest friendRequest);
}
