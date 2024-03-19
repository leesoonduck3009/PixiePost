package com.example.pixelpost.Model.PostReaction;


public class PostReactionModel implements IPostReactionModel {
    //region Singleton define

    private static PostReactionModel instance;

    // Private constructor to prevent instantiation from outside
    private PostReactionModel() {}

    // Static method to get the singleton instance
    public static PostReactionModel getInstance() {
        if (instance == null) {
            synchronized (PostReactionModel.class) {
                if (instance == null) {
                    instance = new PostReactionModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
