package com.example.healthmonitor.utils;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.TypeConverter;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;

import java.util.Calendar;
import java.util.Date;


/*Utility class, this contains self-made functions that helps me to handle some conversions and other stuff */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static  int parseStringToInt(String s){
        if (s.matches("")){
            return DatabaseManager.DEFAULT_NULL_VALUE;
        }
        else {
            return Integer.parseInt(s);
        }
    }

    public static boolean isInt(double f){
        if (f % 1 == 0) return true;
        else return false;
    }

    public static boolean isPositive(double f){
        if(f>= -1) return true;
        else return false;
    }

    public static String parseIntToString(int i){
        if (i == DatabaseManager.DEFAULT_NULL_VALUE){
            return "";
        }
        else return String.valueOf(i);
    }
    public static String parseDoubleToString(double i){
        if (i == DatabaseManager.DEFAULT_NULL_VALUE){
            return "";
        }
        else return String.valueOf(i);
    }

    public static double parseStringToDouble(String s){
        if (s.matches("")){
            return DatabaseManager.DEFAULT_NULL_VALUE;
        }
        else return Double.parseDouble(s);
    }

    public static void putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        edit.putLong(key, Double.doubleToRawLongBits(value)).apply();
    }

    public static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }


    /*Self made version of formatDate */

    public static String printDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String s = day + "/" + month + "/" +year + " " +hours+":"+minutes+":"+seconds;
        return s;
    }

    public static String printDateDayMonthYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String s = day + "/" + month + "/" +year;
        return s;
    }

    public static String printDateHourAndMinutes(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String corrector = "";
        if (minutes <= 9) corrector = "0";
        String s = hours+" : "+corrector+minutes;
        return s;
    }

    public static boolean areSameDay(Date date1, Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH) + 1;
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH) + 1;
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        return (year1 == year2) && (month1== month2) && (day1 == day2);
    }
}