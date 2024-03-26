package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.Conversation.Conversation;

import java.util.List;

public interface IConversationListActivityContract {
    interface View{
        void onFinishLoadConversation(Conversation conversation, int loadingConversationType, boolean isLastConversation);
        void onLoadingFailed(Exception ex);
    }
    interface Presenter{
        void onLoadingConversation();
    }
}
