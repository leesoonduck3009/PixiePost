package com.example.pixelpost.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IAddUserListener;
import com.example.pixelpost.Utils.Listener.IRemoveUserListener;
import com.example.pixelpost.databinding.ListitemExistingFriendBinding;
import com.example.pixelpost.databinding.ListitemRecommendFriendBinding;

import java.util.List;

public class ListRecommendFriendAdapter  extends RecyclerView.Adapter<ListRecommendFriendAdapter.ListRecommendFriendViewHolder>{

    private final List<User> userList;
    private final IAddUserListener addUserListener;

    public ListRecommendFriendAdapter(List<User> userList, IAddUserListener addUserListener) {
        this.addUserListener=addUserListener;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ListRecommendFriendAdapter.ListRecommendFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListRecommendFriendAdapter.ListRecommendFriendViewHolder(ListitemRecommendFriendBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecommendFriendAdapter.ListRecommendFriendViewHolder holder, int position) {

        holder.setData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    class ListRecommendFriendViewHolder extends RecyclerView.ViewHolder{
        ListitemRecommendFriendBinding binding;
        ListRecommendFriendViewHolder(ListitemRecommendFriendBinding listitemRecommendFriendBinding)
        {
            super(listitemRecommendFriendBinding.getRoot());
            binding=listitemRecommendFriendBinding;
        }
        void setData(User user)
        {
            binding.listRecommendFriendName.setText(user.getLastName() +" "+ user.getFirstName());
            if(user.getAvatarUrl()==null || user.getAvatarUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.avatar3).into(binding.listRecommendFriendImage);
            else
                Glide.with(binding.getRoot()).load(user.getAvatarUrl()).into(binding.listRecommendFriendImage);
            binding.btnAddNewFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUserListener.OnAddUserClick(user);
                }
            });
        }

    }
}
