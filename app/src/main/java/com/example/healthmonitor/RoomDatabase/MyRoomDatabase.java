package com.example.healthmonitor.RoomDatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.healthmonitor.utils.Converters;

@Database(entities = {Record.class}, version =1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
