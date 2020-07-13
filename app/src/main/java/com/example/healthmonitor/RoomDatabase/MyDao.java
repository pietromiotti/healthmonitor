package com.example.healthmonitor.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyDao {

    @Insert
    public void addRecord(Record record);

    @Delete
    public void deleteRecord(Record record);

    @Update
    public void updateRecord(Record record);

    @Query("SELECT * FROM records ORDER BY date ASC")
    public List<Record> getRecords();

}
