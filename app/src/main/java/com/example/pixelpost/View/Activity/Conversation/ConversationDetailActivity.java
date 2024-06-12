package com.example.pixelpost.View.Activity.Conversation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Contract.Activity.IConversationDetailActivityContract;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.ConversationDetailActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.View.Adapter.Conversation.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConversationDetailActivity extends AppCompatActivity implements IConversationDetailActivityContract.View {
    private IConversationDetailActivityContract.Presenter presenter;
    private RecyclerView conversationRecyclerView;

    private ImageView bttBackConversation;
    private ImageView bttSendMessage;
    private ArrayList<Message> listChatMessages;
    private MessageAdapter messageAdapter;
    private TextView textUser1;
    private Conversation conversation;
    private RoundedImageView imageProfile;
    private List<Bitmap> listBitmapImages;
    private EditText inputMessage;
    private User receiverUser;
    private ProgressBar progressBar;
    private MessageAdapter chatMessageAdapter;
    private HashMap<String, Post> postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);
        presenter = new ConversationDetailActivityPresenter(this);
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView);
        bttBackConversation = findViewById(R.id.bttBackConversation);
        textUser1 = findViewById(R.id.textUser1);
        bttSendMessage = findViewById(R.id.bttSendMessage);
        imageProfile = findViewById(R.id.imageProfile);
        progressBar = findViewById(R.id.progressBar);
        inputMessage = findViewById(R.id.inputMessage);
        postItem = new HashMap<>();
        listChatMessages = new ArrayList<>();
        initData();
        conversationRecyclerView.setAdapter(chatMessageAdapter);
        setListener();

    }
    private void setListener()
    {
        bttSendMessage.setOnClickListener(v->{
            Message message = new Message.Builder().setConversationId(conversation.getId())
                    .setReceiverId(conversation.getRecieverUser().getId()).setText(inputMessage.getText().toString())
                    .setTimeSent(new Date()).setSenderId(FirebaseAuth.getInstance().getUid()).build();
            presenter.onSendingMessage(message);
            inputMessage.setText("");
        });
    }
    private void initData()
    {
        Intent intent = getIntent();
            String conversationId = intent.getStringExtra(Conversation.FIREBASE_COLLECTION_NAME);
            presenter.onGettingConversation(conversationId);
    }
    private void loadConversation(Conversation conversation){
        receiverUser = conversation.getRecieverUser();
        textUser1.setText(conversation.getRecieverUser().getFirstName()+conversation.getRecieverUser().getLastName());
        if(conversation.getRecieverUser().getAvatarUrl()!=null)
            Glide.with(getApplicationContext()).load(conversation.getRecieverUser().getAvatarUrl()).into(imageProfile);
        else
            Glide.with(getApplicationContext()).load(R.drawable.avatar3).into(imageProfile);
        chatMessageAdapter = new MessageAdapter(listChatMessages, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),postItem,receiverUser);
        conversationRecyclerView.setAdapter(chatMessageAdapter);
        presenter.onLoadingMessage(conversation.getId());
    }

    @Override
    public void onFinishLoadMessage(Message message, Post post, boolean isLastMessage) {
        listChatMessages.add((message));
        if (isLastMessage) {
            progressBar.setVisibility(View.GONE);
            conversationRecyclerView.setVisibility(View.VISIBLE);
        }
        if(post!=null)
            postItem.put(message.getPostId(),post);
        Collections.sort(listChatMessages, Comparator.comparing(Message::getTimeSent));
        chatMessageAdapter.notifyItemRangeInserted(listChatMessages.size(), listChatMessages.size());
        conversationRecyclerView.scrollToPosition(listChatMessages.size() - 1);
        conversationRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoadingFailed(Exception ex) {
        Log.e("CONVERSATION_DETAIL",ex.getMessage().toString());
        Toast.makeText(getApplicationContext(),"Some thing when wrong",Toast.LENGTH_SHORT);
    }

    @Override
    public void onGetConversation(Conversation conversation) {
        this.conversation = conversation;
        loadConversation(conversation);
    }
}