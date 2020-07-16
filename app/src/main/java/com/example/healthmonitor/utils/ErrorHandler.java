package com.example.healthmonitor.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;

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
}
