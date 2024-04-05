package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.Listener.IAcceptUserListener;
import com.example.pixelpost.Utils.Listener.IAddUserListener;
import com.example.pixelpost.Utils.Listener.IRemoveUserListener;
import com.example.pixelpost.View.Activity.QR.QRProfileActivity;
import com.example.pixelpost.View.Adapter.ListExistingFriendAdapter;
import com.example.pixelpost.View.Adapter.ListRecommendFriendAdapter;
import com.example.pixelpost.View.Adapter.ListRequestFriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendFunctionActivity extends AppCompatActivity implements IRemoveUserListener, IAcceptUserListener, IAddUserListener {
    private RecyclerView recycleView_ListFriend;
    private RecyclerView recycleView_ListRequestFriend;
    private RecyclerView recycleView_ListRecommendFriend;
    private List<User> userList;
    private ListExistingFriendAdapter listExistingFriendAdapter;
    private ListRequestFriendAdapter listRequestFriendAdapter;
    private ListRecommendFriendAdapter listRecommendFriendAdapter;
    private ImageView imgViewQRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_function);
        imgViewQRCode = findViewById(R.id.imgViewQRCode);
        recycleView_ListFriend = findViewById(R.id.recycleView_ListFriend);
        recycleView_ListFriend.setLayoutManager(new LinearLayoutManager(this));

        recycleView_ListRequestFriend = findViewById(R.id.recycleView_ListRequestFriend);
        recycleView_ListRequestFriend.setLayoutManager(new LinearLayoutManager(this));

        recycleView_ListRecommendFriend = findViewById(R.id.recycleView_ListRecommendFriend);
        recycleView_ListRecommendFriend.setLayoutManager(new LinearLayoutManager(this));


        userList = new ArrayList<>();
        InitUser();
        listExistingFriendAdapter = new ListExistingFriendAdapter(userList, this);
        listRequestFriendAdapter = new ListRequestFriendAdapter(userList, this,this);
        listRecommendFriendAdapter = new ListRecommendFriendAdapter(userList, this);

        recycleView_ListFriend.setAdapter(listExistingFriendAdapter);
        listExistingFriendAdapter.notifyDataSetChanged();

        recycleView_ListRequestFriend.setAdapter(listRequestFriendAdapter);
        listRequestFriendAdapter.notifyDataSetChanged();

        recycleView_ListRecommendFriend.setAdapter(listRecommendFriendAdapter);
        listRecommendFriendAdapter.notifyDataSetChanged();
        setListener();
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
    public void OnRemoveUserClick(User user) {
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAcceptUserClick(User user) {
        Toast.makeText(this, "Accepted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAddUserClick(User user) {
        Toast.makeText(this, "Added User!", Toast.LENGTH_SHORT).show();
    }
}