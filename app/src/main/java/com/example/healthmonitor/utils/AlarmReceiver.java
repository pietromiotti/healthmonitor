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

        if (!databaseManager.isTodayAlreadyRecorded()){

            notificationManager = notificationHandler.getNotificationManager();

            Intent reperatingIntent = new Intent(context, AlarmReceiverResolver.class);
            reperatingIntent.putExtra("NotificationMessage", true);
            reperatingIntent.setAction(NotificationHandler.ADD_ACTION);
            reperatingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            reperatingIntent.putExtra("ACTION", NotificationHandler.ADD_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), reperatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            Intent delayFiveMinute = new Intent(context, AlarmReceiverResolver.class);
            delayFiveMinute.putExtra("ACTION", NotificationHandler.DELAY_ACTION);
            delayFiveMinute.setAction(NotificationHandler.DELAY_ACTION);
            delayFiveMinute.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent delayFiveMinutesPendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), delayFiveMinute, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder bd = notificationHandler.getDailyNotification();
            bd.addAction(R.drawable.ic_library_add_black_24dp, "Aggiungi Record", pendingIntent);
            bd.addAction(R.drawable.ic_add_black_24dp, "Ritarda", delayFiveMinutesPendingIntent);

            notificationManager.notify(NotificationHandler.DAILY_ID, bd.build());


        }
    }
}
