package com.example.healthmonitor.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.Record;
import com.example.healthmonitor.utils.Converters;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChartsFragment extends Fragment{

    OnChartValueSelectedListenerHelper onChartValueSelectedListenerHelper;
    OnChartGestureListenerHelper onChartGestureListenerHelper;
    LineChart lineChart;
    MaterialButton averageButton;
    DatabaseManager databaseManager;


    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts, container, false);

        /*Initialize Listner to trigger action on the chart */
        onChartGestureListenerHelper = new OnChartGestureListenerHelper();
        onChartValueSelectedListenerHelper = new OnChartValueSelectedListenerHelper(getContext().getApplicationContext());

        databaseManager = DatabaseManager.getInstanceDBNOContext();

        lineChart = view.findViewById(R.id.linechart);
        averageButton = view.findViewById(R.id.getMyAveragePressureButton);

        lineChart.setOnChartGestureListener(onChartGestureListenerHelper);
        lineChart.setOnChartValueSelectedListener(onChartValueSelectedListenerHelper);;


        /*Option on the graphic */
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        ArrayList<Entry> maxpressure_yValue = new ArrayList<>();
        ArrayList<Entry> minPressure_yValue = new ArrayList<>();

        long x;
        Record current;
        int max_pressure, min_pressure;
        /*Iterate all my records */
        for (int i = 0; i < databaseManager.recordList.size(); i++){
            current = databaseManager.recordList.get(i);
            max_pressure = current.getMax_pressure();
            min_pressure = current.getMin_pressure();
            //Conversion Date
            x = Converters.dateToTimestamp(current.getDate());

            /*Check if values are "null", they do not have to be considered */
            if(max_pressure != DatabaseManager.DEFAULT_NULL_VALUE){
                maxpressure_yValue.add(new Entry(x,max_pressure));
            }
            if(min_pressure != DatabaseManager.DEFAULT_NULL_VALUE){
                minPressure_yValue.add(new Entry(x, min_pressure));
            }
        }

        /*Define LineDataSets with the defined coordinates - Each LineDataSet will be a different line on the Chart*/
        LineDataSet maxpressure_line = new LineDataSet(maxpressure_yValue, "Pressione Massima");
        LineDataSet minpressure_line = new LineDataSet(minPressure_yValue , "Pressione Minima");


        /*Graphic details of the lines */
        maxpressure_line.setColor(Color.BLUE);
        maxpressure_line.setCircleColor(Color.BLUE);
        maxpressure_line.setLineWidth(3f);
        maxpressure_line.setCircleRadius(5f);
        maxpressure_line.setValueTextSize(10f);
        maxpressure_line.setValueTextColor(Color.BLACK);
        maxpressure_line.setFillAlpha(110);


        minpressure_line.setColor(Color.GREEN);
        minpressure_line.setCircleColor(Color.GREEN);
        minpressure_line.setLineWidth(3f);
        minpressure_line.setCircleRadius(5f);
        minpressure_line.setValueTextSize(10f);
        minpressure_line.setValueTextColor(Color.BLACK);
        minpressure_line.setFillAlpha(110);

        /* This Array contains all the "Lines" to be rendered in the Chart */
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(maxpressure_line);
        dataSets.add(minpressure_line);


        /* LineData is the final Type to be upload in the chart*/
        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);


        /*Option in formatting the XVALUES, since Date format is not accepted and should be converted in string thanks to my personalized formatter "MyAxisFormatter"*/
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new MyAxisFormatter());
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        averageButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_chart_average, null));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /*Converts float (which is a Date in my case) to string to be rendered in the chart */
    public class MyAxisFormatter extends ValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return Converters.printDateDayMonthYear(new Date((long) value));
        }
    }

    public class OnChartGestureListenerHelper implements OnChartGestureListener {
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    }


    /* I implemented a single feature just to show what it could be done*/
    public class OnChartValueSelectedListenerHelper implements OnChartValueSelectedListener {

        Context context;
        public OnChartValueSelectedListenerHelper(Context context){
            this.context = context;
        }

        /*If a point is clicked this callback prints in a Toast its coordinates */
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            double pointY = e.getY();
            String pointX = Converters.printDateDayMonthYear(new Date((long) e.getX()));
            Toast.makeText(context, "DATE: " + pointX + "\n"+ "VALUE : " + pointY, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected() {

        }

    }



}
