package com.example.pixelpost.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IAcceptUserListener;
import com.example.pixelpost.Utils.Listener.IRemoveUserListener;
import com.example.pixelpost.databinding.ListitemExistingFriendBinding;
import com.example.pixelpost.databinding.ListitemRequestFriendBinding;

import java.util.List;

public class ListRequestFriendAdapter extends RecyclerView.Adapter<ListRequestFriendAdapter.ListRequestFriendViewHolder> {
    private final List<User> userList;
    private final IAcceptUserListener acceptUserListener;
    private final IRemoveUserListener removeUserListener;

    public ListRequestFriendAdapter(List<User> userList, IAcceptUserListener acceptUserListener, IRemoveUserListener removeUserListener) {
        this.acceptUserListener=acceptUserListener;
        this.removeUserListener = removeUserListener;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ListRequestFriendAdapter.ListRequestFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListRequestFriendAdapter.ListRequestFriendViewHolder(ListitemRequestFriendBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ListRequestFriendAdapter.ListRequestFriendViewHolder holder, int position) {

        holder.setData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    class ListRequestFriendViewHolder extends RecyclerView.ViewHolder{
        ListitemRequestFriendBinding binding;
        ListRequestFriendViewHolder(ListitemRequestFriendBinding listitemRequestFriendBinding)
        {
            super(listitemRequestFriendBinding.getRoot());
            binding=listitemRequestFriendBinding;
        }
        void setData(User user)
        {
            binding.listRequestFriendName.setText(user.getLastName() +" "+ user.getFirstName());
            if(user.getAvatarUrl()==null || user.getAvatarUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.avatar3).into(binding.listRequestImage);
            else
                Glide.with(binding.getRoot()).load(user.getAvatarUrl()).into(binding.listRequestImage);
            binding.btnAcceptFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptUserListener.OnAcceptUserClick(user);
                }
            });
            binding.btnRejectFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeUserListener.OnRemoveUserClick(user);
                }
            });
        }

    }
}
