package com.example.pixelpost.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.R;
import com.example.pixelpost.databinding.ItemPostBinding;
import com.example.pixelpost.databinding.ListitemExistingFriendBinding;

import java.text.SimpleDateFormat;
import java.util.List;

public class PostSliderAdapter extends RecyclerView.Adapter<PostSliderAdapter.PostSliderViewHolder> {

    private final List<Post> postList;


    public PostSliderAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostSliderAdapter.PostSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostSliderAdapter.PostSliderViewHolder(ItemPostBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PostSliderAdapter.PostSliderViewHolder holder, int position) {
        holder.setData(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostSliderViewHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        PostSliderViewHolder(ItemPostBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            binding = itemPostBinding;
        }

        void setData(Post post){
            if(post.getUrl() == null || post.getUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.history_test_img).into(binding.postImg);
            else
                Glide.with(binding.getRoot()).load(post.getUrl()).into(binding.postImg);
            binding.postContent.setText(post.getText());
            String timeString = new SimpleDateFormat("yyyy-MM-dd").format(post.getTimePosted());
            binding.postTime.setText(timeString);
            Glide.with(binding.getRoot()).load(R.drawable.avatar3).into(binding.ownAvatar);
            binding.ownLastname.setText("Tâm");
        }
    }

}
