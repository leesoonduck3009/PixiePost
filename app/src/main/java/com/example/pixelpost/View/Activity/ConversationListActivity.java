package com.example.pixelpost.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pixelpost.Contract.Activity.IConversationListActivityContract;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Presenter.Acitivity.ConversationListActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IConversationListener;
import com.example.pixelpost.View.Adapter.ConversationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversationListActivity extends AppCompatActivity implements IConversationListActivityContract.View, IConversationListener {
    private IConversationListActivityContract.Presenter presenter;
    private RecyclerView listConversationRecyclerView;
    private ImageView bttBackConversation;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> listConversations;
    private TextView textViewNotConversation;
    private LinearLayout layoutNotConversation;
    private ProgressBar progressBar;
    private int count;
    private boolean backFromConversationDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ImageView btnBackConversationList = findViewById(R.id.btnBackConversationList);
        Button btnGotoDetail = findViewById(R.id.btnGotoDetail);
        btnBackConversationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnGotoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConversationListActivity.this, ConversationDetailActivity.class);
                startActivity(intent);
            }
        });
//        presenter = new ConversationListActivityPresenter(this);
//        listConversationRecyclerView = findViewById(R.id.listConversationRecyclerView);
//        bttBackConversation = findViewById(R.id.bttBackConversation);
//        textViewNotConversation = findViewById(R.id.textViewNotConversation);
//        layoutNotConversation = findViewById(R.id.layoutNotConversation);
//        progressBar = findViewById(R.id.progressBar);
//        listConversations = new ArrayList<>();
//        conversationAdapter = new ConversationAdapter(listConversations, this);
//        count = 0;
//        presenter.onLoadingConversation();

    }
    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        if(backFromConversationDetail)
        {
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            backFromConversationDetail = false;
        }
    }
    @Override
    public void onConversatonClick(Conversation conversation) {
        Intent intent = new Intent(getApplicationContext(),ConversationDetailActivity.class);
        intent.putExtra(Conversation.FIREBASE_COLLECTION_NAME,conversation);
        backFromConversationDetail = true;
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent,bundle);
    }

    @Override
    public void onFinishLoadConversation(Conversation conversation, int loadingConversationType, boolean isLastConversation) {
        if(loadingConversationType == Conversation.ADD_LIST_CONVERSATION)
        {
            if(conversation!=null)
                this.listConversations.add(conversation);
        }
        else if(loadingConversationType == Conversation.MODIFY_LIST_CONVERSATION)
        {
            for(int i=0;i<listConversations.size();i++)
            {
                if(listConversations.get(i).getId().equals(conversation.getId()))
                {
                    this.listConversations.get(i).setLastMessage(conversation.getLastMessage());
                    conversationAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
        Collections.sort(listConversations,(obj1, obj2)->obj2.getLastMessage().getTimeSent().compareTo(obj1.getLastMessage().getTimeSent()));
        if(isLastConversation) {
            count++;
            if(count == 2)
            {
                conversationAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                listConversationRecyclerView.smoothScrollToPosition(0);
                listConversationRecyclerView.setVisibility(View.VISIBLE);
            }
            LoadConversation(this.listConversations.size());
        }
    }
    private void LoadConversation(int length)
    {
        if(length==0 && count == 2) {
            layoutNotConversation.setVisibility(View.VISIBLE);
            listConversationRecyclerView.setVisibility(View.GONE);
        }
        else{
            layoutNotConversation.setVisibility(View.GONE);
            listConversationRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onLoadingFailed(Exception ex) {

    }
}