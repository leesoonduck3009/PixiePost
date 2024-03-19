package com.example.pixelpost.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pixelpost.Contract.Activity.IConversationDetailActivityContract;
import com.example.pixelpost.Contract.Activity.IConversationListActivityContract;
import com.example.pixelpost.Presenter.Acitivity.ConversationDetailActivityPresenter;
import com.example.pixelpost.R;

public class ConversationDetailActivity extends AppCompatActivity implements IConversationDetailActivityContract.View {
    private IConversationDetailActivityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);
        presenter = new ConversationDetailActivityPresenter(this);
    }
}