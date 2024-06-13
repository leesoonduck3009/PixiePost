package com.example.pixelpost.View.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.MainActivity;

import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class PixiePostWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) throws ExecutionException, InterruptedException {
        Bitmap mBitmap;
        Bitmap userBitmap;
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pixie_post_widget);
        //views.setTextViewText(R.id.btnOpenWidget, widgetText);
        PreferenceManager preferenceManager = new PreferenceManager(context);
        Post post = (Post) preferenceManager.getSerializable(Post.FIREBASE_COLLECTION_NAME);
        mBitmap = Glide.with(context)
                .asBitmap()
                .load(post.getUrl()).override(300, 300)
                .submit()
                .get();
        if(post.getOwnerUser().getAvatarUrl()!=null)
        userBitmap = Glide.with(context).asBitmap().load(post.getOwnerUser().getAvatarUrl())
                .override(100, 100)
                .submit().get();
        else
            userBitmap = Glide.with(context).asBitmap().load(R.drawable.avatar3)
                    .override(100, 100).submit().get();
        Log.d("target","Success");
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setImageViewBitmap(R.id.imgViewMain, mBitmap);
        views.setTextViewText(R.id.textView_title, post.getText());
        views.setImageViewBitmap(R.id.imageViewAvatar, userBitmap);
        views.setOnClickPendingIntent(R.id.imgViewMain, pendingIntent);
        views.setOnClickPendingIntent(R.id.textView_title, pendingIntent);
        views.setOnClickPendingIntent(R.id.imageViewAvatar, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];

            try {
                updateAppWidget(context,appWidgetManager, appWidgetId);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}