package com.example.pixelpost.Model.Message;

import java.util.Date;

public class Message {
    //region Class Property
    private String id;
    private String senderId;
    private String receiverId;
    private String text;
    private String imageUrl; // urlImage
    private String conversationId;
    private Date timeSent;
    //endregion
    //region Static variables for Firebase
    public static final String FIREBASE_COLLECTION_NAME = "messsage";
    public static final String FIELD_SENDER_ID = "senderId";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_IMAGE_URL = "imageUrl";
    public static final String FIELD_CONVERSATION_ID = "conversationId";
    public static final String FIELD_TIME_SENT = "timeSent";
    public static final String FIELD_RECEIVER_ID = "receiverId";
    //endregion
    //region Constructor, getters, and setters

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }
    //endregion
    //region Builder class

    public static class Builder {
        private String id;
        private String senderId;
        private String text;
        private String imageUrl;
        private String conversationId;
        private Date timeSent;
        private String receiverId;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }
        public Builder setReceiverId(String receiverId)
        {
            this.receiverId = receiverId;
            return this;
        }
        public Builder setSenderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder setTimeSent(Date timeSent) {
            this.timeSent = timeSent;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.id = this.id;
            message.senderId = this.senderId;
            message.text = this.text;
            message.imageUrl = this.imageUrl;
            message.conversationId = this.conversationId;
            message.timeSent = this.timeSent;
            message.receiverId = this.receiverId;
            return message;
        }
    }
    //endregion
}
