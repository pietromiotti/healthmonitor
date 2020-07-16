package com.example.healthmonitor.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;

import java.util.Calendar;

/* Handler che mi permette di gestire le notifiche */

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
    public static final int EVENT_DELAY_ID = 4;

    public static final String DELAY_ACTION = "DELAYFIVEMINUTESACTION";
    public static final String EVENT_DELAY_ACTION = "EVENT_DELAY_ACTION";
    public static final String ADD_ACTION = "ADDACTION";
    private static int MINUTE_DELAY = 5;




    private NotificationManager notificationManager;
    private DatabaseManager databaseManager;
    private PreferenceManager preferenceManager;

    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        databaseManager = DatabaseManager.getInstanceDBNOContext();
        preferenceManager = PreferenceManager.getPreferenceManagerNoContext();
        /*creazione del channel se Versione android maggiore di 8 */
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
        /*Creazione di due channel distinti, uno per le notifiche legate alla priorità, uno per le notifiche giornaliere */

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

    /*builder della notifica legata alle priorità*/
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

    /*builder della notifica giornaliera*/
    public NotificationCompat.Builder getDailyNotification(){
        return new NotificationCompat.Builder(context.getApplicationContext(), channelDailyID)
                .setContentTitle(context.getResources().getString(R.string.titleDailyNotification))
                .setContentText(context.getResources().getString(R.string.descriptionDailyNotification))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500,1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_alarm);
    }



    /*Async task che controlla in modo asincrono (con le API rilasciate dal DataBase Manager) se i parametri attuali superano i valori
    di threshold impostati dall'utente.
    Per i dettagli della funzione triggerNotification*ThreShold si veda nel DataBaseManager.java
     */
    public class AsyncNotificationInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if (databaseManager.triggerNotificationPressureThreShold()){
                NotificationCompat.Builder bd = getInfoNotification(context.getResources().getString(R.string.titleNotificationPressione), context.getResources().getString(R.string.descriptionNotificationPressione));
                getNotificationManager().notify(PRESSURE_ID, bd.build());
            }
            if (databaseManager.triggerNotificationTemperatureThreShold()){
                NotificationCompat.Builder bd = getInfoNotification(context.getResources().getString(R.string.titleNotificationTemperatura), context.getResources().getString(R.string.descriptionNotificationTemperatura));
                notificationManager.notify(TEMPERATURE_ID, bd.build());
            }
            if (databaseManager.triggerNotificationWeightThreShold()){
                NotificationCompat.Builder bd = getInfoNotification(context.getResources().getString(R.string.titleNotificationPeso), context.getResources().getString(R.string.descriptionNotificationPeso));
                notificationManager.notify(WEIGHT_ID, bd.build());
            }
            return null;
        }
    }

    /*Api che esegue asyncTask*/
    public void triggerNotificationInfo(){
        AsyncNotificationInfo asyncNotificationInfo = new AsyncNotificationInfo();
        asyncNotificationInfo.execute();
    }



    /*Async task che controlla in modo asincrono, quindi non sul MainThread, se nel giorno odierno non sono ancora stati inseriti dei record,
    In tal caso, crea la notifica le cui azioni (action) verranno gestite dall'AlarmReceiverResolver
     */
    public class AsyncNotificationDaily extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!databaseManager.isTodayAlreadyRecorded()) {

                notificationManager = notificationHandler.getNotificationManager();

                Intent reperatingIntent = new Intent(context, AlarmReceiverResolver.class);
                reperatingIntent.putExtra("NotificationMessage", true);
                reperatingIntent.setAction(NotificationHandler.ADD_ACTION);
                reperatingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                reperatingIntent.putExtra("ACTION", NotificationHandler.ADD_ACTION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), reperatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                Intent delayFiveMinute = new Intent(context, AlarmReceiverResolver.class);
                delayFiveMinute.putExtra("ACTION", NotificationHandler.DELAY_ACTION);
                delayFiveMinute.setAction(NotificationHandler.DELAY_ACTION);
                delayFiveMinute.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent delayFiveMinutesPendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), delayFiveMinute, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder bd = notificationHandler.getDailyNotification();
                bd.addAction(R.drawable.ic_library_add_black_24dp,  context.getResources().getString(R.string.buttonAddRecordNotification), pendingIntent);
                bd.addAction(R.drawable.ic_add_black_24dp, context.getResources().getString(R.string.buttonDelayNotification), delayFiveMinutesPendingIntent);

                notificationManager.notify(NotificationHandler.DAILY_ID, bd.build());

            }
            return null;
        }
    }

    public void triggerNotificationDaily(){
        AsyncNotificationDaily asyncNotificationDaily = new AsyncNotificationDaily();
        asyncNotificationDaily.execute();
    }

    /*Async task che risolve l'azione su notifica con il comportamento opportuno */
    public class AsyncResolveNotificationDaily extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            String action = strings[0];
            if(NotificationHandler.ADD_ACTION.equals(action)){
                Intent reperatingIntent = new Intent(context, MainActivity.class);
                reperatingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                /*This extra is a helper that helps me to distinguish when the main activity is triggered by the notification, for the details check on MainActivity.java*/
                reperatingIntent.putExtra("NotificationMessage", true);
                context.startActivity(reperatingIntent);
                notificationManager.cancel(NotificationHandler.DAILY_ID);
            }
            else if (NotificationHandler.DELAY_ACTION.equals(action)){
                notificationHandler.delayFiveMinutes();
                notificationManager.cancel(NotificationHandler.DAILY_ID);
            }
            return null;
        }
    }

    public void resolveNotificationDaily(String s){
        AsyncResolveNotificationDaily asyncResolveNotificationDaily = new AsyncResolveNotificationDaily();
        asyncResolveNotificationDaily.execute(s);
    }




    public void startAlarm(Calendar c){

        /* Check if the time setted is before the actual time, in case add one day */
        if(c.before(Calendar.getInstance())){

            c.add(Calendar.DATE,1);

        }

        /*Alarm Manager Settings */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, EVENT_DAILY_ID, intent, 0);

        cancelAlarm();
        /* Used InexactRepeating to avoid problem in handling notification */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public void delayFiveMinutes(){

        /* Set new time + 5 minutes */
        Calendar myDate = Calendar.getInstance();
        myDate.set(Calendar.MINUTE, myDate.get(Calendar.MINUTE) + MINUTE_DELAY);

        /*Alarm Manager Settings */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        /*Create new notification with different EVENT CODE */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, EVENT_DELAY_ID, intent, 0);

        /* Note that for the delay notification I have createa a single notification, set in the time delayed, I do not override the perdioc notification*/
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, myDate.getTimeInMillis(), pendingIntent);

    }


    public void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        /*Note that to delete the repeating notification need to have the same EVENT_DAILY_ID */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, EVENT_DAILY_ID, intent, 0);

        alarmManager.cancel(pendingIntent);

        notificationManager.cancel(DAILY_ID);

    }


}
