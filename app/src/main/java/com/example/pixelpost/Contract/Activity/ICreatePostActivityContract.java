package com.example.pixelpost.Contract.Activity;

import com.example.pixelpost.Model.Post.Post;

public interface ICreatePostActivityContract {
    interface Presenter{
        void uploadPost(Post post, byte[] postImage);
    }
    interface View{
        void uploadPostSuccess();
        void uploadPostFailed(Exception e);
    }
}
