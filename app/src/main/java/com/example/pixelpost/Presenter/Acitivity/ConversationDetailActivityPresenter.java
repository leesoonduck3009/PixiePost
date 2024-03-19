package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IConversationDetailActivityContract;
import com.example.pixelpost.Model.Conversation.ConversationModel;
import com.example.pixelpost.Model.Conversation.IConversationModel;

public class ConversationDetailActivityPresenter implements IConversationDetailActivityContract.Presenter {
    //region Define Property
    private IConversationModel conversationModel;
    private IConversationDetailActivityContract.View view;
    //endregion
    //region Constructor
    public ConversationDetailActivityPresenter(IConversationDetailActivityContract.View view)
    {
        this.view = view;
        this.conversationModel = ConversationModel.getInstance();
    }
    //endregion
}
