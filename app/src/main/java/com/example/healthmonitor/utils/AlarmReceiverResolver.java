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

        if(NotificationHandler.ADD_ACTION.equals(action)){
            Intent reperatingIntent = new Intent(context, MainActivity.class);
            reperatingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            reperatingIntent.putExtra("NotificationMessage", true);
            context.startActivity(reperatingIntent);
            notificationManager.cancel(NotificationHandler.DAILY_ID);
        }
        else if (NotificationHandler.DELAY_ACTION.equals(action)){
            Log.i("NOTIFICATION", "DEALAY");
            notificationManager.cancel(NotificationHandler.DAILY_ID);
        }
    }
}
