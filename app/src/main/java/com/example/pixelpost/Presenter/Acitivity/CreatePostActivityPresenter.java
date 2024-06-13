package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.ICreatePostActivityContract;
import com.example.pixelpost.Model.Post.IPostModel;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.Post.PostModel;

public class CreatePostActivityPresenter implements ICreatePostActivityContract.Presenter {
    private ICreatePostActivityContract.View view;
    private IPostModel postModel;
    public CreatePostActivityPresenter(ICreatePostActivityContract.View view){
        this.view = view;
        postModel = PostModel.getInstance();
    }
    @Override
    public void uploadPost(Post post, byte[] postImage) {
        postModel.sendPost(post, postImage, new IPostModel.OnFinishSendPostListener() {
            @Override
            public void onFinishSendPost(Post post, Exception e) {
                if(e!=null)
                    view.uploadPostFailed(e);
                else
                    view.uploadPostSuccess();
            }
        });
    }
}
