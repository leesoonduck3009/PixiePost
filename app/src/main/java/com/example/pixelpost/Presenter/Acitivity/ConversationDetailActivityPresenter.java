package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IConversationDetailActivityContract;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.Conversation.ConversationModel;
import com.example.pixelpost.Model.Conversation.IConversationModel;
import com.example.pixelpost.Model.Message.IMessageModel;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.Message.MessageModel;

public class ConversationDetailActivityPresenter implements IConversationDetailActivityContract.Presenter {
    //region Define Property
    private IConversationModel conversationModel;
    private IMessageModel messageModel;
    private IConversationDetailActivityContract.View view;
    //endregion
    //region Constructor
    public ConversationDetailActivityPresenter(IConversationDetailActivityContract.View view)
    {
        this.view = view;
        this.conversationModel = ConversationModel.getInstance();
        this.messageModel = new MessageModel();
    }
    //endregion

    @Override
    public void onLoadingMessage(String conversationId) {
        messageModel.ReceiveMessage(conversationId, (message, post, e, islastMessage) -> {
            if(e!=null)
                view.onLoadingFailed(e);
            else
                view.onFinishLoadMessage(message, post, islastMessage);
        });
    }

    @Override
    public void onSendingMessage(Message message) {
        messageModel.SendMessage(message, (message1, e) -> {
            if(e!=null)
                view.onLoadingFailed(e);
        });
    }
    @Override
    public void onGettingConversation(String conversationId) {
        conversationModel.loadConversationById(conversationId, new IConversationModel.OnFinishLoadListener() {
            @Override
            public void onFinishLoading(Conversation conversation, Exception e, int type, boolean isLastMessage) {
                if(e!=null)
                    view.onLoadingFailed(e);
                else
                    view.onGetConversation(conversation);
            }
        });
    }
}
