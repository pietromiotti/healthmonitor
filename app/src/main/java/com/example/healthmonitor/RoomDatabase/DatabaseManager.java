package com.example.healthmonitor.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.RecordAdapter;
import com.example.healthmonitor.ui.RecordsFragment;

import java.util.Date;
import java.util.List;


/* Handler od DataBase Request */
public class DatabaseManager {
    String DB_NAME = "Mydb";
    public static DatabaseManager MyDatabaseManager;
    private MyRoomDatabase mydatabase;
    private Context context;

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
            RecordAdapter.recordList = mydatabase.myDao().getRecords();
            return null;
        }
    }

    private class DatabaseManagerAsync extends  AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {
            mydatabase = Room.databaseBuilder(contexts[0], MyRoomDatabase.class, DB_NAME).build();
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

    public Record initRecord(Integer min_pressure, Integer max_pressure, Double temperature, Double weight, Date date) {
        Record record = new Record();
        record.setMin_pressure(min_pressure);
        record.setMax_pressure(max_pressure);
        record.setTemperature(temperature);
        record.setWeight(weight);
        if (date != null) {
            record.setDate(date);
        }
        return record;
    }

    public void Close(){
        if (mydatabase.isOpen()) mydatabase.close();
    }
}
