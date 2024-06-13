package com.example.pixelpost.Utils.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class WidgetUpdateService extends Service {

    private static final long INTERVAL_ONE_MINUTE = 1000; // 1 phút

    private PendingIntent pendingIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleWidgetUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    private void scheduleWidgetUpdate() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WidgetUpdateReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Lên lịch cho việc gửi broadcast sau mỗi phút
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL_ONE_MINUTE, pendingIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
