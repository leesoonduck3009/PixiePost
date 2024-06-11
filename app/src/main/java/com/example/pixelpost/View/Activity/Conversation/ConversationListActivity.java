package com.example.pixelpost.View.Activity.Conversation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pixelpost.Contract.Activity.IConversationListActivityContract;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Presenter.Acitivity.ConversationListActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IConversationListener;
import com.example.pixelpost.View.Adapter.Conversation.ConversationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ConversationListActivity extends AppCompatActivity implements IConversationListActivityContract.View, IConversationListener {
    private IConversationListActivityContract.Presenter presenter;
    private RecyclerView listConversationRecyclerView;
    private ImageView btnBackConversationList;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> listConversations;
    private TextView textViewNotConversation;
    private LinearLayout layoutNotConversation;
    private ProgressBar progressBar;
    private int count;
    private boolean backFromConversationDetail = false;
    private Animation anim_emoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        btnBackConversationList = findViewById(R.id.btnBackConversationList);
        Button btnAniEmo = findViewById(R.id.btnAniEmo);
        ImageView iconEmoji = findViewById(R.id.iconEmoji);
        ArrayList<ImageView> cloneImageViews = new ArrayList<>();
        anim_emoji = AnimationUtils.loadAnimation(this, R.anim.anim_emoji);
        anim_emoji.setDuration(3000);

        btnBackConversationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAniEmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnAniEmo) {
                    // Tạo và áp dụng Animation cho các ImageView nhân bản
                    runSingleThreadAnimation(200,80);
                    runSingleThreadAnimation(70,50);
                    runSingleThreadAnimation(0,100);
                    runSingleThreadAnimation(-100,50);
                    runSingleThreadAnimation(120,50);
                    runSingleThreadAnimation(250,50);
                    runSingleThreadAnimation(-200,50);

                    //layoutNotConversation.addView(imageView1);
                }
            }
        });
        presenter = new ConversationListActivityPresenter(this);
        listConversationRecyclerView = findViewById(R.id.listConversationRecyclerView);
        textViewNotConversation = findViewById(R.id.textViewNotConversation);
        layoutNotConversation = findViewById(R.id.layoutNotConversation);
        progressBar = findViewById(R.id.progressBar);
        listConversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(listConversations, this);
        listConversationRecyclerView.setAdapter(conversationAdapter);
        count = 0;
        presenter.onLoadingConversation();


    }

    private void runSingleThreadAnimation(int x, int y){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView1 = new ImageView(getApplicationContext());
                        imageView1.setImageResource(R.drawable.image_icon1);
                        imageView1.setX(x);
                        imageView1.setY(y);
                        imageView1.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
                        layoutNotConversation.addView(imageView1);
                        imageView1.startAnimation(anim_emoji);
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                layoutNotConversation.removeView(imageView1);
                            }
                        };

                        handler.postDelayed(runnable, 3000);
                    }
                });


            }
        });
        thread.start();
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
        intent.putExtra(Conversation.FIREBASE_COLLECTION_NAME,conversation.getId());
        backFromConversationDetail = true;
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent,bundle);
    }

    @Override
    public void onFinishLoadConversation(Conversation conversation, int loadingConversationType, boolean isLastConversation) {
        if(loadingConversationType == Conversation.ADD_LIST_CONVERSATION)
        {
            if(conversation!=null)
            {
                this.listConversations.add(conversation);
                conversationAdapter.notifyDataSetChanged();
            }
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
                progressBar.setVisibility(View.GONE);
                layoutNotConversation.setVisibility(View.GONE);
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