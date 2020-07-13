package com.example.healthmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.healthmonitor.RoomDatabase.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static PreferenceManager preferenceManager;
    public Context context;
    //DateFormat dateFormat;
    String name;

    private static final String DATE_FROM = "DATE_FROM";
    private static final String DATE_TO = "DATE_TO";
    private static final String MIN_PRESSURE_LOWER_BOUND = "MIN_PRESSURE_LOWER_BOUND ";
    private static final String MIN_PRESSURE_UPPER_BOUND= "MIN_PRESSURE_UPPER_BOUND ";
    private static final String MAX_PRESSURE_LOWER_BOUND = "MAX_PRESSURE_LOWER_BOUND ";
    private static final String MAX_PRESSURE_UPPER_BOUND= "MAX_PRESSURE_UPPER_BOUND ";
    private static final String TEMPERATURE_FROM = "TEMPERATURE_FROM";
    private static final String TEMPERATURE_TO = "TEMPERATURE_TO";
    private static final String WEIGHT_FROM = "WEIGHT_FROM";
    private static final String WEIGHT_TO = "WEIGHT_TO";
    private static final String PRIORITY_WEIGHT = "PRIORITY_WEIGHT";
    private static final String PRIORITY_TEMPERATURE = "PRIORITY_TEMPERATURE";
    private static final String PRIORITY_PRESSURE = "PRIORITY_PRESSURE";
    private static final String DAILY_NOTIFICATION = "DAILY_NOTIFICATION";
    private static final String DAILY_NOTIFICATION_HOUR = "DAILY_NOTIFICATION_HOUR";
    private static final String INTERVAL_MONITOR_TIME = "INTERVAL_MONITOR_TIME";
    private static final String MIN_PRESSURE_MONITORING_LOWER_BOUND = "MIN_PRESSURE_MONITORING_LOWER_BOUND ";
    private static final String MIN_PRESSURE_MONITORING_UPPER_BOUND = "MIN_PRESSURE_MONITORING_UPPER_BOUND";
    private static final String MAX_PRESSURE_MONITORING_LOWER_BOUND = "MAX_PRESSURE_MONITORING_LOWER_BOUND ";
    private static final String MAX_PRESSURE_MONITORING_UPPER_BOUND = "MAX_PRESSURE_MONITORING_UPPER_BOUND";
    private static final String TEMPERATURE_MONITORING_LOWER_BOUND = "TEMPERATURE_MONITORING_LOWER_BOUND ";
    private static final String TEMPERATURE_MONITORING_UPPER_BOUND = "TEMPERATURE_MONITORING_UPPER_BOUND";
    private static final String WEIGHT_MONITORING_LOWER_BOUND = "WEIGHT_MONITORING_LOWER_BOUND";
    private static final String WEIGHT_MONITORING_UPPER_BOUND = "WEIGHT_MONITORING_UPPER_BOUND";
    private static final String FIRST_TIME_ACCESS = "FIRST_TIME_ACCESS";




    public static final long LIMIT_MIN_DATE = Long.MIN_VALUE;
    public static final long LIMIT_MAX_DATE = Long.MAX_VALUE;
    public static final int DEFAULT_MAX_INT = Integer.MAX_VALUE;
    public static final int DEFAULT_MIN_INT = Integer.MIN_VALUE;
    public static final double DEFAULT_MAX_DOUBLE = Double.MAX_VALUE;
    public static final double DEFAULT_MIN_DOUBLE = Double.MIN_VALUE;


    public PreferenceManager(Context context){
        this.context= context;
        this.name = this.context.getClass().getSimpleName();
        sharedPreferences = this.context.getSharedPreferences(context.getPackageName()+"."+name, Context.MODE_PRIVATE);
        //this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        editor = this.sharedPreferences.edit();
        if (getFirstTimeAccess()) {
            setFirstTimeAccess();
            resetFilterPreference();
        }
}
    public static PreferenceManager getPreferenceManagerWithContext(Context context) {
        if (preferenceManager == null){
            preferenceManager = new PreferenceManager(context);
        }

        return preferenceManager;
    }

    public static PreferenceManager getPreferenceManagerNoContext(){
        return preferenceManager;
    }

    public void setDatePreference(Date from, Date to){
        if((from != null) && (to != null)) {
            long date = Converters.dateToTimestamp(from);
            editor.putLong(DATE_FROM, date).apply();
            long date2 = Converters.dateToTimestamp(to);
            editor.putLong(DATE_TO, date2).apply();
        }
        else{
            editor.putLong(DATE_TO, LIMIT_MAX_DATE).apply();
            editor.putLong(DATE_FROM,LIMIT_MIN_DATE).apply();
        }
    }
    public void setDateToPreference(Date to){
        if(to != null){
            long date = Converters.dateToTimestamp(to);
            editor.putLong(DATE_TO, date).apply();
        }
        else editor.putLong(DATE_TO, LIMIT_MAX_DATE).apply();
    }

    public void setDateFromPreference(Date from){
        if(from != null){
            long date = Converters.dateToTimestamp(from);
            editor.putLong(DATE_FROM, date).apply();
        }
        else editor.putLong(DATE_FROM,LIMIT_MIN_DATE).apply();
    }

    public Date getDateFromPreference() throws ParseException {

        Date datefrom = Converters.fromTimestamp(sharedPreferences.getLong(DATE_FROM, LIMIT_MIN_DATE));
        return datefrom;

    }
    public Date getDateToPreference() throws ParseException {
        Date dateTo = Converters.fromTimestamp(sharedPreferences.getLong(DATE_TO, LIMIT_MAX_DATE));
        return dateTo;
    }

    public void setMinPressureFrom(int minPressureFrom){
            editor.putInt(MIN_PRESSURE_LOWER_BOUND, minPressureFrom).apply();
    }

    public int getMinPressureFrom(){
            return sharedPreferences.getInt(MIN_PRESSURE_LOWER_BOUND, DEFAULT_MIN_INT);
    }

    public void setMinPressureTo(int minPressureTo){
            editor.putInt(MIN_PRESSURE_UPPER_BOUND, minPressureTo).apply();
    }

    public int getMinPressureTo(){
        return sharedPreferences.getInt(MIN_PRESSURE_UPPER_BOUND, DEFAULT_MAX_INT);
    }

    public void setMaxPressureFrom(int maxPressureFrom){
            editor.putInt(MAX_PRESSURE_LOWER_BOUND, maxPressureFrom).apply();
    }

    public int getMaxPressureFrom(){
        return sharedPreferences.getInt(MAX_PRESSURE_LOWER_BOUND, DEFAULT_MIN_INT);
    }

    public void setMaxPressureTo(int maxPressureTo){
            editor.putInt(MAX_PRESSURE_UPPER_BOUND, maxPressureTo).apply();
    }

    public int getMaxPressureTo(){
        return sharedPreferences.getInt(MAX_PRESSURE_UPPER_BOUND, DEFAULT_MAX_INT);
    }

    public void setTemperatureTo(double temperatureTo){
            Converters.putDouble(editor, TEMPERATURE_TO, temperatureTo);
    }

    public double getTemperatureTo(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_TO, DEFAULT_MAX_DOUBLE);
    }

    public void setTemperatureFrom(double temperatureFrom){
        Converters.putDouble(editor, TEMPERATURE_FROM, temperatureFrom);
    }

    public double getTemperatureFrom(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_FROM, DEFAULT_MIN_DOUBLE);
    }


    public void setWeightTo(double weightTo){
        Converters.putDouble(editor, WEIGHT_TO,  weightTo);
    }

    public double getWeightTo(){
        return Converters.getDouble(sharedPreferences, WEIGHT_TO, DEFAULT_MAX_DOUBLE);
    }


    public void setWeightFrom(double weightFrom){
        Converters.putDouble(editor, WEIGHT_FROM,  weightFrom);
    }

    public double getWeightFrom(){
        return Converters.getDouble(sharedPreferences, WEIGHT_FROM, DEFAULT_MIN_DOUBLE);
    }


    public void resetFilterPreference(){
        this.setDateToPreference(null);
        this.setDateFromPreference(null);

        this.setMinPressureFrom(-1);
        this.setMinPressureTo(-1);

        this.setMaxPressureTo(-1);
        this.setMaxPressureFrom(-1);

        this.setWeightFrom(-1);
        this.setWeightTo(-1);

        this.setTemperatureFrom(-1);
        this.setTemperatureTo(-1);
    }

    public boolean filteringRecord(Record r) throws ParseException {
        boolean ok;

        ok =    r.getDate().after(getDateFromPreference()) &&
                r.getDate().before(getDateToPreference());

        if (getMinPressureFrom() != -1){
            ok = ok && r.getMin_pressure() >= getMinPressureFrom();
        }

        if(getMinPressureTo() != -1){
            ok = ok &&  r.getMin_pressure() <= getMinPressureTo();
        }

        if(getMaxPressureFrom() != -1){
            ok = ok && r.getMax_pressure() >= getMaxPressureFrom();
        }

        if(getMaxPressureTo() != -1){
            ok = ok && r.getMax_pressure() <= getMaxPressureTo();
        }

        if(getTemperatureFrom() != -1){
            ok = ok && r.getMax_pressure() <= getMaxPressureTo();
        }

        if(getTemperatureFrom() != -1){
            ok = ok && r.getTemperature() >= getTemperatureFrom();
        }

        if(getTemperatureTo() != -1){
            ok = ok && r.getTemperature() >= getTemperatureTo();
        }

        if(getWeightFrom() != -1){
            ok = ok && r.getWeight() >= getWeightFrom();
        }

        if(getWeightTo() != -1) {
            ok = ok && r.getWeight() >= getWeightTo();
        }
        return ok;
    }


    public void setDailyNotification(boolean agreement){
        editor.putBoolean(DAILY_NOTIFICATION, agreement).apply();
    }
    public boolean getDailyNotification(){
        return sharedPreferences.getBoolean(DAILY_NOTIFICATION, false);
    }

    public void setDailyNotificationHour(Date date){
        long dateLong = Converters.dateToTimestamp(date);
        editor.putLong(DAILY_NOTIFICATION_HOUR, dateLong).apply();
    }
    public Date getDailyNotificationHour(){
        Calendar c = Calendar.getInstance();
        c.set(0,0,0, 12,0);
        return Converters.fromTimestamp(sharedPreferences.getLong(DAILY_NOTIFICATION_HOUR, Converters.dateToTimestamp(c.getTime())));
    }



    public void setWeightPriority(int priority){
        editor.putInt(PRIORITY_WEIGHT, priority).apply();
    }

    public void setTemperaturePriority(int priority){
        editor.putInt(PRIORITY_TEMPERATURE, priority).apply();
    }

    public void setPressurePriority(int priority){
        editor.putInt(PRIORITY_PRESSURE, priority).apply();
    }

    public int getWeightPriority(){
        return sharedPreferences.getInt(PRIORITY_WEIGHT, 1);
    }

    public int getTemperaturePriority(){
        return sharedPreferences.getInt(PRIORITY_TEMPERATURE, 1);
    }

    public int getPressurePriority(){
        return sharedPreferences.getInt(PRIORITY_PRESSURE, 1);
    }


    public void setIntervalMonitorTime(int days){
        editor.putInt(INTERVAL_MONITOR_TIME, days).apply();
    }

    public int getIntervalMonitorTime(){
        return sharedPreferences.getInt(INTERVAL_MONITOR_TIME, 30);
    }


    public void setMinPressureAverageLowerBound(int minPressureFrom){
        editor.putInt(MIN_PRESSURE_MONITORING_LOWER_BOUND, minPressureFrom).apply();
    }

    public int getMinPressureAverageLowerBound(){
        return sharedPreferences.getInt(MIN_PRESSURE_MONITORING_LOWER_BOUND, DEFAULT_MIN_INT);
    }

    public void setMinPressureAverageUpperBound(int minPressureTo){
        editor.putInt(MIN_PRESSURE_MONITORING_UPPER_BOUND, minPressureTo).apply();
    }

    public int getMinPressureAverageUpperBound(){
        return sharedPreferences.getInt(MIN_PRESSURE_MONITORING_UPPER_BOUND, DEFAULT_MAX_INT);
    }

    public void setMaxPressureAverageLowerBound(int maxPressureFrom){
        editor.putInt(MAX_PRESSURE_MONITORING_LOWER_BOUND, maxPressureFrom).apply();
    }

    public int getMaxPressureAverageLowerBound(){
        return sharedPreferences.getInt(MAX_PRESSURE_MONITORING_LOWER_BOUND, DEFAULT_MIN_INT);
    }

    public void setMaxPressureAverageUpperBound(int maxPressureTo){
        editor.putInt(MAX_PRESSURE_MONITORING_UPPER_BOUND, maxPressureTo).apply();
    }

    public int getMaxPressureAverageUpperBound(){
        return sharedPreferences.getInt(MAX_PRESSURE_MONITORING_UPPER_BOUND, DEFAULT_MAX_INT);
    }

    public void setTemperatureAverageUpperBound(double temperatureTo){
        Converters.putDouble(editor, TEMPERATURE_MONITORING_UPPER_BOUND, temperatureTo);
    }

    public double getTemperatureAverageUpperBound(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_MONITORING_UPPER_BOUND, DEFAULT_MAX_DOUBLE);
    }

    public void setTemperatureAverageLowerBound(double temperatureFrom){
        Converters.putDouble(editor, TEMPERATURE_MONITORING_LOWER_BOUND, temperatureFrom);
    }

    public double getTemperatureAverageLowerBound(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_MONITORING_LOWER_BOUND, DEFAULT_MIN_DOUBLE);
    }


    public void setWeightAverageUpperBound(double weightTo){
        Converters.putDouble(editor, WEIGHT_MONITORING_UPPER_BOUND,  weightTo);
    }

    public double getWeightAverageUpperBound(){
        return Converters.getDouble(sharedPreferences, WEIGHT_MONITORING_UPPER_BOUND, DEFAULT_MAX_DOUBLE);
    }


    public void setWeightAverageLowerBound(double weightFrom){
        Converters.putDouble(editor, WEIGHT_MONITORING_LOWER_BOUND,  weightFrom);
    }

    public double getWeightAverageLowerBound(){
        return Converters.getDouble(sharedPreferences, WEIGHT_MONITORING_LOWER_BOUND, DEFAULT_MIN_DOUBLE);
    }


    public boolean getFirstTimeAccess(){
        return sharedPreferences.getBoolean(FIRST_TIME_ACCESS, true);
    }

    public void setFirstTimeAccess(){
        editor.putBoolean(FIRST_TIME_ACCESS, false).apply();
    }





}
