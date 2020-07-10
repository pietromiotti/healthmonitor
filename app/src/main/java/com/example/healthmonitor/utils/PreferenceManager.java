package com.example.healthmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static PreferenceManager preferenceManager;
    public Context context;
    DateFormat dateFormat;
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
    private static final String PRIOTIRY_PRESSURE = "PRIORITY_PRESSURE";
    private static final long LIMIT_MIN_DATE = Long.MIN_VALUE;
    private static final long LIMIT_MAX_DATE = Long.MAX_VALUE;


    public PreferenceManager(Context context){
        this.context= context;
        this.name = this.context.getClass().getSimpleName();
        sharedPreferences = this.context.getSharedPreferences(context.getPackageName()+"."+name, Context.MODE_PRIVATE);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        editor = this.sharedPreferences.edit();

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
        if(from != null){
            long date = Converters.dateToTimestamp(from);
            editor.putLong(DATE_FROM, date).apply();
        }
        if(to != null){
            long date = Converters.dateToTimestamp(to);
            editor.putLong(DATE_TO, date).apply();
        }
    }

    public void setDateToPreference(Date to){
        if(to != null){
            long date = Converters.dateToTimestamp(to);
            editor.putLong(DATE_TO, date).apply();
        }
        else editor.putLong(DATE_TO, Long.MAX_VALUE).apply();
    }

    public void setDateFromPreference(Date from){
        if(from != null){
            long date = Converters.dateToTimestamp(from);
            editor.putLong(DATE_FROM, date).apply();
        }
        else editor.putLong(DATE_FROM,Long.MIN_VALUE).apply();
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
            return sharedPreferences.getInt(MIN_PRESSURE_LOWER_BOUND, -1);
    }

    public void setMinPressureTo(int minPressureTo){
            editor.putInt(MIN_PRESSURE_UPPER_BOUND, minPressureTo).apply();
    }

    public int getMinPressureTo(){
        return sharedPreferences.getInt(MIN_PRESSURE_UPPER_BOUND, -1);
    }

    public void setMaxPressureFrom(int maxPressureFrom){
            editor.putInt(MAX_PRESSURE_LOWER_BOUND, maxPressureFrom).apply();
    }

    public int getMaxPressureFrom(){
        return sharedPreferences.getInt(MAX_PRESSURE_LOWER_BOUND, -1);
    }

    public void setMaxPressureTo(int maxPressureTo){
            editor.putInt(MAX_PRESSURE_UPPER_BOUND, maxPressureTo).apply();
    }

    public int getMaxPressureTo(){
        return sharedPreferences.getInt(MAX_PRESSURE_UPPER_BOUND, -1);
    }

    public void setTemperatureTo(double temperatureTo){
            Converters.putDouble(editor, TEMPERATURE_TO, temperatureTo);
    }

    public double getTemperatureTo(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_TO, -1);
    }

    public void setTemperatureFrom(double temperatureFrom){
        Converters.putDouble(editor, TEMPERATURE_FROM, temperatureFrom);
    }

    public double getTemperatureFrom(){
        return Converters.getDouble(sharedPreferences, TEMPERATURE_FROM, -1);
    }


    public void setWeightTo(double weightTo){
            Converters.putDouble(editor, WEIGHT_TO,  weightTo);
    }

    public double getWeightTo(){
        return Converters.getDouble(sharedPreferences, WEIGHT_TO, -1);
    }


    public void setWeightFrom(double weightFrom){
        Converters.putDouble(editor, WEIGHT_FROM,  weightFrom);
    }

    public double getWeightFrom(){
        return Converters.getDouble(sharedPreferences, WEIGHT_FROM, -1);
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
}
