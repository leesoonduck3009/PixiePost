package com.example.pixelpost.Model.Message;

public class MessageModel implements IMessageModel{
    //region Singleton define
    private static MessageModel instance;

    // Private constructor to prevent instantiation from outside
    private MessageModel() {}

    // Static method to get the singleton instance
    public static MessageModel getInstance() {
        if (instance == null) {
            synchronized (MessageModel.class) {
                if (instance == null) {
                    instance = new MessageModel();
                }
            }
        }
        return instance;
    }
    //endregion
}
