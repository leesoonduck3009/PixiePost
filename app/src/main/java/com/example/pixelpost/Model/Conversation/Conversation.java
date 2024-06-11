package com.example.pixelpost.Model.Conversation;

import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.User.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firestore.v1.Document;

import java.io.Serializable;
import java.util.Date;

public class Conversation implements Serializable {
    //region Class Property
    private String id;
    private DocumentReference user1; // user1
    private DocumentReference user2; // user2
    private DocumentReference lastMessageRef;
    @Exclude
    private Message lastMessage;
    @Exclude
    private User recieverUser;
    private String personNotSeenID;

    //endregion
    //region Static variables for Firebase
    public static final String FIREBASE_COLLECTION_NAME = "conversation";
    public static final String FIELD_USER1_REF = "user1";
    public static final String FIELD_USER2_REF = "user2";
    public static final String FIELD_LAST_MESSAGE_REF = "lastMessageRef";
    //endregion
    //region Static variables for TYPE;
    public static final int MODIFY_LIST_CONVERSATION = 1;
    public static final int ADD_LIST_CONVERSATION = 2;


    public String getPersonNotSeenID() {
        return personNotSeenID;
    }

    public void setPersonNotSeenID(String personNotSeenID) {
        this.personNotSeenID = personNotSeenID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getUser1() {
        return user1;
    }

    public DocumentReference getUser2() {
        return user2;
    }

    public DocumentReference getLastMessageRef() {
        return lastMessageRef;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public User getRecieverUser() {
        return recieverUser;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setRecieverUser(User recieverUser) {
        this.recieverUser = recieverUser;
    }

    //endregion
    //region Builder class
    public static class Builder {
        private String id;
        private DocumentReference user1; // user1
        private DocumentReference user2; // user2
        private DocumentReference lastMessageRef;
        private Message lastMessage;
        private User recieverUser;
        private String personNotSeenID;
        public Builder setPersonNotSeenID(String personNotSeenID) {
            this.personNotSeenID = personNotSeenID;
            return this;
        }
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUser1Ref(DocumentReference user1) {
            this.user1 = user1;
            return this;
        }

        public Builder setUser2Ref(DocumentReference user2) {
            this.user2 = user2;
            return this;
        }

        public Builder setLastMessageRef(DocumentReference lastMessageRef) {
            this.lastMessageRef = lastMessageRef;
            return this;
        }
        public Builder setLastMessage(Message message)
        {
            this.lastMessage = message;
            return this;
        }
        public Builder setReceiverUser(User recieverUser)
        {
            this.recieverUser = recieverUser;
            return this;
        }
        public Conversation build() {
            Conversation conversation = new Conversation();
            conversation.id = this.id;
            conversation.user1 = this.user1;
            conversation.user2 = this.user2;
            conversation.lastMessageRef = this.lastMessageRef;
            conversation.lastMessage = this.lastMessage;
            conversation.recieverUser = this.recieverUser;
            conversation.personNotSeenID = this.personNotSeenID;
            return conversation;
        }
    }
    //endregion
}
