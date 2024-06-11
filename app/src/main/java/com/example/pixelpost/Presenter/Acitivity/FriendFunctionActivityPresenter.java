package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IFriendFunctionActivityContract;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.FriendRequest.FriendRequestModel;
import com.example.pixelpost.Model.FriendRequest.IFriendRequestModel;
import com.example.pixelpost.Model.Message.IMessageModel;
import com.example.pixelpost.Model.Message.Message;
import com.example.pixelpost.Model.Message.MessageModel;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Model.User.UserModel;

public class FriendFunctionActivityPresenter implements IFriendFunctionActivityContract.Presenter {
    private IFriendFunctionActivityContract.View view;
    private IFriendRequestModel friendRequestModel;
    private IUserModel userModel;
    private IMessageModel messageModel;
    public FriendFunctionActivityPresenter(IFriendFunctionActivityContract.View view)
    {
        this.view = view;
        friendRequestModel = FriendRequestModel.getInstance();
        userModel = UserModel.getInstance();
        messageModel = MessageModel.getInstance();
    }
    @Override
    public void loadFriendRequest() {
        friendRequestModel.receiveFriendRequest((friendRequestUser,friendRequest, type, isLastFriendRequest, e) -> {
            if(e==null)
                view.loadFriendRequest(friendRequestUser,friendRequest,type,isLastFriendRequest);
            else{
                view.loadingFailed(e);
            }
        });
    }

    @Override
    public void loadSendingFriendRequest() {
        friendRequestModel.getSendingFriendRequest((friendRequestUser,friendRequest, type, isLastFriendRequest, e) -> {
            if(e==null)
                view.loadFriendSendingRequest(friendRequestUser,friendRequest,type,isLastFriendRequest);
            else{
                view.loadingFailed(e);
            }
        });
    }

    @Override
    public void loadFriend() {
        userModel.getListFriend(new IUserModel.OnLoadingUserFriendListener() {
            @Override
            public void OnLoadingUserFriend(User friendRequestUser, boolean isLastFriendRequest, Exception e) {
                if(e!=null)
                    view.loadingFailed(e);
                else
                    view.loadFriend(friendRequestUser,null,isLastFriendRequest);
            }

            @Override
            public void OnResetLoadingUser() {
                view.refreshFriend();
            }
        });
    }

    @Override
    public void acceptFriendRequest(FriendRequest friendRequest) {
        friendRequestModel.confirmFriendRequest(friendRequest, true, (friendRequest1, e) -> {
            if(e!=null)
                view.loadingFailed(e);

        });
    }

    @Override
    public void denyFriendRequest(FriendRequest friendRequest) {
        friendRequestModel.confirmFriendRequest(friendRequest,false,(friendRequest1, e) -> {
            if(e!=null)
                view.loadingFailed(e);
        });
    }

    @Override
    public void removeFriend(User user) {
        userModel.deleteFriend(user, (user1, e) -> {
            if(e!=null)
                view.loadingFailed(e);
        });
    }

    @Override
    public void cancelSendFriendRequest(FriendRequest friendRequest) {
        friendRequestModel.confirmFriendRequest(friendRequest,false,(friendRequest1, e) -> {
            if(e!=null)
                view.loadingFailed(e);
        });
    }

    @Override
    public void onChatToFriend(Message message) {
        messageModel.SendMessage(message, new IMessageModel.OnFinishSendMessageListener() {
            @Override
            public void onFinishSendMessage(Message message, Exception e) {
                if(e!=null)
                    view.loadingFailed(e);
                else
                    view.chatToFrriend(message);
            }
        });
    }
}
