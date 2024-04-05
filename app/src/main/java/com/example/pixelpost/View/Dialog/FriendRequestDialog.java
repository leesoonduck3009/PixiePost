package com.example.pixelpost.View.Dialog;

import static com.example.pixelpost.View.Dialog.FriendRequestDialog.FriendRequestDialogType.NOT_IS_FRIEND;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private FriendRequestDialog(Context context,FriendRequestDialogType type)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_add_friend_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnDismiss = dialog.findViewById(R.id.btnDismiss);
        if(type == NOT_IS_FRIEND)
            btnAcceptFriend.setOnClickListener(v->{
                if (dialogClickListener != null) {
                    dialogClickListener.onAcceptFriendClick();
                }
            });
        btnDismiss.setOnClickListener(v->{
            dialog.dismiss();
        });
    }
    public static void showDialog(Context context, User user, FriendRequestDialogType type, DialogClickListener listener)
    {
        FriendRequestDialog friendRequestDialog = new FriendRequestDialog(context,type);
        friendRequestDialog.dialogClickListener = listener;
        ImageView imgViewAcceptBtn = friendRequestDialog.dialog.findViewById(R.id.imgViewAcceptBtn);
        TextView txtViewAcceptBtn = friendRequestDialog.dialog.findViewById(R.id.txtViewAcceptBtn);
        RoundedImageView imgviewAvatar = friendRequestDialog.dialog.findViewById(R.id.imgviewAvatar);
        TextView txtViewName = friendRequestDialog.dialog.findViewById(R.id.txtViewName);
        switch (type){
            case IS_FRIEND:
                imgViewAcceptBtn.setImageResource(R.drawable.ic_check);
                txtViewAcceptBtn.setText(R.string.isFriend);
                break;
            case NOT_IS_FRIEND:
                imgViewAcceptBtn.setImageResource(R.drawable.icon_add);
                txtViewAcceptBtn.setText(R.string.notIsFriend);
                break;
        }
        if(user.getAvatarUrl()!=null && !user.getAvatarUrl().isEmpty())
            Glide.with(context).load(user.getAvatarUrl()).into(imgviewAvatar);
        else
            Glide.with(context).load(R.drawable.avatar3).into(imgviewAvatar);
        String name =user.getFirstName() + user.getLastName();
        txtViewName.setText(name);
        friendRequestDialog.dialog.show();
    }
    public interface DialogClickListener {
        void onAcceptFriendClick();
    }
    public enum FriendRequestDialogType {
        IS_FRIEND,
        NOT_IS_FRIEND
    }
}
