package com.example.pixelpost.Model.Post;

import com.google.firebase.firestore.DocumentChange;

public interface IPostModel {
    void sendPost(Post post,byte[] image, OnFinishSendPostListener listener);
    interface OnFinishSendPostListener{
        void onFinishSendPost(Post post, Exception e);
    }
    void recievedPost(OnFinishReceiveListener listener);
    interface OnFinishReceiveListener{
        void onFinishReceivePost(Post post, DocumentChange.Type postType, Exception e);

    }
    void sendReactionPost(String reaction, String postId);
}
