package com.example.pixelpost.Model.User;


public class UserModel implements IUserModel{
    //region Singleton define
    private static UserModel instance;
    private UserModel() {}

    // Static method to get the singleton instance
    public static UserModel getInstance() {
        if (instance == null) {
            synchronized (UserModel.class) {
                if (instance == null) {
                    instance = new UserModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
