package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.Message.Message;

import java.util.List;

public interface IConversationDetailActivityContract {
    interface View{
        void onFinishLoadMessage(Message message, boolean isLastMessage);
        void onLoadingFailed(Exception ex);
        void onGetConversation(Conversation conversation);
    }
    interface Presenter{
        void onLoadingMessage(String conversationId);
        void onSendingMessage(Message message);
        void onGettingConversation(String conversationId);
    }

}
