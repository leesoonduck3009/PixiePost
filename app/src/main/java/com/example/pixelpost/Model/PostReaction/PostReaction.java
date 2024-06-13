package com.example.pixelpost.Model.PostReaction;

import java.util.List;
import java.util.Map;

public class PostReaction {
    //region Class Property
    private String id;
    private String postId;
    private List<Map<String, String>> reactionList; // reactlist
    //endregion
    //region Static variables for Firebase
    //public static final String FIELD_POST_ID = "postId";
    public static final String HEART = "heart";
    public static final String HAHA = "haha";
    public static final String ANGRY = "angry";
    public static final String SAD = "sad";
    //public static final String FIELD_REACT_LIST = "reactionList";
    public static final String FIREBASE_COLLECTION_NAME = "post_reaction";
    //endregion
    //region Constructor, getters, and setters

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<Map<String, String>> getReactionList() {
        return reactionList;
    }

    public void setReactionList(List<Map<String, String>> reactionList) {
        this.reactionList = reactionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //endregion
    //region Builder class

    public static class Builder {
        private String postId;
        private String id;

        private List<Map<String, String>> reacttionList;

        public Builder setPostId(String postId) {
            this.postId = postId;
            return this;
        }

        public Builder setReactionList(List<Map<String, String>> reacttionList) {
            this.reacttionList = reacttionList;
            return this;
        }
        public Builder setId(String id)
        {
            this.id = id;
            return this;
        }
        public PostReaction build() {
            PostReaction postReaction = new PostReaction();
            postReaction.postId = this.postId;
            postReaction.reactionList = this.reacttionList;
            postReaction.id = this.id;
            return postReaction;
        }
    }
    //endregion
}
