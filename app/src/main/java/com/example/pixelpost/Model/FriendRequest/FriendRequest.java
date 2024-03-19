package com.example.pixelpost.Model.FriendRequest;

import java.util.Date;

public class FriendRequest {
    //region Class Property
    private String id;
    private String senderId; // sender
    private String receiverId; // reciever
    private Date timeSent; // timesend
    //endregion
    //region Static variables for Firebase
    public static final String FIREBASE_COLLECTION_NAME = "friend_request";
    public static final String FIELD_SENDER_ID = "senderId";
    public static final String FIELD_RECEIVER_ID = "receiverId";
    public static final String FIELD_TIME_SENT = "timeSent";
    //endregion
    //region Constructor, getters, and setters
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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
        private String receiverId;
        private Date timeSent;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSenderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setReceiverId(String receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Builder setTimeSent(Date timeSent) {
            this.timeSent = timeSent;
            return this;
        }

        public FriendRequest build() {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.id = this.id;
            friendRequest.senderId = this.senderId;
            friendRequest.receiverId = this.receiverId;
            friendRequest.timeSent = this.timeSent;
            return friendRequest;
        }
    }
    //endregion
}
