package com.example.pixelpost.Model.Post;


import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Utils.SupportClass.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostModel implements IPostModel{
    //region Singleton define
    private static PostModel instance;

    // Private constructor to prevent instantiation from outside
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private PostModel() {}

    // Static method to get the singleton instance
    public static PostModel getInstance() {
        if (instance == null) {
            synchronized (PostModel.class) {
                if (instance == null) {
                    instance = new PostModel();
                }
            }
        }
        return instance;
    }


    //endregion
    @Override
    public void sendPost(Post post, byte[] image, OnFinishSendPostListener listener) {
        initFirebase();
        post.setOwnerId(user.getUid());
        post.setTimePosted(new Date());
        db.collection(Post.FIREBASE_COLLECTION_NAME).add(post).addOnCompleteListener(taskPost->{
            if(taskPost.isSuccessful())
            {
                Storage.UploadImage(image,taskPost.getResult().getId(),(url, e) -> {
                    if(e!=null)
                    {
                        post.setId(taskPost.getResult().getId());
                        post.setUrl(url);
                        db.collection(Post.FIREBASE_COLLECTION_NAME).document(taskPost.getResult().getId())
                                .update(Post.FIELD_URL,url).addOnCompleteListener(taskPost1->{
                                    if(taskPost1.isSuccessful())
                                    {
                                        listener.onFinishSendPost(post,null);
                                        return;
                                    }
                                    else
                                    {
                                        listener.onFinishSendPost(null, taskPost1.getException());
                                        return;
                                    }
                                });
                    }
                    else{
                        listener.onFinishSendPost(null, e);
                        return;
                    }
                });
            }
            else{
                listener.onFinishSendPost(null, taskPost.getException());
                return;
            }
        });

    }

    @Override
    public void recievedPost(OnFinishReceiveListener listener) {
        initFirebase();
        db.collection(Post.FIREBASE_COLLECTION_NAME).whereArrayContains(Post.FIELD_DISPLAYED_USERS,user.getUid())
                .addSnapshotListener(((value, error) -> {
            if(error!=null){
                listener.onFinishReceivePost(null,null, error);
                return;
            }
            else{
                assert value != null;
                for(DocumentChange dc : value.getDocumentChanges())
                {
                    switch (dc.getType())
                    {
                        case ADDED:
                        case MODIFIED:
                            Post post = dc.getDocument().toObject(Post.class);
                            post.setId(dc.getDocument().getId());
                            listener.onFinishReceivePost(post, dc.getType(),null);
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        }));
    }

    private void initFirebase()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}
