package com.example.pixelpost.Model.Conversation;

public interface IConversationModel {
    void LoadingConversation(OnFinishLoadListener listener);
    interface OnFinishLoadListener{
        void onFinishLoading(Conversation conversation, Exception e, int type, boolean isLastMessage);
    }
}
