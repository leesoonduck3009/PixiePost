package com.example.pixelpost.Model.FriendRequest;


public class FriendRequestModel implements IFriendRequestModel{
    //region Singleton define
    private static FriendRequestModel instance;

    // Private constructor to prevent instantiation from outside
    private FriendRequestModel() {}

    // Static method to get the singleton instance
    public static FriendRequestModel getInstance() {
        if (instance == null) {
            synchronized (FriendRequestModel.class) {
                if (instance == null) {
                    instance = new FriendRequestModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
