package com.example.pixelpost.Model.FriendRequest;


import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public void checkFriendRequestSent(String id, OnFinishCheckFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).whereEqualTo(FriendRequest.FIELD_SENDER_ID,user.getUid())
                .whereEqualTo(FriendRequest.FIELD_RECEIVER_ID,id)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(!task.getResult().getDocumentChanges().isEmpty())
                        {
                            UserModel.getInstance().getUserWithID(id, (user1, e) -> {
                                if(e!=null)
                                    listener.onFinishCheckFriendRequest(null,null,false,task.getException());
                                else
                                    listener.onFinishCheckFriendRequest(user1,null,true,null);
                            });
                        }
                        else
                            listener.onFinishCheckFriendRequest(null,null,false,null);
                    }
                    else {
                        listener.onFinishCheckFriendRequest(null,null,false,task.getException());
                    }
                });
        }

    @Override
    public void checkFriendRequestReceived(String id, OnFinishCheckFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).whereEqualTo(FriendRequest.FIELD_RECEIVER_ID,user.getUid())
                .whereEqualTo(FriendRequest.FIELD_SENDER_ID,id)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(!task.getResult().getDocumentChanges().isEmpty())
                        {
                            DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                            FriendRequest friendRequest = new FriendRequest.Builder().setId(ds.getId())
                                            .setReceiverId(ds.getString(FriendRequest.FIELD_RECEIVER_ID))
                                                    .setSenderId(ds.getString(FriendRequest.FIELD_SENDER_ID))
                                                            .setTimeSent(ds.getDate(FriendRequest.FIELD_TIME_SENT)).build();
                            UserModel.getInstance().getUserWithID(id, (user1, e) -> {
                                if(e!=null)
                                    listener.onFinishCheckFriendRequest(null,null,false,task.getException());
                                else
                                    listener.onFinishCheckFriendRequest(user1,friendRequest,true,null);
                            });
                        }
                        else
                            listener.onFinishCheckFriendRequest(null,null,false,null);
                    }
                    else {
                        listener.onFinishCheckFriendRequest(null,null,false,task.getException());
                    }
                });
    }

    @Override
    public void confirmFriendRequest(FriendRequest friendRequest, boolean isAccepted, OnFinishFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).document(friendRequest.getId()).delete().addOnCompleteListener(taskDelete -> {
            if(taskDelete.isSuccessful())
            {
                if(isAccepted)
                {
                    db.collection(User.FIREBASE_COLLECTION_NAME).document(user.getUid()).update(User.FIELD_FRIEND_LIST, FieldValue.arrayUnion(friendRequest.getSenderId()))
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful())
                                {
                                    db.collection(User.FIREBASE_COLLECTION_NAME).document(friendRequest.getSenderId()).update(User.FIELD_FRIEND_LIST, FieldValue.arrayUnion(user.getUid()))
                                        .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        listener.onFinishFriendRequest(null, null);
                                                    }
                                                    else{
                                                        listener.onFinishFriendRequest(null,task.getException());
                                                    }
                                                });
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
            if(e==null) {
                AtomicInteger countFR = new AtomicInteger();
                if (value.isEmpty()) {
                    listener.onFinishReceivedFriendRequest(null,null, null, true, null);
                } else {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        DocumentSnapshot document = dc.getDocument();
                        FriendRequest friendRequest = new FriendRequest.Builder().setReceiverId(document.getString(FriendRequest.FIELD_RECEIVER_ID))
                                .setSenderId(document.getString(FriendRequest.FIELD_SENDER_ID))
                                .setTimeSent(document.getDate(FriendRequest.FIELD_TIME_SENT)).setId(document.getId()).build();
                        UserModel.getInstance().getUserWithID(document.getString(FriendRequest.FIELD_SENDER_ID),(user1, e1) -> {
                            if(e!=null)
                                listener.onFinishReceivedFriendRequest(null,null,null,true,e);
                            else
                                listener.onFinishReceivedFriendRequest(user1, friendRequest,dc.getType(),countFR.incrementAndGet()== value.size(),null);
                        });
                    }
                }
            }
            else{
                listener.onFinishReceivedFriendRequest(null,null,null,true,e);
            }
        });
    }

    @Override
    public void getSendingFriendRequest(OnFinishReceivedFriendRequestListener listener) {
        initFirebase();
        db.collection(FriendRequest.FIREBASE_COLLECTION_NAME).whereEqualTo(FriendRequest.FIELD_SENDER_ID,user.getUid()).addSnapshotListener((value,e) -> {
            if(e==null) {
                AtomicInteger countFR = new AtomicInteger();
                if (value.isEmpty()) {
                    listener.onFinishReceivedFriendRequest(null,null, null, true, null);
                } else {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        DocumentSnapshot document = dc.getDocument();
                        FriendRequest friendRequest = new FriendRequest.Builder().setReceiverId(document.getString(FriendRequest.FIELD_RECEIVER_ID))
                                .setSenderId(document.getString(FriendRequest.FIELD_SENDER_ID))
                                .setTimeSent(document.getDate(FriendRequest.FIELD_TIME_SENT)).setId(document.getId()).build();
                        UserModel.getInstance().getUserWithID(document.getString(FriendRequest.FIELD_RECEIVER_ID),(user1, e1) -> {
                            if(e!=null)
                                listener.onFinishReceivedFriendRequest(null,null,null,true,e);
                            else
                                listener.onFinishReceivedFriendRequest(user1, friendRequest,dc.getType(),countFR.incrementAndGet()== value.size(),null);
                        });
                    }
                }
            }
            else{
                listener.onFinishReceivedFriendRequest(null,null,null,true,e);
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
