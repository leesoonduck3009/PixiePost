package com.example.pixelpost.Model.Message;

import com.example.pixelpost.Model.Post.Post;

public interface IMessageModel {
    void SendMessage(Message message, OnFinishSendMessageListener listener);
    interface  OnFinishSendMessageListener{
        void onFinishSendMessage(Message message, Exception e);
    }
    void ReceiveMessage(String conversationId, OnFinishReceiveMessageListener listener);
    interface OnFinishReceiveMessageListener{
        void onFinishReceiveMessage(Message message, Post post,  Exception e, boolean islastMessage);
    }
}
