package com.example.pixelpost.Model.Post;

import com.example.pixelpost.Model.User.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Post {
    //region Class Property
    private String id;
    private String url;
    private String text;
    private String ownerId; // idOwner
    private Date timePosted; // timePost
    private List<String> displayedUsers; // userHienThi
    private String lastReaction; // lastReaction
    private User ownerUser;
    //endregion
    //region Static variables for Firebase
    public static final String FIELD_URL = "url";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_TIME_POSTED = "timePosted";
    public static final String FIELD_DISPLAYED_USERS = "displayedUsers";
    public static final String FIELD_LAST_REACTION = "lastReaction";
    public static final String FIREBASE_COLLECTION_NAME = "post";
    //endregion
    //region Constructor, getters, and setters

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Date timePosted) {
        this.timePosted = timePosted;
    }

    public List<String> getDisplayedUsers() {
        return displayedUsers;
    }

    public void setDisplayedUsers(List<String> displayedUsers) {
        this.displayedUsers = displayedUsers;
    }

    public String getLastReaction() {
        return lastReaction;
    }

    public void setLastReaction(String lastReaction) {
        this.lastReaction = lastReaction;
    }
    //endregion
    //region Builder class
    public static class Builder {
        private String id;
        private String url;
        private String text;
        private String ownerId;
        private Date timePosted;
        private List<String> displayedUsers;
        private String lastReaction;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setOwnerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder setTimePosted(Date timePosted) {
            this.timePosted = timePosted;
            return this;
        }

        public Builder setDisplayedUsers(List<String> displayedUsers) {
            this.displayedUsers = displayedUsers;
            return this;
        }

        public Builder setLastReaction(String lastReaction) {
            this.lastReaction = lastReaction;
            return this;
        }

        public Post build() {
            Post post = new Post();
            post.id = this.id;
            post.url = this.url;
            post.text = this.text;
            post.ownerId = this.ownerId;
            post.timePosted = this.timePosted;
            post.displayedUsers = this.displayedUsers;
            post.lastReaction = this.lastReaction;
            return post;
        }
    }
    //endregion
}
