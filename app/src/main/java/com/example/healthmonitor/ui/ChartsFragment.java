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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartsFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OnChartValueSelectedListenerHelper onChartValueSelectedListenerHelper;
    OnChartGestureListenerHelper onChartGestureListenerHelper;
    LineChart lineChart;
    MaterialButton averageButton;
    DatabaseManager databaseManager;


    public ChartsFragment() {
        // Required empty public constructor
    }


    public static ChartsFragment newInstance(String param1, String param2) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts, container, false);

        onChartGestureListenerHelper = new OnChartGestureListenerHelper();
        onChartValueSelectedListenerHelper = new OnChartValueSelectedListenerHelper(getContext().getApplicationContext());
        databaseManager = DatabaseManager.getInstanceDBNOContext();

        lineChart = view.findViewById(R.id.linechart);
        averageButton = view.findViewById(R.id.getMyAveragePressureButton);

        lineChart.setOnChartGestureListener(onChartGestureListenerHelper);
        lineChart.setOnChartValueSelectedListener(onChartValueSelectedListenerHelper);;


        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        ArrayList<Entry> yValue = new ArrayList<>();
        ArrayList<Entry> minPressure = new ArrayList<>();

        long x;
        Record current;
        int max_pressure, min_pressure;
        for (int i = 0; i < databaseManager.recordList.size(); i++){
            current = databaseManager.recordList.get(i);
            max_pressure = current.getMax_pressure();
            min_pressure = current.getMin_pressure();
            //Conversion Date
            x = Converters.dateToTimestamp(current.getDate());

            yValue.add(new Entry(x,max_pressure));
            minPressure.add(new Entry(x, min_pressure));

        }

        LineDataSet set1 = new LineDataSet(yValue, "Pressione Massima");
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);
        set1.setFillAlpha(110);
        LineDataSet set2 = new LineDataSet(minPressure, "Pressione Minima");
        set2.setColor(Color.GREEN);
        set2.setCircleColor(Color.GREEN);
        set2.setLineWidth(3f);
        set2.setCircleRadius(5f);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.BLACK);
        set2.setFillAlpha(110);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new MyAxisFormatter());
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        averageButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_chart_average, null));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

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

    public class OnChartValueSelectedListenerHelper implements OnChartValueSelectedListener {

        Context context;
        public OnChartValueSelectedListenerHelper(Context context){
            this.context = context;
        }
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
