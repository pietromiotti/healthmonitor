package com.example.healthmonitor.RoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "records")
public class Record {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private int min_pressure;

    private int max_pressure;

    private double temperature;

    private double weight;

    /*Extra field that helps me with the Record Adapter rendering*/
    private boolean isSummary;

    public void setId(int id){
        this.id =id;
    }

    public int getId() {
        return id;
    }

    public void setIsSummary(boolean isSummary){
        this.isSummary =isSummary;
    }
    public boolean getIsSummary(){
        return this.isSummary;
    }

    public Date getDate(){
        return this.date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public int getMin_pressure(){
        return min_pressure;
    }

    public int getMax_pressure() {
        return max_pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWeight() {
        return weight;
    }

    public void setMin_pressure(int min_pressure) {
        this.min_pressure = min_pressure;
    }

    public void setMax_pressure(int max_pressure) {
        this.max_pressure = max_pressure;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}


