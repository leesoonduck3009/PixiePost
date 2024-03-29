package com.example.pixelpost.Model.FriendRequest;


import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendRequestModel implements IFriendRequestModel{
    //region Singleton define
    private static FriendRequestModel instance;

    // Private constructor to prevent instantiation from outside
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FriendRequestModel() {}

    // Static method to get the singleton instance
    public static FriendRequestModel getInstance() {
        if (instance == null) {
            synchronized (FriendRequestModel.class) {
                if (instance == null) {
                    instance = new FriendRequestModel();
                }
            }
        }
        return instance;
    }
    //endregion

    @Override
    public void sendFriendRequest(FriendRequest friendRequest, OnFinishFriendRequestListener listener) {
        initFirebase();
        friendRequest.setSenderId(user.getUid());
        friendRequest.setTimeSent(new Date());
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).add(friendRequest).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful())
                    {
                        friendRequest.setId(task.getResult().getId());
                        listener.onFinishFriendRequest(friendRequest,null);
                    }
                    else{
                        listener.onFinishFriendRequest(null, task.getException());
                    }
                }
        );
    }

    @Override
    public void confirmFriendRequest(FriendRequest friendRequest, boolean isAccepted, OnFinishFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).document(friendRequest.getId()).delete().addOnCompleteListener(taskDelete -> {
            if(taskDelete.isSuccessful())
            {
                if(isAccepted)
                {
                    db.collection(User.FIELD_FRIEND_LIST).document(user.getUid()).update(User.FIELD_FRIEND_LIST, FieldValue.arrayUnion(friendRequest.getSenderId()))
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful())
                                {

                                }
                                else{
                                    listener.onFinishFriendRequest(null,task.getException());
                                }
                            });
                }
                else
                    listener.onFinishFriendRequest(friendRequest,null);
                }
            else{
                listener.onFinishFriendRequest(null, taskDelete.getException());
            }
        });
    }

    @Override
    public void receiveFriendRequest(OnFinishReceivedFriendRequestListener listener) {
        initFirebase();

        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).whereEqualTo(FriendRequest.FIELD_RECEIVER_ID,user.getUid()).addSnapshotListener((value,e) -> {
            if(e!=null) {
                AtomicInteger countFR = new AtomicInteger();
                if (value.isEmpty()) {
                    listener.onFinishReceivedFriendRequest(null, null, true, null);
                } else {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        FriendRequest friendRequest = dc.getDocument().toObject(FriendRequest.class);
                        listener.onFinishReceivedFriendRequest(friendRequest, dc.getType(), countFR.incrementAndGet() == value.getDocumentChanges().size(),null);
                    }
                }
            }
            else{
                listener.onFinishReceivedFriendRequest(null,null,true,e);
            }
        });
    }

    @Override
    public void setFriendRequestChangeListener(OnGettingNumberFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).whereEqualTo(FriendRequest.FIELD_RECEIVER_ID,user.getUid())
                .addSnapshotListener(((value, error) -> {
                    if(error!=null)
                    {
                        listener.onGetNumber(value.getDocumentChanges().size(),null);
                    }
                    else{
                        listener.onGetNumber(0,error);
                    }
                }));
    }
    private void initFirebase()
    {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
    }
}
