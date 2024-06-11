package com.example.pixelpost.Model.Conversation;

import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.User.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConversationModel implements IConversationModel {
    private static ConversationModel instance;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    //region Singleton define


    // Private constructor to prevent instantiation from outside
    private ConversationModel() {}

    // Static method to get the singleton instance
    public static ConversationModel getInstance() {
        if (instance == null) {
            synchronized (ConversationModel.class) {
                if (instance == null) {
                    instance = new ConversationModel();
                }
            }
        }
        return instance;
    }
    //endregion
    //region Loading Conversation
    @Override
    public void LoadingConversation(OnFinishLoadListener listener) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadConversation(listener,auth.getCurrentUser().getUid(),Conversation.FIELD_USER1_REF);
        loadConversation(listener,auth.getCurrentUser().getUid(),Conversation.FIELD_USER2_REF);
    }

    @Override
    public void loadConversationById(String conversationId, OnFinishLoadListener listener) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        db.collection(Conversation.FIREBASE_COLLECTION_NAME).document(conversationId).get().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot dc = task.getResult();
                        Conversation conversation = new Conversation.Builder().setLastMessageRef(dc
                                        .getDocumentReference(Conversation.FIELD_LAST_MESSAGE_REF)).setUser1Ref(dc.getDocumentReference(Conversation.FIELD_USER1_REF))
                                .setUser2Ref(dc.getDocumentReference(Conversation.FIELD_USER2_REF)).setId(dc.getId()).build();
                        if(!conversation.getUser2().getId().equals(auth.getCurrentUser().getUid())){
                            db.document(conversation.getUser2().getPath()).get().addOnCompleteListener(task1 -> {
                                if(!task1.isSuccessful()){
                                    listener.onFinishLoading(null,task.getException(),0,true);
                                }
                                else{
                                    DocumentSnapshot ds = task1.getResult();
                                    User user = new User.Builder().setId(ds.getId()).setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL)).setLastName(ds.getString(User.FIELD_LAST_NAME))
                                            .setFirstName(ds.getString(User.FIELD_FIRST_NAME)).build();
                                    conversation.setRecieverUser(user);
                                    listener.onFinishLoading(conversation,null,Conversation.ADD_LIST_CONVERSATION,true);
                                }
                            });
                        }
                        else{
                            db.document(conversation.getUser1().getPath()).get().addOnCompleteListener(task1 -> {
                                if(!task1.isSuccessful()){
                                    listener.onFinishLoading(null,task.getException(),0,true);
                                }
                                else{
                                    DocumentSnapshot ds = task1.getResult();
                                    User user = new User.Builder().setId(ds.getId()).setAvatarUrl(ds.getString(User.FIELD_AVATAR_URL)).setLastName(ds.getString(User.FIELD_LAST_NAME))
                                            .setFirstName(ds.getString(User.FIELD_FIRST_NAME)).build();
                                    conversation.setRecieverUser(user);
                                    listener.onFinishLoading(conversation,null,Conversation.ADD_LIST_CONVERSATION,true);
                                }
                            });
                        }
                    }
                    else{
                        listener.onFinishLoading(null,task.getException(),0,true);
                    }
                }
        );

    }

    private void loadConversation(OnFinishLoadListener listener, String user, String userType)
    {
        DocumentReference drUser1 = db.collection(User.FIREBASE_COLLECTION_NAME).document(auth.getCurrentUser().getUid());
        db.collection(Conversation.FIREBASE_COLLECTION_NAME)
                .whereEqualTo(userType,drUser1)
                .addSnapshotListener((value,error) -> {
                    AtomicInteger count1 = new AtomicInteger();
                    if(error!=null)
                    {
                        listener.onFinishLoading(null,error,0,false);
                    }
                    else {
                        if(value.isEmpty()) {
                            listener.onFinishLoading(null, null, Conversation.ADD_LIST_CONVERSATION, true);
                        }
                        else {
                            for (int i =0; i< value.getDocumentChanges().size();i++) {
                                DocumentChange dc = value.getDocumentChanges().get(i);
                                switch (dc.getType()) {
                                    case ADDED:
                                                Conversation conversation = new Conversation.Builder().setLastMessageRef(dc.getDocument()
                                                        .getDocumentReference(Conversation.FIELD_LAST_MESSAGE_REF)).setUser1Ref(dc.getDocument().getDocumentReference(Conversation.FIELD_USER1_REF))
                                                        .setUser2Ref(dc.getDocument().getDocumentReference(Conversation.FIELD_USER2_REF)).setId(dc.getDocument().getId()).build();
                                                List<Task<DocumentSnapshot>> arrayListTask = new ArrayList<>();
                                                Task<DocumentSnapshot> taskMessage =  db.document(conversation.getLastMessageRef().getPath()).get();
                                                Task<DocumentSnapshot> taskUser;
                                                if(conversation.getUser2().getId() == auth.getCurrentUser().getUid())
                                                {
                                                    taskUser =  db.document(conversation.getUser1().getPath()).get();
                                                }
                                                else {
                                                    taskUser = db.document(conversation.getUser2().getPath()).get();
                                                }
                                                arrayListTask.add(taskMessage);
                                                arrayListTask.add(taskUser);
                                                Tasks.whenAllSuccess(arrayListTask).addOnCompleteListener(listTask -> {
                                                    if(listTask.isSuccessful())
                                                    {
                                                        List<Object> documentSnapshots = listTask.getResult();
                                                        DocumentSnapshot dcMessage = (DocumentSnapshot) documentSnapshots.get(0);
                                                        DocumentSnapshot dcUser = (DocumentSnapshot) documentSnapshots.get(1);
                                                        Message message = new Message.Builder().setText(dcMessage.getString(Message.FIELD_TEXT))
                                                                .setConversationId(conversation.getId()).setTimeSent(dcMessage.getDate(Message.FIELD_TIME_SENT))
                                                                .setReceiverId(dcMessage.getString(Message.FIELD_RECEIVER_ID)).setSenderId(dcMessage.getString(Message.FIELD_SENDER_ID)).build();
                                                        User receiverUser = new User.Builder().setAvatarUrl(dcUser.getString(User.FIELD_AVATAR_URL)).setId(dcUser.getId())
                                                                        .setFirstName(dcUser.getString(User.FIELD_FIRST_NAME)).setLastName(dcUser.getString(User.FIELD_LAST_NAME)).build();
                                                        conversation.setLastMessage(message);
                                                        conversation.setRecieverUser(receiverUser);
                                                        listener.onFinishLoading(conversation, null, Conversation.ADD_LIST_CONVERSATION, count1.incrementAndGet() == value.getDocumentChanges().size());
                                                    }
                                                   else
                                                    {
                                                        listener.onFinishLoading(null,  listTask.getException(), 0 ,false);
                                                    }
                                                });
                                        break;
                                    case MODIFIED:
                                        Conversation conversationModify = dc.getDocument().toObject(Conversation.class);
                                        db.document(conversationModify.getLastMessageRef().getPath()).get().addOnCompleteListener(task -> {
                                            if(task.isSuccessful())
                                            {
                                                conversationModify.setLastMessage(task.getResult().toObject(Message.class));
                                                conversationModify.setId(dc.getDocument().getId());
                                                listener.onFinishLoading(conversationModify,null, Conversation.MODIFY_LIST_CONVERSATION, true);
                                            }
                                            else{
                                                listener.onFinishLoading(null,  task.getException(), 0 ,false);
                                            }
                                        });
                                        break;
                                    case REMOVED:
                                        // Xử lý khi có Conversation bị xóa
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    //endregion
}
