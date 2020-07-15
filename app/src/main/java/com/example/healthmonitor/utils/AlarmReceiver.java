package com.example.healthmonitor.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.ui.CalendarFragment;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationHandler notificationHandler;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
        //notificationHandler.triggerNotificationDaily();
        notificationManager = notificationHandler.getNotificationManager();

        Intent reperatingIntent = new Intent(context, MainActivity.class);

        reperatingIntent.putExtra("NotificationMessage", true);

        reperatingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, reperatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder bd = notificationHandler.getDailyNotification(pendingIntent);

        notificationManager.notify(NotificationHandler.DAILY_ID, bd.build());


    }
}
