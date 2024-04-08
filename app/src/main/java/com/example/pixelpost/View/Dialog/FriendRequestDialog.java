package com.example.pixelpost.View.Dialog;

import static com.example.pixelpost.View.Dialog.FriendRequestDialog.FriendRequestDialogType.ACCEPT;
import static com.example.pixelpost.View.Dialog.FriendRequestDialog.FriendRequestDialogType.NOT_IS_FRIEND;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

public class FriendRequestDialog {
    private Dialog dialog;
    private LinearLayout btnAcceptFriend, btnDismiss;
    private ImageView imgViewAcceptBtn;
    private TextView txtViewAcceptBtn;
    private DialogClickListener dialogClickListener;
    private ProgressBar progressBar;
    private FriendRequestDialogType type;
    private FriendRequest friendRequest;
    private FriendRequestDialog(Context context, FriendRequest friendRequest, FriendRequestDialogType type)
    {
        this.type = type;
        this.friendRequest = friendRequest;
        dialog = new Dialog(context,R.style.AppTheme_Dialog_MyDialogTheme);
        dialog.setContentView(R.layout.layout_add_friend_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnAcceptFriend = dialog.findViewById(R.id.btnAcceptFriend);
        btnDismiss = dialog.findViewById(R.id.btnDismiss);
        imgViewAcceptBtn = dialog.findViewById(R.id.imgViewAcceptBtn);
        txtViewAcceptBtn = dialog.findViewById(R.id.txtViewAcceptBtn);
        progressBar = dialog.findViewById(R.id.progressBar);
        if(type == NOT_IS_FRIEND || type == ACCEPT)
            btnAcceptFriend.setOnClickListener(v->{
                if (dialogClickListener != null) {
                    dialogClickListener.onAcceptFriendClick(this);
                    imgViewAcceptBtn.setVisibility(View.GONE);
                    txtViewAcceptBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

        btnDismiss.setOnClickListener(v->{
            dialog.dismiss();
        });
    }

    public FriendRequestDialogType getType() {
        return type;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public static void showDialog(Context context, User user, FriendRequest friendRequest, FriendRequestDialogType type, DialogClickListener listener)
    {
        FriendRequestDialog friendRequestDialog = new FriendRequestDialog(context,friendRequest,type);
        friendRequestDialog.dialogClickListener = listener;
        ImageView imgViewAcceptBtn = friendRequestDialog.dialog.findViewById(R.id.imgViewAcceptBtn);
        TextView txtViewAcceptBtn = friendRequestDialog.dialog.findViewById(R.id.txtViewAcceptBtn);
        RoundedImageView imgviewAvatar = friendRequestDialog.dialog.findViewById(R.id.imgviewAvatar);
        TextView txtViewName = friendRequestDialog.dialog.findViewById(R.id.txtViewName);
        TextView txtViewAlreadyFriend = friendRequestDialog.dialog.findViewById(R.id.txtViewAlreadyFriend);
        switch (type){
            case IS_FRIEND:
                imgViewAcceptBtn.setBackgroundResource(R.drawable.ic_check);
                txtViewAcceptBtn.setText(R.string.isFriend);
                break;
            case NOT_IS_FRIEND:
                imgViewAcceptBtn.setBackgroundResource(R.drawable.icon_add);
                txtViewAcceptBtn.setText(R.string.notIsFriend);
                break;
            case ACCEPT:
                imgViewAcceptBtn.setBackgroundResource(R.drawable.ic_check);
                txtViewAcceptBtn.setText("Đồng ý");
                break;
            case ALREADY_SENT:
                imgViewAcceptBtn.setVisibility(View.GONE);
                txtViewAcceptBtn.setVisibility(View.GONE);
                txtViewAlreadyFriend.setVisibility(View.VISIBLE);
                break;
        }
        if(user.getAvatarUrl()!=null && !user.getAvatarUrl().isEmpty())
            Glide.with(context).load(user.getAvatarUrl()).into(imgviewAvatar);
        else
            Glide.with(context).load(R.drawable.avatar3).into(imgviewAvatar);
        String name = user.getLastName() + " " + user.getFirstName();
        txtViewName.setText(name);
        friendRequestDialog.dialog.show();
    }
    public void successLoading()   {
            this.progressBar.setVisibility(View.GONE);
            ImageView imgViewSuccess = dialog.findViewById(R.id.imgViewSuccess);
            imgViewSuccess.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Thực hiện các hành động sau 1 giây
                    // Ví dụ: ẩn ImageViewSuccess và đóng dialog
                    imgViewSuccess.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }, 500); // 1000 milliseconds = 1 giây

    }
    public void failedLoading()
    {
        progressBar.setVisibility(View.GONE);
        imgViewAcceptBtn.setVisibility(View.VISIBLE);
        txtViewAcceptBtn.setVisibility(View.VISIBLE);
    }
    public interface DialogClickListener {
        void onAcceptFriendClick(FriendRequestDialog dialog);
    }
    public enum FriendRequestDialogType {
        IS_FRIEND,
        NOT_IS_FRIEND,
        ACCEPT,
        ALREADY_SENT
    }
}
