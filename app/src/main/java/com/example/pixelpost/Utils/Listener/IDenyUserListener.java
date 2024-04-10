package com.example.pixelpost.Utils.Listener;

import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;

public interface IDenyUserListener {
    void OnDenyUserClick (User user, FriendRequest friendRequest);

}
