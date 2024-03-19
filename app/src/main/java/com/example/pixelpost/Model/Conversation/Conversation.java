package com.example.pixelpost.Model.Conversation;

public class Conversation {
    //region Class Property
    private String id;
    private String user1Id; // user1
    private String user2Id; // user2
    private String lastMessageId;
    private String conversationURI;
    //endregion
    //region Static variables for Firebase
    public static final String FIREBASE_COLLECTION_NAME = "conversation";
    public static final String FIELD_USER1_ID = "user1Id";
    public static final String FIELD_USER2_ID = "user2Id";
    public static final String FIELD_LAST_MESSAGE_ID = "lastMessageId";
    //endregion
    //region Constructor, getters, and setters
    public String getConversationURI() {
        return conversationURI;
    }

    public void setConversationURI(String conversationURI) {
        this.conversationURI = conversationURI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    //endregion
    //region Builder class
    public static class Builder {
        private String id;
        private String user1Id;
        private String user2Id;
        private String lastMessageId;
        private String conversationURI;
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUser1Id(String user1Id) {
            this.user1Id = user1Id;
            return this;
        }

        public Builder setUser2Id(String user2Id) {
            this.user2Id = user2Id;
            return this;
        }

        public Builder setLastMessageId(String lastMessageId) {
            this.lastMessageId = lastMessageId;
            return this;
        }
        public Builder setconversationURI(String conversationURI)
        {
            this.conversationURI = conversationURI;
            return this;
        }
        public Conversation build() {
            Conversation conversation = new Conversation();
            conversation.id = this.id;
            conversation.user1Id = this.user1Id;
            conversation.user2Id = this.user2Id;
            conversation.lastMessageId = this.lastMessageId;
            conversation.conversationURI = this.conversationURI;
            return conversation;
        }
    }
    //endregion
}   
