package com.example.pixelpost.Model.User;

import java.util.Date;
import java.util.List;

public class User {
    //region Class Property
    private String id;
    private String phoneNumber; // sdt
    private String name; // ten
    private String avatarUrl; // urlAvatar
    private List<String> friendList; //Danh sach ban be
    private Date createTime;
    //endregion
    //region Static variables for Firebase representation
    public static final String FIREBASE_COLLECTION_NAME = "users";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_AVATAR_URL = "avatarUrl";
    public static final String FIELD_FRIEND_LIST = "friendList";
    public static final String FIELD_CREATE_TIME = "createTime";
    //endregion
    //region Constructor, Getters, and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    // Private constructor
    private User(Builder builder) {
        this.id = builder.id;
        this.phoneNumber = builder.phoneNumber;
        this.name = builder.name;
        this.avatarUrl = builder.avatarUrl;
        this.friendList = builder.friendList;
        this.createTime = builder.createTime;
    }
    //endregion
    //region Builder class
    public static class Builder {
        private String id;
        private String phoneNumber;
        private String name;
        private String avatarUrl;
        private List<String> friendList; //Danh sach ban be
        private Date createTime;
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Builder setFriendList(List<String> friendList)
        {
            this.friendList = friendList;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }
        public Builder setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
    //endregion
}