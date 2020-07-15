package com.example.healthmonitor.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.healthmonitor.MainActivity;

public class AlarmReceiverResolver extends BroadcastReceiver {
    NotificationHandler notificationHandler;
    NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
        notificationManager = notificationHandler.getNotificationManager();
        String action = intent.getAction();
        notificationHandler.resolveNotificationDaily(action);
    }
}
