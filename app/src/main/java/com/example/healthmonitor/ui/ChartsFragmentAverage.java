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


/*Fragment relativo alla pressione media */
public class ChartsFragmentAverage extends Fragment {

    private int NULL_DEFAULT_VALUE = DatabaseManager.DEFAULT_NULL_VALUE;
    BarChart barChart;
    DatabaseManager databaseManager;


    private final int minPressure = 1;
    private final int maxPressure = 2;
    String[] months;


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

        months = getResources().getStringArray(R.array.month_value);

        /*Getting the getBarEntries */
        ArrayList<BarEntry> maxPressureEntry = new ArrayList<>(getBarEntriesBasedOnType(maxPressure));
        ArrayList<BarEntry> minPressureEntry = new ArrayList<>(getBarEntriesBasedOnType(minPressure));

        /*Define barDataSet with the defined coordinates - Each barDataSet will be a different Bar on the Chart*/
        BarDataSet minPressureDataSet = new BarDataSet(minPressureEntry, getResources().getString(R.string.minimumPressureAverage_label));
        BarDataSet maxPressureDataSet = new BarDataSet(maxPressureEntry, getResources().getString(R.string.maximumPressureAverage_label));

        /*Color Options */
        minPressureDataSet.setColor(Color.GREEN);
        maxPressureDataSet.setColor(Color.BLUE);

        dataSets.add(minPressureDataSet);
        dataSets.add(maxPressureDataSet);


        BarData data = new BarData(dataSets);

        barChart.setData(data);

        /* GRAPHIC SETTINGS */

        data.setValueTextSize(12f);
        data.setBarWidth(0.50f);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(4f);

        barChart.groupBars(0, 0, 0);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        barChart.getXAxis().setAxisMinValue(0);
        barChart.getXAxis().setAxisMaximum(12f);


        /*Formatting the Xvalues based on the month names*/
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


    /*This method retrieve the entries from the recordList on the dataBase */
    private ArrayList<BarEntry> getBarEntriesBasedOnType(int type){
        ArrayList<BarEntry> actual = new ArrayList<>();
        ArrayList<ArrayList<Double>> sparseMatrix = new ArrayList<ArrayList<Double>>();
        ArrayList<Record> mylist = new ArrayList<>(databaseManager.recordList);

        for(int q= 0; q<12; q++){
            sparseMatrix.add(new ArrayList<Double>(0));
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
                    if (current.getMin_pressure() != DatabaseManager.DEFAULT_NULL_VALUE){
                        currentArrayList.add((double) current.getMin_pressure());
                    }
                    break;
                case maxPressure:
                    currentArrayList = sparseMatrix.get(month);
                    if(current.getMax_pressure() != DatabaseManager.DEFAULT_NULL_VALUE){
                        currentArrayList.add((double) current.getMax_pressure());
                    }
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
            /*check if the list have at least one element*/
            if(currentArrayList.size()!= 0){
                average = (float) total / currentArrayList.size();
            }
            else average = 0;

            actual.add(new BarEntry(i, average));
        }
        return actual;
    }

}
