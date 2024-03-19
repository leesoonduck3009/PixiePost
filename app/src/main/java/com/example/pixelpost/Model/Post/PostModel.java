package com.example.pixelpost.Model.Post;


public class PostModel implements IPostModel{
    //region Singleton define
    private static PostModel instance;

    // Private constructor to prevent instantiation from outside
    private PostModel() {}

    // Static method to get the singleton instance
    public static PostModel getInstance() {
        if (instance == null) {
            synchronized (PostModel.class) {
                if (instance == null) {
                    instance = new PostModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
