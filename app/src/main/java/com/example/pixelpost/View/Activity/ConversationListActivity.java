package com.example.pixelpost.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pixelpost.Contract.Activity.IConversationListActivityContract;
import com.example.pixelpost.Presenter.Acitivity.ConversationListActivityPresenter;
import com.example.pixelpost.R;

public class ConversationListActivity extends AppCompatActivity implements IConversationListActivityContract.View {
    private IConversationListActivityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        presenter = new ConversationListActivityPresenter(this);
    }
}