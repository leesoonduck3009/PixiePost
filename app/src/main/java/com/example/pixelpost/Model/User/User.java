package com.example.pixelpost.Model.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    //region Class Property
    private String id;
    private String phoneNumber; // sdt

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String avatarUrl; // urlAvatar
    private List<String> friendList; //Danh sach ban be
    private Date createTime;
    //endregion
    //region Static variables for Firebase representation
    public static final String FIREBASE_COLLECTION_NAME = "users";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_PASSWORD = "password";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        this.avatarUrl = builder.avatarUrl;
        this.friendList = builder.friendList;
        this.createTime = builder.createTime;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.password = builder.password;

    }
    //endregion
    //region Builder class
    public static class Builder {
        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private String phoneNumber;
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
        public Builder setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }
        public Builder setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
        // Setters for new properties
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
    //endregion
}