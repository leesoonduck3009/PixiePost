package com.example.pixelpost.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IDenyUserListener;
import com.example.pixelpost.databinding.ListitemExistingFriendBinding;

import java.util.List;

public class ListSendingFriendAdapter extends RecyclerView.Adapter<ListSendingFriendAdapter.ListSendingFriendViewHolder>{
    private final List<User> userList;
    private final IDenyUserListener removeUserListener;
    private final List<FriendRequest> friendRequestList;

    public ListSendingFriendAdapter(List<User> userList,List<FriendRequest> friendRequestList, IDenyUserListener removeUserListener) {
            this.removeUserListener=removeUserListener;
            this.userList = userList;
            this.friendRequestList = friendRequestList;

    }

    @NonNull
    @Override
    public ListSendingFriendAdapter.ListSendingFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListSendingFriendAdapter.ListSendingFriendViewHolder(ListitemExistingFriendBinding
            .inflate(LayoutInflater.from(parent.getContext()),parent,false)
            );
            }


        @Override
    public void onBindViewHolder(@NonNull ListSendingFriendAdapter.ListSendingFriendViewHolder holder, int position) {
            holder.setData(userList.get(position),friendRequestList.get(position));
            }

    @Override
    public int getItemCount() {
            return userList.size();
    }
    class ListSendingFriendViewHolder extends RecyclerView.ViewHolder{
        ListitemExistingFriendBinding binding;
        ListSendingFriendViewHolder(ListitemExistingFriendBinding listitemSendingFriendBinding)
        {
            super(listitemSendingFriendBinding.getRoot());
            binding=listitemSendingFriendBinding;
        }
        void setData(User user,FriendRequest friendRequest)
        {
            binding.listName.setText(user.getLastName() +" "+ user.getFirstName());
            if(user.getAvatarUrl()==null || user.getAvatarUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.avatar3).into(binding.listImage);
            else
                Glide.with(binding.getRoot()).load(user.getAvatarUrl()).into(binding.listImage);
            binding.btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeUserListener.OnDenyUserClick(user,friendRequest);
                }
            });
        }

    }

}
