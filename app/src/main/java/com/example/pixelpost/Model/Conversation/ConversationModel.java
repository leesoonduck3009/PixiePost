package com.example.pixelpost.Model.Conversation;

public class ConversationModel implements IConversationModel {
    //region Singleton define
    private static ConversationModel instance;

    // Private constructor to prevent instantiation from outside
    private ConversationModel() {}

    // Static method to get the singleton instance
    public static ConversationModel getInstance() {
        if (instance == null) {
            synchronized (ConversationModel.class) {
                if (instance == null) {
                    instance = new ConversationModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
