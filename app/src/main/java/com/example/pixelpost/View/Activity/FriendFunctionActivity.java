package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelpost.Contract.Activity.IFriendFunctionActivityContract;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.FriendFunctionActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IAcceptUserListener;
import com.example.pixelpost.Utils.Listener.IAddUserListener;
import com.example.pixelpost.Utils.Listener.IDenyUserListener;
import com.example.pixelpost.Utils.Listener.IRemoveUserListener;
import com.example.pixelpost.View.Activity.QR.QRProfileActivity;
import com.example.pixelpost.View.Adapter.ListExistingFriendAdapter;
import com.example.pixelpost.View.Adapter.ListRecommendFriendAdapter;
import com.example.pixelpost.View.Adapter.ListRequestFriendAdapter;
import com.example.pixelpost.View.Adapter.ListSendingFriendAdapter;
import com.example.pixelpost.View.Dialog.CustomDialog;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendFunctionActivity extends AppCompatActivity implements IDenyUserListener, IFriendFunctionActivityContract.View, IRemoveUserListener, IAcceptUserListener, IAddUserListener {
    private RecyclerView recycleView_ListFriend;
    private RecyclerView recycleView_ListRequestFriend;
    private RecyclerView recycleView_ListRecommendFriend;
    private RecyclerView recycleView_ListSendingFriend;
    private List<User> userList;
    private ListExistingFriendAdapter listExistingFriendAdapter;
    private ListRequestFriendAdapter listRequestFriendAdapter;
    private ListRecommendFriendAdapter listRecommendFriendAdapter;
    private IFriendFunctionActivityContract.Presenter presenter;
    private List<User> receivedUserFriendRequest;
    private List<FriendRequest> receivedFriendRequest;
    private List<User> sendingUserFriendRequest;
    private List<FriendRequest> sendingFriendRequest;

    private List<User> friendUser;
    private ListSendingFriendAdapter listSendingFriendAdapter;
    private ImageView imgViewQRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_function);
        receivedUserFriendRequest = new ArrayList<>();
        receivedFriendRequest = new ArrayList<>();
        friendUser = new ArrayList<>();
        sendingUserFriendRequest = new ArrayList<>();
        sendingFriendRequest = new ArrayList<>();
        presenter = new FriendFunctionActivityPresenter(this);
        userList = new ArrayList<>();
        //
        imgViewQRCode = findViewById(R.id.imgViewQRCode);
        recycleView_ListFriend = findViewById(R.id.recycleView_ListFriend);
        recycleView_ListFriend.setLayoutManager(new LinearLayoutManager(this));
        recycleView_ListRequestFriend = findViewById(R.id.recycleView_ListRequestFriend);
        recycleView_ListRequestFriend.setLayoutManager(new LinearLayoutManager(this));
        recycleView_ListRecommendFriend = findViewById(R.id.recycleView_ListRecommendFriend);
        recycleView_ListRecommendFriend.setLayoutManager(new LinearLayoutManager(this));
        recycleView_ListSendingFriend = findViewById(R.id.recycleView_ListSendingFriend);
        recycleView_ListSendingFriend.setLayoutManager(new LinearLayoutManager(this));

        InitUser();
        listExistingFriendAdapter = new ListExistingFriendAdapter(friendUser, this);
        listRequestFriendAdapter = new ListRequestFriendAdapter(receivedUserFriendRequest,receivedFriendRequest, this,this);
        listRecommendFriendAdapter = new ListRecommendFriendAdapter(userList, this);
        listSendingFriendAdapter = new ListSendingFriendAdapter(sendingUserFriendRequest,sendingFriendRequest,this);
        // ----------------------------
        recycleView_ListFriend.setAdapter(listExistingFriendAdapter);
        listExistingFriendAdapter.notifyDataSetChanged();

        recycleView_ListRequestFriend.setAdapter(listRequestFriendAdapter);
        listRequestFriendAdapter.notifyDataSetChanged();

        recycleView_ListRecommendFriend.setAdapter(listRecommendFriendAdapter);
        listRecommendFriendAdapter.notifyDataSetChanged();
        recycleView_ListSendingFriend.setAdapter(listSendingFriendAdapter);
        loadData();
        setListener();
    }
    private void loadData()
    {
        presenter.loadFriendRequest();
        presenter.loadSendingFriendRequest();
        presenter.loadFriend();
    }
    public void InitUser() {
        userList.add(new User.Builder().setFirstName("Nam").setLastName("Nguyễn Văn").build());
        userList.add(new User.Builder().setFirstName("Duy").setLastName("Lê Quang").build());
        userList.add(new User.Builder().setFirstName("Đạt").setLastName("Phạm Tiến").build());
    }
    private void setListener()
    {
        imgViewQRCode.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), QRProfileActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void loadFriendRequest(User user,FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendRequestUser) {
        if (user != null)
        {
            switch (type) {
                case ADDED:
                    receivedUserFriendRequest.add(user);
                    receivedFriendRequest.add(friendRequest);
                    break;
                case MODIFIED:
                case REMOVED:
                    receivedUserFriendRequest.stream()
                            .filter(user1 -> Objects.equals(user1.getId(), user.getId()))
                            .findFirst().ifPresent(userRemove -> receivedUserFriendRequest.remove(userRemove));
                    receivedFriendRequest.stream()
                            .filter(friendRequest1 -> Objects.equals(friendRequest.getId(), friendRequest1.getId()))
                            .findFirst().ifPresent(friendRequestRemove -> receivedFriendRequest.remove(friendRequestRemove));
                    break;
            }
        listRequestFriendAdapter.notifyDataSetChanged();
        }
        else{
            receivedFriendRequest.clear();
            receivedUserFriendRequest.clear();
            listRequestFriendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadFriend(User user, DocumentChange.Type type, boolean isLastFriendUser) {
        if(user!=null)
        {
            friendUser.add(user);
            listExistingFriendAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void refreshFriend() {
        friendUser.clear();
        listExistingFriendAdapter.notifyDataSetChanged();

    }

    @Override
    public void loadFriendSendingRequest(User user,FriendRequest friendRequest, DocumentChange.Type type, boolean isLastFriendUser) {
            if (user != null)
        {
            switch (type) {
                case ADDED:
                    sendingUserFriendRequest.add(user);
                    sendingFriendRequest.add(friendRequest);
                    break;
                case MODIFIED:
                case REMOVED:
                    sendingUserFriendRequest.stream()
                            .filter(user1 -> Objects.equals(user1.getId(), user.getId()))
                            .findFirst().ifPresent(userRemove -> sendingUserFriendRequest.remove(userRemove));
                    sendingFriendRequest.stream()
                            .filter(friendRequest1 -> Objects.equals(friendRequest.getId(), friendRequest1.getId()))
                            .findFirst().ifPresent(friendRequestRemove -> sendingFriendRequest.remove(friendRequestRemove));
                    break;
            }
            listSendingFriendAdapter.notifyDataSetChanged();
        }else {
                sendingFriendRequest.clear();
                sendingUserFriendRequest.clear();
                listSendingFriendAdapter.notifyDataSetChanged();
            }
    }

    @Override
    public void loadingFailed(Exception e) {
        Log.e("friend-function",e.getMessage());
    }

    @Override
    public void OnDenyUserClick(User user, FriendRequest friendRequest) {
        presenter.denyFriendRequest(friendRequest);
    }
    @Override
    public void OnRemoveUserClick(User user) {
        CustomDialog.showDialog(this, "Bạn có muốn huỷ kết bạn với người bạn thân của bạn chứ?", CustomDialog.DialogType.YES_NO, new CustomDialog.DialogClickListener() {
            @Override
            public void onYesClick() {
                presenter.removeFriend(user);
            }

            @Override
            public void onNoClick() {

            }
            @Override
            public void onOkClick() {

            }
        });
    }

    @Override
    public void OnAcceptUserClick(User user, FriendRequest friendRequest) {
        presenter.acceptFriendRequest(friendRequest);
        Toast.makeText(this, "Accepted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAddUserClick(User user) {
        Toast.makeText(this, "Added User!", Toast.LENGTH_SHORT).show();
    }
}