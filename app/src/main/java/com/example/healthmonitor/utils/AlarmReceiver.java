package com.example.healthmonitor.utils;

import android.app.NotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.healthmonitor.RoomDatabase.DatabaseManager;



/*This broadCast receiver is triggered every time the set date (check StartAlarm in NotificationHandler) occurred */

public class AlarmReceiver extends BroadcastReceiver {
    NotificationHandler notificationHandler;
    DatabaseManager databaseManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
        databaseManager = DatabaseManager.getInstanceDBNOContext();

        /*Since the OnReceive is handled in the UI Thread, I created an Async Task to perform this longer computations*/
        notificationHandler.triggerNotificationDaily();

    }
}

