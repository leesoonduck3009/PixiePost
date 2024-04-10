package com.example.pixelpost.Presenter.Acitivity;

import com.example.pixelpost.Contract.Activity.IMainActivityContract;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.FriendRequest.FriendRequestModel;
import com.example.pixelpost.Model.FriendRequest.IFriendRequestModel;
import com.example.pixelpost.Model.User.IUserModel;
import com.example.pixelpost.Model.User.UserModel;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivityPresenter implements IMainActivityContract.Presenter {
    private IMainActivityContract.View view;
    private IUserModel userModel;
    private IFriendRequestModel friendRequestModel;
    public MainActivityPresenter(IMainActivityContract.View view)
    {
        this.view = view;
        userModel = UserModel.getInstance();
        friendRequestModel = FriendRequestModel.getInstance();
    }
    @Override
    public void getUserFriendRequest(String id) {
        friendRequestModel.checkFriendRequestSent(id,(user,friendRequest,isExisted, e) -> {
            if(e!=null)
                view.loadingFailed(e);
            else {
                if(isExisted)
                {
                    view.getFriendRequestSuccess(user,friendRequest, FriendRequestDialog.FriendRequestDialogType.ALREADY_SENT);
                    return;
                }
                else{
                    friendRequestModel.checkFriendRequestReceived(id,(user1,friendRequest1,isExisted1,e1)->{
                        if(isExisted1)
                        {
                            view.getFriendRequestSuccess(user1,friendRequest1, FriendRequestDialog.FriendRequestDialogType.ACCEPT);
                            return;
                        }
                        userModel.getUserWithID(id,(user2, e2) -> {
                            if(e2!=null)
                                view.loadingFailed(e2);
                            else{
                                if(user2.getFriendList() == null)
                                    view.getFriendRequestSuccess(user2,friendRequest1, FriendRequestDialog.FriendRequestDialogType.NOT_IS_FRIEND);
                                else{
                                    if(user2.getFriendList().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        view.getFriendRequestSuccess(user2,friendRequest1,FriendRequestDialog.FriendRequestDialogType.IS_FRIEND);
                                    else
                                        view.getFriendRequestSuccess(user2,friendRequest1,FriendRequestDialog.FriendRequestDialogType.NOT_IS_FRIEND);
                                }
                            }
                        });
                    });
                }
            }
        });

    }

    @Override
    public void sendFriendRequest(String id) {
        FriendRequest friendRequest = new FriendRequest.Builder()
                .setSenderId(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setReceiverId(id)
                .setTimeSent(new Date()).build();
        friendRequestModel.sendFriendRequest(friendRequest, (friendRequest1, e) -> {
            if(e!=null)
                view.loadingFailed(e);
            else
            {
                view.sendFriendRequestSuccess(friendRequest1);
            }
        });
    }

    @Override
    public void acceptFriendRequest(FriendRequest friendRequest) {
        friendRequestModel.confirmFriendRequest(friendRequest,true,(friendRequest1, e) -> {
            if(e!=null)
                view.loadingFailed(e);
            else
                view.confirmFriendRequestSucess();
        });
    }

    @Override
    public void getUserInformation() {
        userModel.getCurrentUser((user, e) -> {
            view.getUserInformation(user);
        });
    }

}
