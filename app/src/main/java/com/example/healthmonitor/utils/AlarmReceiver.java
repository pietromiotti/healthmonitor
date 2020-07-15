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
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.ui.CalendarFragment;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationHandler notificationHandler;
    NotificationManager notificationManager;
    DatabaseManager databaseManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
        databaseManager = DatabaseManager.getInstanceDBNOContext();
        Log.i("NOTIFICATION", "INTENT: " + intent.toString());
        notificationHandler.triggerNotificationDaily();

    }
}

