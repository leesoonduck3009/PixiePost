package com.example.pixelpost.View.Adapter.Conversation;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.StringFormat;
import com.example.pixelpost.databinding.ItemContainerImageMessageBinding;
import com.example.pixelpost.databinding.ItemContainerReceivedMessageBinding;
import com.example.pixelpost.databinding.ItemContainerSentMessageBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Message> messages;
    private List<Boolean> listIsShowTime;
    private HashMap<String, Post> postItem;
    private User otherUser;
    private final String accountSenderId;
    private Context context;
    public static final int VIEW_TEXT_SENT=1;
    public static final int VIEW_TEXT_RECEIVED=2;
    public static final int VIEW_IMAGE_SENT = 3;
    public static final int VIEW_IMAGE_RECEIVED = 4;
    public MessageAdapter(List<Message> messages, String accountSenderId, HashMap<String, Post> postItem, User otherUser) {
        this.messages = messages;
        this.accountSenderId = accountSenderId;
        this.postItem = postItem;
        this.otherUser= otherUser;
        listIsShowTime = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TEXT_SENT)
            return new SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else if (viewType == VIEW_TEXT_RECEIVED)
            return new ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        else
            return new ImageMessageViewHolder(ItemContainerImageMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TEXT_SENT)
        {
            ((SentMessageViewHolder) holder).setData(messages.get(position));
        }
        else if(getItemViewType(position) == VIEW_TEXT_RECEIVED)
            ((ReceivedMessageViewHolder) holder).setData((messages.get(position)));
        else
            ((ImageMessageViewHolder) holder).setData(postItem.get(messages.get(position).getPostId()), otherUser);


    }
    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderId().equals(accountSenderId))
        {
            if(messages.get(position).getPostId() != null)
                return VIEW_IMAGE_SENT;
            else
                return VIEW_TEXT_SENT;
        }
        else
        {
            if(messages.get(position).getPostId() != null )
                return VIEW_IMAGE_RECEIVED;
            else
                return VIEW_TEXT_RECEIVED;
        }
    }
    // Hàm cập nhật bitmap tại một vị trí
    public void updateBitmap(int position, Message message) {
        // Thực hiện cập nhật dữ liệu cho ViewHolder tại vị trí position
        messages.set(position, message);

        // Thông báo cho RecyclerView cập nhật lại item tại vị trí position
        notifyItemChanged(position, "bitmap");
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        // Nếu đối tượng payload chứa thông tin về bitmap mới
        if (payloads.contains("bitmap")) {
            // Lấy bitmap mới từ danh sách bitmap
            Message message = messages.get(position);
            if(getItemViewType(position)==VIEW_TEXT_SENT)
            {
                ((SentMessageViewHolder) holder).setData(messages.get(position));
            }
            else if(getItemViewType(position) == VIEW_TEXT_RECEIVED)
                ((ReceivedMessageViewHolder) holder).setData((messages.get(position)));

        } else {
            // Nếu không có payload hoặc payload không chứa thông tin về bitmap mới
            // Thực hiện các xử lý bình thường để cập nhật dữ liệu cho ViewHolder
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    // Override phương thức onBindViewHolder() để cập nhật dữ liệu cho ViewHol

    @Override
    public int getItemCount() {
        return messages.size();
    }


    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;
        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSendMessageBinding)
        {
            super(itemContainerSendMessageBinding.getRoot());
            binding=itemContainerSendMessageBinding;
        }
        void setData(Message message)
        {
            binding.textMessage.setText(message.getText());
            binding.textMessage.setOnClickListener(view -> {
                if(binding.textDateTime.getVisibility()==View.VISIBLE)
                {
                    binding.textDateTime.setVisibility(View.GONE);
                }
                else
                    binding.textDateTime.setVisibility(View.VISIBLE);
            });
            binding.textDateTime.setText(StringFormat.getReadableDateTime(message.getTimeSent()));
        }
    }
    static class ReceivedMessageViewHolder extends  RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;
        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding)
        {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding=itemContainerReceivedMessageBinding;
        }
        void setData(Message message)
        {
            binding.textMessage.setText(message.getText());
            binding.textMessage.setOnClickListener(view -> {
                if(binding.textDateTime.getVisibility()==View.VISIBLE)
                {
                    binding.textDateTime.setVisibility(View.GONE);
                }
                else
                    binding.textDateTime.setVisibility(View.VISIBLE);
            });
            binding.textDateTime.setText(StringFormat.getReadableDateTime(message.getTimeSent()));
        }
    }
    static class ImageMessageViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerImageMessageBinding itemContainerImageMessageBinding;
        ImageMessageViewHolder(ItemContainerImageMessageBinding itemContainerImageMessageBinding) {
            super(itemContainerImageMessageBinding.getRoot());
            this.itemContainerImageMessageBinding = itemContainerImageMessageBinding;
        }
        void setData(Post post, User user){
            if(user.getAvatarUrl()!=null)
                Glide.with(itemContainerImageMessageBinding.getRoot()).load(user.getAvatarUrl()).into(itemContainerImageMessageBinding.avatarImg);
            else
                Glide.with(itemContainerImageMessageBinding.getRoot()).load(R.drawable.avatar3).into(itemContainerImageMessageBinding.avatarImg);
            Glide.with(itemContainerImageMessageBinding.getRoot()).load(post.getUrl()).into(itemContainerImageMessageBinding.image);
            if(Objects.equals(post.getOwnerId(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                itemContainerImageMessageBinding.userName.setText("Bạn");
            else
                itemContainerImageMessageBinding.userName.setText(user.getFirstName() + " " + user.getLastName());
            itemContainerImageMessageBinding.postTimeUpload.setText(StringFormat.getReadableDateTime(post.getTimePosted()));
        }
    }

}
