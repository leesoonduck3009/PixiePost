package com.example.pixelpost.Utils.Widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pixelpost.Model.Post.IPostModel;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Widget.PixiePostWidget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WidgetUpdateReceiver extends BroadcastReceiver {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, PixiePostWidget.class));
            // Gọi phương thức cập nhật widget
            if (appWidgetIds.length > 0) {
                new PixiePostWidget().onUpdate(context, appWidgetManager, appWidgetIds);
            }
        });
        executorService.shutdown();
    }
    private void loadUser(Post post, Context context){


    }
}