package com.example.healthmonitor.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.RecordAdapter;
import com.example.healthmonitor.ui.RecordsFragment;
import com.example.healthmonitor.utils.PreferenceManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;


/* Handler od DataBase Request */
public class DatabaseManager {
    String DB_NAME = "Mydb";
    public static DatabaseManager MyDatabaseManager;
    public List<Record> recordList;
    public List<Record> filteredList;
    private MyRoomDatabase mydatabase;
    private Context context;
    private PreferenceManager preferenceManager;


    private final int minPressureType = 1;
    private final int maxPressureType = 2;
    private final int temperatureType = 3;
    private final int weightType = 4;

    private class addRecordAsyncTask extends AsyncTask<Record, Void, Void> {
        @Override
        protected Void doInBackground(Record... records) {
            MyDatabaseManager.mydatabase.myDao().addRecord(records[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getRecords();
        }
    }

    private class deleteRecordAsyncTask extends AsyncTask<Record, Void, Void> {

        @Override
        protected Void doInBackground(Record... records) {
            MyDatabaseManager.mydatabase.myDao().deleteRecord(records[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getRecords();
        }
    }

    private class updateRecordAsyncTask extends AsyncTask<Record, Void, Void> {

        @Override
        protected Void doInBackground(Record... records) {
            MyDatabaseManager.mydatabase.myDao().updateRecord(records[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getRecords();
        }
    }

    private class getListRecordAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            recordList = mydatabase.myDao().getRecords();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateFilterList();
        }
    }

    private class DatabaseManagerAsync extends  AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {
            mydatabase = Room.databaseBuilder(contexts[0], MyRoomDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            preferenceManager = PreferenceManager.getPreferenceManagerNoContext();
            filteredList = new ArrayList<>();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getRecords();
        }
    }

    public static DatabaseManager getInstanceDb(Context context){
        if(MyDatabaseManager == null){
            MyDatabaseManager = new DatabaseManager(context);
        }
        return MyDatabaseManager;
    }

    public static DatabaseManager getInstanceDBNOContext(){
        return MyDatabaseManager;
    }

    public DatabaseManager(Context context){
        this.context = context;
        DatabaseManagerAsync initializer = new DatabaseManagerAsync();
        initializer.execute(this.context);

    }

    public void addRecord(Record r){
        addRecordAsyncTask addRecord = new addRecordAsyncTask();
        addRecord.execute(r);
    }

    public void deleteRecord(Record r){
        deleteRecordAsyncTask deleteRecord = new deleteRecordAsyncTask();
        deleteRecord.execute(r);
    }

    public void updateRecord(Record r, int min_pressure, int max_pressure, double temperature, double weight, Date date){
        if (weight != -1) r.setWeight(weight);
        if(temperature != -1) r.setTemperature(temperature);
        if(max_pressure != -1) r.setMax_pressure(max_pressure);
        if(min_pressure != -1) r.setMin_pressure(min_pressure);
        if(date != null) r.setDate(date);
        updateRecordAsyncTask updateRecord = new updateRecordAsyncTask();
        updateRecord.execute(r);
    }

    public void getRecords() {
        getListRecordAsyncTask getList = new getListRecordAsyncTask();
        getList.execute();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public Record initRecord(Integer min_pressure, Integer max_pressure, Double temperature, Double weight, Date date, Boolean isSummary) {
        Record record = new Record();
        record.setMin_pressure(min_pressure);
        record.setMax_pressure(max_pressure);
        record.setTemperature(temperature);
        record.setWeight(weight);
        if (date != null) {
            record.setDate(date);
        }
        record.setIsSummary(isSummary);
        return record;
    }

    public List<Record>getRecordsDate(Date selected){
        List<Record> selectedDateRecords = new ArrayList<>();
        boolean sameDay;
        Calendar cal_selected = Calendar.getInstance();
        Calendar cal_current = Calendar.getInstance();
        cal_selected.setTime(selected);
        Record current;

        for (int i =0; i< recordList.size(); i++){
            current = recordList.get(i);
            cal_current.setTime(current.getDate());
            sameDay = cal_selected.get(Calendar.DAY_OF_YEAR) == cal_current.get(Calendar.DAY_OF_YEAR) &&
                    cal_selected.get(Calendar.YEAR) == cal_current.get(Calendar.YEAR);
            if(sameDay) {
                selectedDateRecords.add(current);
            }

        }
        return selectedDateRecords;
    }

    public void Close(){
        if (mydatabase.isOpen()) mydatabase.close();
    }


    private class updateFilteredListAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            filteredList.clear();
            for(int i = 0; i < recordList.size(); i++){
                try {
                    if (preferenceManager.filteringRecord(recordList.get(i))){
                        filteredList.add(recordList.get(i));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void updateFilterList(){
        updateFilteredListAsyncTask updateFiltered = new updateFilteredListAsyncTask();
        updateFiltered.execute();
    }


    public ArrayList<Record> recordsInIntervalTime(){
        ArrayList<Record> recordsInIntervalTine = new ArrayList<>();
        Calendar myLimitDate = Calendar.getInstance();
        Date today = new Date();

        myLimitDate.setTime(today);
        myLimitDate.add(Calendar.DAY_OF_MONTH, - preferenceManager.getIntervalMonitorTime());
        Record current;
        for(int i= 0; i< recordList.size(); i++){
            current = recordList.get(i);
            if(current.getDate().after(myLimitDate.getTime()) && ((current.getDate().before(today) || (current.getDate().equals(today))))){
                recordsInIntervalTine.add(current);
            }
        }
        return recordsInIntervalTine;
    }


    public boolean triggerNotificationPressureThreShold(){
        ArrayList<Record> myArrayList = recordsInIntervalTime();
        if (preferenceManager.pressurePriorityGreaterThanMonitorValue()){
            int minPressureAverageLowerBound = preferenceManager.getMinPressureAverageLowerBound();
            int minPressureAverageUpperBound = preferenceManager.getMinPressureAverageUpperBound();
            int maxPressureAverageLowerBound = preferenceManager.getMaxPressureAverageLowerBound();
            int maxPressureAverageUpperBound = preferenceManager.getMaxPressureAverageUpperBound();

            double averageMinPressure = getAverage(myArrayList, minPressureType);
            double averageMaxPressure = getAverage(myArrayList, maxPressureType);

            if( (averageMinPressure>= minPressureAverageUpperBound || averageMinPressure< minPressureAverageLowerBound) ||
                    (averageMaxPressure>= maxPressureAverageUpperBound || averageMaxPressure< maxPressureAverageLowerBound)){
                Log.i("AVERAGE", "PRESSURE BOOLEAN --> TRUE");
                return true;
            }
        }
        return false;
    }

    public boolean triggerNotificationTemperatureThreShold(){
        ArrayList<Record> myArrayList = recordsInIntervalTime();
        if (preferenceManager.temperaturePriorityGreaterThanMonitorValue()){
            double temperatureAverageLowerBound = preferenceManager.getTemperatureAverageLowerBound();
            double temperatureAverageUpperBound = preferenceManager.getTemperatureAverageUpperBound();

            double averageTemperature = getAverage(myArrayList, temperatureType );

            if(averageTemperature>= temperatureAverageUpperBound || averageTemperature <= temperatureAverageLowerBound) {
                Log.i("AVERAGE", "TEMPERATURE BOOLEAN --> TRUE");
                return true;
            }
        }
        return false;
    }

    public boolean triggerNotificationWeightThreShold(){
        ArrayList<Record> myArrayList = recordsInIntervalTime();
        if (preferenceManager.weightPriorityGreaterThanMonitorValue()){
            double weightAverageLowerBound = preferenceManager.getWeightAverageLowerBound();
            double weightAverageUpperBound = preferenceManager.getWeightAverageUpperBound();

            double averageweight = getAverage(myArrayList, weightType);

            if(averageweight>= weightAverageUpperBound || averageweight <= weightAverageLowerBound) {
                Log.i("AVERAGE", "BOOLEAN --> TRUE");
                return true;
            }
        }
        return false;
    }

    private double getAverage(ArrayList<Record> myList, int type){
        double sum = 0;
        double average = 0;
        int counter = 0;
        Record current;
        if (myList.size() != 0) {
            for (int i = 0; i < myList.size(); i++) {
                current = myList.get(i);
                switch (type) {
                    case minPressureType:
                        if (current.getMin_pressure() != -1) {
                            sum = sum + current.getMin_pressure();
                            counter = counter + 1;
                        }
                        break;
                    case maxPressureType:
                        if (current.getMax_pressure() != -1) {
                            sum = sum + current.getMax_pressure();
                            counter = counter + 1;
                        }
                        break;
                    case temperatureType:
                        if (current.getTemperature() != -1) {
                            sum = sum + current.getTemperature();
                            counter = counter + 1;
                        }
                        break;
                    case weightType:
                        if (current.getWeight() != -1) {
                            sum = sum + current.getWeight();
                            counter = counter + 1;
                        }
                        break;
                    default:
                        break;
                }
            }
            average = sum / counter;
        }
        else average = 0;
        Log.i("AVERAGE", "AVERAGE: ->" + average);
        return average;
    }

    public boolean isTodayAlreadyRecorded(){
        Date today = new Date();
        List<Record> mylist = getRecordsDate(today);
        if(mylist.size() != 0) {
            return true;
        }
        else return false;
    }

    /*
    public List<Record> filteredRecords() throws ParseException {
        List<Record> filtered = new ArrayList<>();
        for(int i = 0; i < recordList.size(); i++){
            if (preferenceManager.filteringRecord(recordList.get(i))){
                filtered.add(recordList.get(i));
            }
        }
        return filtered;
    }

     */
}
