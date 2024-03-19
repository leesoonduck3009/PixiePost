package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IConversationDetailActivityContract;
import com.example.pixelpost.Contract.Activity.IConversationListActivityContract;
import com.example.pixelpost.Model.Conversation.ConversationModel;
import com.example.pixelpost.Model.Conversation.IConversationModel;

public class ConversationListActivityPresenter implements IConversationListActivityContract.Presenter {
    //region Define property
    private IConversationListActivityContract.View view;
    private IConversationModel conversationModel;
    //endregion
    //region Constructor
    public ConversationListActivityPresenter(IConversationListActivityContract.View view)
    {
        this.view = view;
        this.conversationModel = ConversationModel.getInstance();
    }
    //endregion
}
