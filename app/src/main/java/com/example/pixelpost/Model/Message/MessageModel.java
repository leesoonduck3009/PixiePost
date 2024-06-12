package com.example.pixelpost.Model.Message;

import com.example.pixelpost.Model.Conversation.Conversation;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageModel implements IMessageModel{
    //region Singleton define
    private static MessageModel instance;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    // Private constructor to prevent instantiation from outside
    public MessageModel() {
        initFirebase();

    }

    // Static method to get the singleton instance
    public static MessageModel getInstance() {
        if (instance == null) {
            synchronized (MessageModel.class) {
                if (instance == null) {
                    instance = new MessageModel();
                }
            }
        }
        return instance;
    }
    private void initFirebase()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    public void SendMessage(Message message, OnFinishSendMessageListener listener) {
        if(message.getConversationId()==null || message.getConversationId().isEmpty())
        {
            DocumentReference drUser1 = db.collection(User.FIREBASE_COLLECTION_NAME).document(message.getSenderId());
            DocumentReference drUser2 = db.collection(User.FIREBASE_COLLECTION_NAME).document(message.getReceiverId());
            Conversation conversation = new Conversation.Builder().setPersonNotSeenID(message.getSenderId())
                    .setUser1Ref(drUser1)
                            .setUser2Ref(drUser2).setPersonNotSeenID(message.getReceiverId()).build();
            db.collection(Conversation.FIREBASE_COLLECTION_NAME).add(conversation).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    String conversationID = task.getResult().getId();
                    message.setConversationId(conversationID);
                    conversation.setId(conversationID);
                    db.collection(Message.FIREBASE_COLLECTION_NAME).add(message).addOnCompleteListener(
                            taskMessage->{
                                if(taskMessage.isSuccessful())
                                {
                                    db.collection(Conversation.FIREBASE_COLLECTION_NAME).document(conversationID).update(Conversation.FIELD_LAST_MESSAGE_REF, db.collection(Message.FIREBASE_COLLECTION_NAME).document(taskMessage.getResult().getId())).addOnCompleteListener(taskConversation->{
                                        if(taskConversation.isSuccessful())
                                        {
                                            listener.onFinishSendMessage(message,null);
                                        }
                                        else
                                            listener.onFinishSendMessage(null,taskConversation.getException());
                                    });
                                }
                                else{
                                    listener.onFinishSendMessage(null, taskMessage.getException());
                                }
                            }
                    );
                }
                else
                    listener.onFinishSendMessage(message,task.getException());
            });
        }
        else{
            db.collection(Message.FIREBASE_COLLECTION_NAME).add(message).addOnCompleteListener(
                    taskMessage->{
                        if(taskMessage.isSuccessful())
                        {
                            db.collection(Conversation.FIREBASE_COLLECTION_NAME).document(message.getConversationId()).update(Conversation.FIELD_LAST_MESSAGE_REF, db.collection(Message.FIREBASE_COLLECTION_NAME).document(taskMessage.getResult().getId())).addOnCompleteListener(taskConversation->{
                                if(taskConversation.isSuccessful())
                                {
                                    listener.onFinishSendMessage(message,null);
                                }
                                else
                                    listener.onFinishSendMessage(null,taskConversation.getException());
                            });
                        }
                        else{
                            listener.onFinishSendMessage(null, taskMessage.getException());
                        }
                    }
            );
        }
    }

    @Override
    public void ReceiveMessage(String conversationId, OnFinishReceiveMessageListener listener) {
        db.collection(Message.FIREBASE_COLLECTION_NAME).whereEqualTo(Message.FIELD_CONVERSATION_ID,conversationId)
                .addSnapshotListener((value, error) -> {
            AtomicInteger count = new AtomicInteger();

            if(error!=null)
                listener.onFinishReceiveMessage(null,null, error, false);
            else{
                for(DocumentChange documentChange: value.getDocumentChanges())
                {
                    switch (documentChange.getType())
                    {
                        case ADDED:
                        case MODIFIED:
                            Message message = new Message();
                            message.setText(documentChange.getDocument().getString(Message.FIELD_TEXT));
                            message.setId(documentChange.getDocument().getId());
                            message.setTimeSent(documentChange.getDocument().getDate(Message.FIELD_TIME_SENT));
                            message.setReceiverId(documentChange.getDocument().getString(Message.FIELD_RECEIVER_ID));
                            message.setSenderId(documentChange.getDocument().getString(Message.FIELD_SENDER_ID));
                            message.setPostId(documentChange.getDocument().getString(Message.FIELD_POST_ID));
                            if(message.getPostId()!=null)
                            {
                                getPostByMessage(message,count, value.getDocumentChanges().size(),listener);
                            }
                            else {
                                listener.onFinishReceiveMessage(message,null, null, count.incrementAndGet() == value.getDocumentChanges().size() );
                            }
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }

        });
    }
    private void getPostByMessage(Message message, AtomicInteger count, int total, OnFinishReceiveMessageListener listener){
        db.collection(Post.FIREBASE_COLLECTION_NAME).document(message.getPostId()).get().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot ds = task.getResult();
                        Post post = new Post.Builder().setTimePosted(ds.getDate(Post.FIELD_TIME_POSTED)).setId(ds.getId()).setDisplayedUsers((ArrayList<String>) ds.get(Post.FIELD_DISPLAYED_USERS))
                                        .setOwnerId(ds.getString(Post.FIELD_OWNER_ID)).setUrl(ds.getString(Post.FIELD_URL)).setText(ds.getString(Post.FIELD_TEXT)).setLastReaction(ds.getString(Post.FIELD_LAST_REACTION)).build();
                        listener.onFinishReceiveMessage(message,post,null,count.incrementAndGet() == total);
                    }
                    else
                        listener.onFinishReceiveMessage(message,null,task.getException(),true);
                }
        );
    }
    //endregion
}
