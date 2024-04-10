package com.example.pixelpost.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pixelpost.R;

public class CustomDialog {
    private Dialog dialog;
    private Button btnYes, btnNo, btnOk;
    private DialogClickListener dialogClickListener;

    private CustomDialog(Context context ) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find views
        btnYes = dialog.findViewById(R.id.btnYes);
        btnNo = dialog.findViewById(R.id.btnNo);
        btnOk = dialog.findViewById(R.id.btnOk);

        // Set button click listeners
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClickListener != null) {
                    dialogClickListener.onYesClick();
                }
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClickListener != null) {
                    dialogClickListener.onNoClick();
                }
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClickListener != null) {
                    dialogClickListener.onOkClick();
                }
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context, String message, DialogType type, DialogClickListener listener) {
        CustomDialog customDialog = new CustomDialog(context);
        customDialog.dialogClickListener = listener;
        TextView txtDialogMessage = customDialog.dialog.findViewById(R.id.txtDialogMessage);
        txtDialogMessage.setText(message);

        switch (type) {
            case OK:
                customDialog.btnYes.setVisibility(View.GONE);
                customDialog.btnNo.setVisibility(View.GONE);
                customDialog.btnOk.setVisibility(View.VISIBLE);
                break;
            case YES_NO:
                customDialog.btnYes.setVisibility(View.VISIBLE);
                customDialog.btnNo.setVisibility(View.VISIBLE);
                customDialog.btnOk.setVisibility(View.GONE);
                break;
        }
        customDialog.dialog.show();
    }

    public interface DialogClickListener {
        void onYesClick();
        void onNoClick();
        void onOkClick();
    }

    public enum DialogType {
        OK,
        YES_NO
    }
}
