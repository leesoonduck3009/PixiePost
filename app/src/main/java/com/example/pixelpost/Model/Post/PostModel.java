package com.example.pixelpost.Model.Post;


import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Utils.SupportClass.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
                Storage.UploadImage(image,taskPost.getResult().getId(),Post.FIREBASE_COLLECTION_NAME,(url, e) -> {
                    if(e==null)
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
                            DocumentSnapshot ds = dc.getDocument();
                            Object displayUser = ds.get(Post.FIELD_DISPLAYED_USERS);

                            Post post = new Post.Builder().setText(ds.getString(Post.FIELD_TEXT))
                                    .setTimePosted(ds.getDate(Post.FIELD_TIME_POSTED))
                                    .setDisplayedUsers(displayUser == null ? null : (ArrayList<String>) displayUser)
                                    .setUrl(ds.getString(Post.FIELD_URL))
                                    .setOwnerId(ds.getString(Post.FIELD_OWNER_ID))
                                    .setId(ds.getId())
                                    .setLastReaction(ds.getString(Post.FIELD_LAST_REACTION)).build();
                            if(!Objects.equals(post.getOwnerId(), auth.getCurrentUser().getUid())){
                                loadUser(post,dc.getType(),listener);
                            }
                            else
                                listener.onFinishReceivePost(post, dc.getType(),null);
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        }));
    }

    @Override
    public void sendReactionPost(String reaction, String postId) {
        initFirebase();
        db.collection(Post.FIREBASE_COLLECTION_NAME).document(postId).update(Post.FIELD_LAST_REACTION,reaction);
    }

    private void loadUser(Post post, DocumentChange.Type type,OnFinishReceiveListener listener){
        db.collection(User.FIREBASE_COLLECTION_NAME).document(post.getOwnerId()).get().addOnCompleteListener(
                task -> {
                    if(!task.isSuccessful()){
                        listener.onFinishReceivePost(null,null,task.getException());
                    }
                    else{
                        DocumentSnapshot ds = task.getResult();
                        User user1 = new User.Builder().setEmail(ds.getString(User.FIELD_EMAIL))
                                .setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL))
                                .setId(task.getResult().getId()).setFirstName(ds.getString(User.FIELD_FIRST_NAME))
                                .setLastName(ds.getString(User.FIELD_LAST_NAME))
                                .setPhoneNumber(ds.getString(User.FIELD_PHONE_NUMBER)).build();
                        post.setOwnerUser(user1);
                        listener.onFinishReceivePost(post,type,null);
                    }
                }
        );
    }
    private void initFirebase()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}
