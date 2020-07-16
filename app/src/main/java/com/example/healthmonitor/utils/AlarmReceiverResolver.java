package com.example.healthmonitor.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.healthmonitor.MainActivity;


/* This class resolve the action triggered by the notification:
* 1) ADD RECORD
* 2) DELAY THE NOTIFICATION
* */
public class AlarmReceiverResolver extends BroadcastReceiver {
    NotificationHandler notificationHandler;
    NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
        notificationManager = notificationHandler.getNotificationManager();
        String action = intent.getAction();
        /*This method handle the notification by an AsyncTask */
        notificationHandler.resolveNotificationDaily(action);
    }
}
