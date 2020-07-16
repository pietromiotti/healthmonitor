package com.example.healthmonitor.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;

import java.util.ArrayList;

public class ErrorHandler {

    /* check if there are at leat two arguments*/
    public static  boolean checkIfDialogFieldsAreCorrect(int min_pressure, int max_pressure, double temperature, double weight){
        /*Check if there are at least two fields */
        int counter = 0;
        if(min_pressure != DatabaseManager.DEFAULT_NULL_VALUE)counter++;
        if(max_pressure != DatabaseManager.DEFAULT_NULL_VALUE) counter++;
        if(temperature != DatabaseManager.DEFAULT_NULL_VALUE) counter++;
        if(weight != DatabaseManager.DEFAULT_NULL_VALUE) counter++;
        if (counter>=2) return true;
        else return false;
    }

    public static void showToLessArgument(Context context){
        Toast.makeText(context, "Modifica Fallita! \nDevi inserire almeno due campi", Toast.LENGTH_LONG).show();
    }
    public static void InsertShowToLessArgument(Context context){
        Toast.makeText(context, "Inserimento Fallito! \nDevi inserire almeno due campi", Toast.LENGTH_LONG).show();
    }

    public static void editCompleted(Context context){
        Toast.makeText(context, "Modifica avvenuta con successo", Toast.LENGTH_LONG).show();
    }
    public static void insertCompleted(Context context){
        Toast.makeText(context, "Aggiunta del record avvenuta con successo", Toast.LENGTH_LONG).show();
    }

    public static void mustBeInteger(Context context){
        Toast.makeText(context, "Errore! \nI valori della pressione devono essere interi!", Toast.LENGTH_LONG).show();
    }

    public static void mustBePositive(Context context){
        Toast.makeText(context, "Errore! \nI valori dei parametri non possono essere negativi!", Toast.LENGTH_LONG).show();
    }

    public static boolean arePositive(double minpressure, double maxpressure, double weight, double temperature){
        if (
            Converters.isPositive(minpressure) &&
            Converters.isPositive(maxpressure) &&
            Converters.isPositive(weight) &&
            Converters.isPositive(temperature)
        ) return true;
        else return false;
    }

    public static boolean areInteger(double minpressure, double maxpressure){
        if (
            Converters.isInt(minpressure) &&
            Converters.isInt(maxpressure)
        ) return  true;
        else return false;
    }

    public static boolean arePositiveArray(Double...values){
        boolean arePositive = true;
        for(double myval:values){
            arePositive = arePositive && Converters.isPositive(myval);
        }
        return arePositive;
    }
    public static boolean areIntegerArray(Double...values){
        boolean areInteger = true;
        for(double myval:values){
            areInteger = areInteger && Converters.isInt(myval);
        }
        return areInteger;
    }

}
