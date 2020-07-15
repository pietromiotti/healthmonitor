package com.example.healthmonitor.utils;

public class ErrorHandler {


    /* check if there are at leat two arguments*/
    public boolean checkIfDialogFieldsAreCorrect(int position, int min_pressure, int max_pressure, double temperature, double weight){
        /*Check if there are at least two fields */
        int counter = 0;
        if(min_pressure != -1) counter++;
        if(max_pressure != -1) counter++;
        if(temperature != -1) counter++;
        if(weight != -1) counter++;
        if (counter>=2) return true;
        else return false;
    }
}
