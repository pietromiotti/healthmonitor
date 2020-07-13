package com.example.healthmonitor.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.Record;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChartsFragmentAverage extends Fragment {

    private int NULL_DEFAULT_VALUE = -1;
    BarChart barChart;
    DatabaseManager databaseManager;

    private ArrayList<BarEntry> januaryBarEntry;
    private ArrayList<BarEntry> februaryBarEntry;
    private ArrayList<BarEntry> marchBarEntry;
    private ArrayList<BarEntry> aprilBarEntry;
    private ArrayList<BarEntry> mayBarEntry;
    private ArrayList<BarEntry> juneBarEntry;
    private ArrayList<BarEntry> julyBarEntry;
    private ArrayList<BarEntry> augustBarEntry;
    private ArrayList<BarEntry> septemberBarEntry;
    private ArrayList<BarEntry> octoberBarEntry;
    private ArrayList<BarEntry> novemberBarEntry;
    private ArrayList<BarEntry> decemberBarEntry;


    private int Jan = 0;
    private int Feb = 1;
    private int Mar = 2;
    private int Apr = 3;
    private int May = 4;
    private int Jun = 5;
    private int Jul = 6;
    private int Aug = 7;
    private int Sept = 8;
    private int Oct = 9;
    private int Nov = 10;
    private int Dec = 11;

    private final int minPressure = 1;
    private final int maxPressure = 2;


    public ChartsFragmentAverage() {
        // Required empty public constructor
    }


    public static ChartsFragmentAverage newInstance(String param1, String param2) {
        ChartsFragmentAverage fragment = new ChartsFragmentAverage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts_average, container, false);
        // Inflate the layout for this fragment
        databaseManager = DatabaseManager.getInstanceDBNOContext();

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        barChart = view.findViewById(R.id.barchart);

        String[] months = new String[]{"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

        ArrayList<BarEntry> maxPressureEntry = new ArrayList<>(getBarEntriesBasedOnType(maxPressure));
        ArrayList<BarEntry> minPressureEntry = new ArrayList<>(getBarEntriesBasedOnType(minPressure));

        BarDataSet minPressureDataSet = new BarDataSet(minPressureEntry, "Pressione Minima - Media");
        BarDataSet maxPressureDataSet = new BarDataSet(maxPressureEntry, "Pressione Massima - Media");

        minPressureDataSet.setColor(Color.GREEN);
        maxPressureDataSet.setColor(Color.BLUE);

        dataSets.add(minPressureDataSet);
        dataSets.add(maxPressureDataSet);

        Log.i("MINPRESSURE", "DATASET" + dataSets.toString());

        BarData data = new BarData(dataSets);

        barChart.setData(data);


        /* GRAPHIC SETTINGS */

        data.setBarWidth(0.50f);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(4.5f);


        barChart.groupBars(0, 0, 0);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        barChart.getXAxis().setAxisMinValue(0);
        barChart.getXAxis().setAxisMaximum(12f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);




        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(60f);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public ArrayList<BarEntry> getBarEntriesBasedOnType(int type){
        ArrayList<BarEntry> actual = new ArrayList<>();
        ArrayList<ArrayList<Double>> sparseMatrix = new ArrayList<ArrayList<Double>>();
        ArrayList<Record> mylist = new ArrayList<>(databaseManager.recordList);

        for(int q= 0; q<12; q++){
            sparseMatrix.add(new ArrayList<Double>());
        }

        Record current;
        ArrayList<Double> currentArrayList;
        Calendar calendar = Calendar.getInstance();
        int month;
        for (int j = 0; j < mylist.size(); j++) {
            current = mylist.get(j);
            calendar.setTime(current.getDate());
            month = calendar.get(Calendar.MONTH);
            switch (type) {
                case minPressure:
                    currentArrayList = sparseMatrix.get(month);
                    currentArrayList.add((double) current.getMin_pressure());
                    break;
                case maxPressure:
                    currentArrayList = sparseMatrix.get(month);
                    currentArrayList.add((double) current.getMax_pressure());
                    break;
                default:
                    break;
            }
        }
        double total;
        float average = 0;
        for(int i=0; i<12; i++){
            total = 0;
            currentArrayList = sparseMatrix.get(i);
            for(int m= 0; m< currentArrayList.size(); m++){
                total = total + currentArrayList.get(m);
            }
            average = (float) total / currentArrayList.size();
            actual.add(new BarEntry(i, average));
        }
        return actual;
    }

    public ArrayList<BarEntry> getBarEntries(int monthToAnalyze) {
        int totalMaxPressure = 0;
        int totalMinPressure = 0;
        int counterMaxPressure = 0;
        int counterMinPressure = 0;
        float finalresut_maxPressure = 0;
        float finalresult_minPressure = 0;
        ArrayList<Record> mylist = new ArrayList<>(databaseManager.recordList);
        Record current;
        Calendar calendar = Calendar.getInstance();
        for (int j = 0; j < mylist.size(); j++) {
            current = mylist.get(j);
            calendar.setTime(current.getDate());
            if (calendar.get(Calendar.MONTH) == monthToAnalyze) {
                if (current.getMin_pressure() != NULL_DEFAULT_VALUE) {
                    counterMinPressure = counterMinPressure + 1;
                    totalMinPressure = totalMinPressure + current.getMin_pressure();
                }
                if (current.getMax_pressure() != NULL_DEFAULT_VALUE) {
                    counterMaxPressure = counterMaxPressure + 1;
                    totalMaxPressure = totalMaxPressure + 1;
                }
            }
        }
        finalresult_minPressure = (float) totalMinPressure / counterMinPressure;
        finalresut_maxPressure = (float) totalMaxPressure / counterMaxPressure;

        ArrayList<BarEntry> newBarEntry = new ArrayList<>();
        newBarEntry.add(new BarEntry(minPressure, finalresult_minPressure));
        newBarEntry.add(new BarEntry(maxPressure, finalresut_maxPressure));
        return newBarEntry;
    }
    /*
    public void addBarEntries(int monthToAnalyze, float averageMinPressure, float averageMaxPressure){
        if(monthToAnalyze == Jan){
            januaryBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            januaryBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Feb){
            februaryBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            februaryBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Mar){
            marchBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            marchBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Apr){
            aprilBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            aprilBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == May){
            mayBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            mayBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Jun){
            juneBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            juneBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Jul){
            julyBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            julyBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Aug){
            augustBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            augustBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Sept){
            septemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            septemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Oct){
            octoberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            octoberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Nov){
            novemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            novemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Dec){
            decemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            decemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }





/*


       /* januaryBarEntry = new ArrayList<>();
        februaryBarEntry = new ArrayList<>();
        marchBarEntry = new ArrayList<>();
        aprilBarEntry = new ArrayList<>();
        mayBarEntry = new ArrayList<>();
        juneBarEntry = new ArrayList<>();
        julyBarEntry = new ArrayList<>();
        augustBarEntry = new ArrayList<>();
        septemberBarEntry = new ArrayList<>();
        octoberBarEntry = new ArrayList<>();
        novemberBarEntry = new ArrayList<>();
        decemberBarEntry = new ArrayList<>();
        */
/*
       januaryBarEntry = getBarEntries(Jan);
       februaryBarEntry = getBarEntries(Feb);
       marchBarEntry = getBarEntries(Mar);
       aprilBarEntry = getBarEntries(Apr);
       mayBarEntry = getBarEntries(May);
       juneBarEntry = getBarEntries(Jun);
       julyBarEntry = getBarEntries(Jul);
       augustBarEntry = getBarEntries(Aug);
       septemberBarEntry = getBarEntries(Sept);
       octoberBarEntry= getBarEntries(Oct);
       novemberBarEntry = getBarEntries(Nov);
       decemberBarEntry = getBarEntries(Dec);

        BarDataSet barDataSetJan = new BarDataSet()
        XAxis xAxis = barChart.getXAxis()

    public class uploadAverageAsyncTask extends AsyncTask<Void, Void, Void>{

        int monthToAnalyze;
        ArrayList<Record> mylist;
        ArrayList<BarEntry> barEntries;
        int totalMinPressure;
        int totalMaxPressure;
        int counterMinPressure;
        int counterMaxPressure;
        float finalresult_minPressure;
        float finalresut_maxPressure;

        public uploadAverageAsyncTask(int i){
            this.monthToAnalyze = i;
            this.mylist = new ArrayList<>(databaseManager.recordList;
            totalMaxPressure = 0;
            totalMinPressure = 0;
            counterMaxPressure = 0;
            counterMinPressure = 0;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Record current;
            Calendar calendar = Calendar.getInstance();
            BarEntry barEntry;
            for (int j = 0; j < mylist.size(); j++) {
                current = mylist.get(j);
                calendar.setTime(current.getDate());
                if (calendar.get(Calendar.MONTH) == monthToAnalyze) {
                    if (current.getMin_pressure() != NULL_DEFAULT_VALUE) {
                        counterMinPressure = counterMinPressure + 1;
                        totalMinPressure = totalMinPressure + current.getMin_pressure();
                    }
                    if (current.getMax_pressure() != NULL_DEFAULT_VALUE) {
                        counterMaxPressure = counterMaxPressure + 1;
                        totalMaxPressure = totalMaxPressure + 1;
                    }
                }
            }
            finalresult_minPressure = (float) totalMinPressure / counterMinPressure;
            finalresut_maxPressure = (float) totalMaxPressure / counterMaxPressure;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            addBarEntries(monthToAnalyze, finalresult_minPressure, finalresut_maxPressure);
        }
    }

    public void addBarEntries(int monthToAnalyze, float averageMinPressure, float averageMaxPressure){
       /* if(monthToAnalyze == Jan){
            januaryBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            januaryBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Feb){
            februaryBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            februaryBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Mar){
            marchBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            marchBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Apr){
            aprilBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            aprilBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == May){
            mayBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            mayBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Jun){
            juneBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            juneBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Jul){
            julyBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            julyBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
        else if (monthToAnalyze == Aug){
            augustBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            augustBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Sept){
            septemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            septemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Oct){
            octoberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            octoberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Nov){
            novemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            novemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }
         else if (monthToAnalyze == Dec){
            decemberBarEntry.add(new BarEntry(minPressure, averageMinPressure));
            decemberBarEntry.add(new BarEntry(maxPressure, averageMaxPressure));
        }


       ArrayList<BarEntry> myArray = new ArrayList<BarEntry>();
       myArray.add(new BarEntry(minPressure, averageMinPressure));
       myArray.add(new BarEntry(maxPressure, averageMaxPressure));
       myArrayListEntries.add(monthToAnalyze, myArray);
    }
*/



}
