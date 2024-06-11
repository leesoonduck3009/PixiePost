package com.example.pixelpost.View.Adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IConversationListener;
import com.example.pixelpost.Utils.Listener.IRemoveUserListener;
import com.example.pixelpost.Utils.Listener.ISendMessageListener;
import com.example.pixelpost.databinding.ItemContainerConversationBinding;
import com.example.pixelpost.databinding.ListitemExistingFriendBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListExistingFriendAdapter extends RecyclerView.Adapter<ListExistingFriendAdapter.ListExistingFriendViewHolder>{
    private final List<User> userList;

    private final IRemoveUserListener removeUserListener;
    private final ISendMessageListener sendMessageListener;

    public ListExistingFriendAdapter(List<User> userList, IRemoveUserListener removeUserListener, ISendMessageListener sendMessageListener) {
        this.removeUserListener=removeUserListener;
        this.userList = userList;
        this.sendMessageListener = sendMessageListener;
    }

    @NonNull
    @Override
    public ListExistingFriendAdapter.ListExistingFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListExistingFriendAdapter.ListExistingFriendViewHolder(ListitemExistingFriendBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ListExistingFriendAdapter.ListExistingFriendViewHolder holder, int position) {

        holder.setData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    class ListExistingFriendViewHolder extends RecyclerView.ViewHolder{
        ListitemExistingFriendBinding binding;
        ListExistingFriendViewHolder(ListitemExistingFriendBinding listitemExistingFriendBinding)
        {
            super(listitemExistingFriendBinding.getRoot());
            binding=listitemExistingFriendBinding;
        }
        void setData(User user)
        {
            binding.listName.setText(user.getLastName() +" "+ user.getFirstName());
            if(user.getAvatarUrl()==null || user.getAvatarUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.avatar3).into(binding.listImage);
            else
                Glide.with(binding.getRoot()).load(user.getAvatarUrl()).into(binding.listImage);
            binding.btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeUserListener.OnRemoveUserClick(user);
                }
            });
            binding.listImage.setOnClickListener(v -> {
                sendMessageListener.OnSendMessageClick(user);
            });
        }

    }

}
