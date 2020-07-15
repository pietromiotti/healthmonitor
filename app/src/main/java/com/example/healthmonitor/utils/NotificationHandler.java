package com.example.healthmonitor.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.ui.CalendarFragment;
import com.google.android.material.button.MaterialButton;

import java.net.Inet4Address;
import java.util.Calendar;
import java.util.Date;

public class NotificationHandler {
    public static NotificationHandler notificationHandler;
    public static final String channelInfoID = "channelInfoID";
    public static final String channelInfoName = "channelInfoName";
    public static final String channelDailyID = "channelDailyID";
    public static final String channelDailyName = "channelDailyName";
    public static final int PRESSURE_ID = 1;
    public static final int TEMPERATURE_ID = 2;
    public static final int WEIGHT_ID = 3;
    public static final int DAILY_ID = 4;
    public static final int EVENT_DAILY_ID = 3;
    public static final String DELAY_ACTION = "DELAYFIVEMINUTESACTION";
    public static final String ADD_ACTION = "ADDACTION";



    NotificationManager notificationManager;
    DatabaseManager databaseManager;


    Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        databaseManager = DatabaseManager.getInstanceDBNOContext();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createChannels();
        }
        getNotificationManager();
    }

    public static NotificationHandler getInstanceOfNotificationHandler(Context context) {
        if (notificationHandler == null) {
            notificationHandler = new NotificationHandler(context);
        }
        return notificationHandler;
    }

    public static NotificationHandler getInstanceOfNotificationHandlerNoContext() {
        return notificationHandler;
    }


    public NotificationManager getNotificationManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels(){
        NotificationChannel channelInfo = new NotificationChannel(channelInfoID, channelInfoName, NotificationManager.IMPORTANCE_HIGH);
        channelInfo.enableLights(true);
        channelInfo.enableVibration(true);
        channelInfo.setLightColor(R.color.colorBlueDark);


        NotificationChannel channelDaily = new NotificationChannel(channelDailyID, channelDailyName, NotificationManager.IMPORTANCE_HIGH);
        channelInfo.enableLights(true);
        channelInfo.enableVibration(true);
        channelInfo.setLightColor(R.color.colorBlueDark);

        getNotificationManager().createNotificationChannel(channelInfo);
        getNotificationManager().createNotificationChannel(channelDaily);

    }

    public NotificationCompat.Builder getInfoNotification(String title, String message){
        return new NotificationCompat.Builder(context.getApplicationContext(), channelInfoID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500,1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_warning_black_24dp);
    }

    public NotificationCompat.Builder getDailyNotification(){

        return new NotificationCompat.Builder(context.getApplicationContext(), channelDailyID)
                .setContentTitle("Aggiungi il tuo record")
                .setContentText("Non trascurarti!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500,1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_alarm);
    }


    public class AsyncNotificationInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if (databaseManager.triggerNotificationPressureThreShold()){
                NotificationCompat.Builder bd = getInfoNotification("Pressione", "I parametri della tua pressione preoccupanti!");
                getNotificationManager().notify(PRESSURE_ID, bd.build());
            }
            if (databaseManager.triggerNotificationTemperatureThreShold()){
                NotificationCompat.Builder bd = getInfoNotification("Temperatura", "I parametri della tua temperatura preoccupanti!");
                notificationManager.notify(TEMPERATURE_ID, bd.build());
            }
            if (databaseManager.triggerNotificationWeightThreShold()){
                NotificationCompat.Builder bd = getInfoNotification("Peso", "I parametri del tuo peso sono preoccupanti!");
                notificationManager.notify(WEIGHT_ID, bd.build());
            }
            return null;
        }
    }

    public void triggerNotificationInfo(){
        AsyncNotificationInfo asyncNotificationInfo = new AsyncNotificationInfo();
        asyncNotificationInfo.execute();
    }


    /*
    public class AsyncNotificationDaily extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

    public void triggerNotificationDaily(){
        AsyncNotificationDaily asyncNotificationDaily = new AsyncNotificationDaily();
        asyncNotificationDaily.execute();
    }
*/

    public void startAlarm(Calendar c){

        /* Check if the time setted is before the actual time, in case add one day */
        Date nowDate = new Date();
        Calendar myDate = Calendar.getInstance();

        myDate.set(Calendar.DAY_OF_MONTH, c.get(Calendar.HOUR_OF_DAY));
        myDate.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

        Log.i("NOTIFICATION", "START ALARM -> ACTIVE NOTIFICATIONS -> " + notificationManager.getActiveNotifications().toString());

        if(c.after(Calendar.getInstance())){

            myDate.add(Calendar.DATE,1);

            Log.i("NOTIFICATION", "C-> " + Converters.printDate(Calendar.getInstance().getTime()));
            Log.i("NOTIFICATION", " ALARM -> " + Converters.printDate(myDate.getTime()));
        }

        /*Alarm Manager Settings */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /* Used InexactRepeating to avoid problem in handling notification */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public void delayFiveMinute(Calendar c){

        /* Check if the time setted is before the actual time, in case add one day */
        Date nowDate = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);

        if(c.before(now)){
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        /*Alarm Manager Settings */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /* Used InexactRepeating to avoid problem in handling notification */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        notificationManager.cancel(DAILY_ID);

    }


}
