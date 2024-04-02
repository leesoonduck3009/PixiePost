package com.example.pixelpost.View.Adapter.Conversation;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IConversationListener;
import com.example.pixelpost.databinding.ItemContainerConversationBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
    private final List<Conversation> listConversation;
    private final IConversationListener conversationListener;
    public final int HAVE_SEEN_MESSAGE = 1;
    public final int HAVE_NOT_SEEN_MESSAGE = 2;

    public ConversationAdapter(List<Conversation> listConversation, IConversationListener conversationListener) {
        this.conversationListener=conversationListener;
        this.listConversation = listConversation;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(ItemContainerConversationBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {

        holder.setData(listConversation.get(position),HAVE_NOT_SEEN_MESSAGE);
    }
    /*    @Override
        public int getItemViewType(int position) {
            if(chatMessageList.get(position).conversationSeen.equals(userId))
            {
                return HAVE_NOT_SEEN_MESSAGE;
            }
            else
                return HAVE_SEEN_MESSAGE;
        }*/
    @Override
    public int getItemCount() {
        return listConversation.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{
        ItemContainerConversationBinding binding;
        ConversationViewHolder(ItemContainerConversationBinding itemContainerConversationBinding)
        {
            super(itemContainerConversationBinding.getRoot());
            binding=itemContainerConversationBinding;
        }
        void setData(Conversation conversation, int type)
        {
            User receiverUser = conversation.getRecieverUser();
            Message lastMessage = conversation.getLastMessage();
            if(receiverUser.getAvatarUrl()==null || receiverUser.getAvatarUrl().isEmpty())
                Glide.with(binding.getRoot()).load(R.drawable.ic_basic_avatar).into(binding.imageProfile);
            else
                Glide.with(binding.getRoot()).load(receiverUser.getAvatarUrl()).into(binding.imageProfile);
            binding.textConversationName.setText(receiverUser.getFirstName()+ receiverUser.getLastName());
            if(conversation.getPersonNotSeenID() != null) {
                if (conversation.getPersonNotSeenID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    binding.textRecentMessage.setTypeface(Typeface.DEFAULT_BOLD);
                    binding.textRecentMessage.setTextColor(Color.BLACK);
                } else {
                    binding.textRecentMessage.setTypeface(Typeface.DEFAULT);
                    binding.textRecentMessage.setTextColor(binding.getRoot().getResources().getColor(R.color.secondary_text_color));
                }
            }
            if(lastMessage.getTimeSent()!=null) {
                SimpleDateFormat sdfDate;
                Calendar calendarNow = Calendar.getInstance();
                int dateNow = calendarNow.get(Calendar.DAY_OF_MONTH);
                int monthNow = calendarNow.get(Calendar.MONTH);
                int yearNow = calendarNow.get(Calendar.YEAR);
                Calendar calendarConversation = Calendar.getInstance();
                calendarConversation.setTime(lastMessage.getTimeSent());
                int dateConversation = calendarConversation.get(Calendar.DATE);
                int monthConversation = calendarConversation.get(Calendar.MONTH);
                int yearConversation = calendarConversation.get(Calendar.YEAR);
                String messageDate;
                if (dateNow == dateConversation && monthNow == monthConversation && yearConversation == yearNow) {
                    sdfDate = new SimpleDateFormat("HH:mm");
                } else if (yearConversation == yearNow) {
                    sdfDate = new SimpleDateFormat("dd/MM");
                } else
                    sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                messageDate = sdfDate.format(lastMessage.getTimeSent());
                binding.textConversationDate.setText(messageDate);
            }
            binding.textConversationName.setText(receiverUser.getFirstName()+ receiverUser.getLastName());
            binding.textRecentMessage.setText(lastMessage.getText());
            binding.getRoot().setOnClickListener(view -> {
                conversationListener.onConversatonClick(conversation);
            });
        }

    }
}
