package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;
import com.google.firebase.firestore.DocumentChange;

public interface IMainActivityContract {
    interface View{
        void getFriendRequestSuccess(User user,FriendRequest friendRequest, FriendRequestDialog.FriendRequestDialogType type);
        void loadingFailed(Exception e);
        void sendFriendRequestSuccess(FriendRequest friendRequest);
        void confirmFriendRequestSucess();
        void getUserInformation(User user);
        void onGettingPost(Post post, DocumentChange.Type type);
        void onGetPostFailed(Exception e);
        void onSendMessageSuccess(String conversationId);
        void onSendMessagePostSuccess(String conversationId);
        void onSendMessageFailed(Exception e);

    }
    interface Presenter{
        void sendMessagePost(Message message, User user);
        void sendReaction(String reaction, String postId);
        void sendMessage(Message message, User user);
        void getUserFriendRequest(String id);
        void sendFriendRequest(String id);
        void acceptFriendRequest(FriendRequest friendRequest);
        void getUserInformation();
        void getPost();
    }
}
