package com.example.pixelpost.Model.Conversation;

import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.User.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
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
    private void loadConversation(OnFinishLoadListener listener, String user, String userType)
    {
        db.collection(Conversation.FIREBASE_COLLECTION_NAME)
                .whereEqualTo(user,auth.getCurrentUser().getUid())
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
                                        Conversation conversation = dc.getDocument().toObject(Conversation.class);
                                        conversation.setId(dc.getDocument().getId());
                                        db.collection(Conversation.FIREBASE_COLLECTION_NAME).whereEqualTo(userType, user).get().addOnCompleteListener(task -> {
                                            if (!task.isSuccessful())
                                                listener.onFinishLoading(null,  task.getException(), 0 ,false);
                                            else {
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
                                                        Message message = ((DocumentSnapshot) documentSnapshots.get(0)).toObject(Message.class);
                                                        User receiverUser = ((DocumentSnapshot) documentSnapshots.get(1)).toObject(User.class);
                                                        conversation.setLastMessage(message);
                                                        conversation.setRecieverUser(receiverUser);
                                                        listener.onFinishLoading(conversation, null, Conversation.ADD_LIST_CONVERSATION, count1.incrementAndGet() == value.getDocumentChanges().size());
                                                    }
                                                   else
                                                    {
                                                        listener.onFinishLoading(null,  task.getException(), 0 ,false);
                                                    }
                                                });
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
