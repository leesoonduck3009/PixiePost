package com.example.pixelpost.Model.Message;

import com.example.pixelpost.Model.Conversation.Conversation;
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
    private MessageModel() {}

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
        initFirebase();
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
                                    db.collection(Conversation.FIREBASE_COLLECTION_NAME).document(conversationID).update(Conversation.FIELD_LAST_MESSAGE_REF, taskMessage.getResult().getPath()).addOnCompleteListener(taskConversation->{
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
    }

    @Override
    public void ReceiveMessage(String conversationId, OnFinishReceiveMessageListener listener) {
        initFirebase();
        db.collection(Message.FIREBASE_COLLECTION_NAME).whereEqualTo(Message.FIELD_CONVERSATION_ID,conversationId).limit(100).orderBy(Message.FIELD_TIME_SENT, Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
            AtomicInteger count = new AtomicInteger();

            if(error!=null)
                listener.onFinishReceiveMessage(null, error, false);
            else{
                for(DocumentChange documentChange: value.getDocumentChanges())
                {
                    switch (documentChange.getType())
                    {
                        case ADDED:
                        case MODIFIED:
                            Message message = documentChange.getDocument().toObject(Message.class);
                            message.setId(documentChange.getDocument().getId());
                            listener.onFinishReceiveMessage(message, null, count.incrementAndGet() == value.getDocumentChanges().size() );
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }

        });
    }
    //endregion
}
