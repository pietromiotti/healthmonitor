package com.example.healthmonitor.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.utils.Converters;
import com.example.healthmonitor.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class FilterFragment extends Fragment {

    NavigationView navigationView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatePickerDialog.OnDateSetListener datePickerDialogFrom;
    private DatePickerDialog.OnDateSetListener datePickerDialogTo;
    private Calendar dateFromFilterValue;
    private Calendar dateToFilterValue;
    private int minPressureFromFilterValue;
    private int minPressureToFilterValue;
    private int maxPressureFromFilterValue;
    private int maxPressureToFilterValue;
    private double temperatureFromFilterValue;
    private double temperatureToFilterValue;
    private double weightFromFilterValue;
    private double weightToFilterValue;
    private PreferenceManager preferenceManager;
    private DatabaseManager databaseManager;

    private MaterialButton selectDateFrom;
    private TextView selectedDataFrom;
    private MaterialButton selectDateTo;
    private TextView selectedDataTo;
    private EditText selectedMinPressureFrom;
    private EditText selectedMinPressureTo;
    private EditText selectedMaxPressureFrom;
    private EditText selectedMaxPressureTo;
    private EditText selectedTemperatureFrom;
    private EditText selectedTemperatureTo;
    private EditText selectedWeightFrom;
    private EditText selectedWeightTo;
    private MaterialButton applyButtonFilter;
    private MaterialButton resetButtonFilter;
    private MaterialButton cancelButtonFilter;

    public FilterFragment() {
        // Required empty public constructor
    }


    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
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
        this.preferenceManager = PreferenceManager.getPreferenceManagerNoContext();
        this.databaseManager = DatabaseManager.getInstanceDBNOContext();
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_filter, container, false);
        navigationView = getActivity().findViewById(R.id.nav_filters);
        selectDateFrom = myview.findViewById(R.id.chooseFromDate);
        selectedDataFrom = myview.findViewById(R.id.selectedFromDate);
        selectDateTo = myview.findViewById(R.id.chooseToDate);
        selectedDataTo = myview.findViewById(R.id.selectedToDate);
        selectedMinPressureFrom = myview.findViewById(R.id.minPressureFilter_from_val);
        selectedMinPressureTo = myview.findViewById(R.id.minPressureFilter_to_val);
        selectedMaxPressureFrom = myview.findViewById(R.id.maxPressureFilter_from_val);
        selectedMaxPressureTo = myview.findViewById(R.id.maxPressureFilter_to_val);
        selectedTemperatureFrom = myview.findViewById(R.id.temperatureFilter_from_val);
        selectedTemperatureTo = myview.findViewById(R.id.temperatureFilter_to_val);
        selectedWeightFrom = myview.findViewById(R.id.weightFilter_from_val);
        selectedWeightTo = myview.findViewById(R.id.weightFilter_to_val);
        applyButtonFilter = myview.findViewById(R.id.applyFilters);
        resetButtonFilter = myview.findViewById(R.id.resetFilters);
        cancelButtonFilter = myview.findViewById(R.id.cancelFilters);


        selectDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePickerDialogFrom, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datePickerDialogFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFromFilterValue = Calendar.getInstance();
                dateFromFilterValue.set(year, month, dayOfMonth, 0, 0, 0);
                selectedDataFrom.setText(Converters.printDate(dateFromFilterValue.getTime()));
            }
        };

        selectDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePickerDialogTo, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datePickerDialogTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateToFilterValue = Calendar.getInstance();
                dateToFilterValue.set(year, month, dayOfMonth, 23, 59, 59);
                selectedDataTo.setText(Converters.printDate(dateToFilterValue.getTime()));
            }
        };


        applyButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minPressureFromFilterValue = Converters.parseStringToInt(selectedMinPressureFrom.getText().toString());
                minPressureToFilterValue = Converters.parseStringToInt(selectedMinPressureTo.getText().toString());
                maxPressureFromFilterValue = Converters.parseStringToInt(selectedMaxPressureFrom.getText().toString());
                maxPressureToFilterValue = Converters.parseStringToInt(selectedMaxPressureTo.getText().toString());
                temperatureFromFilterValue = Converters.parseStringToDouble(selectedTemperatureFrom.getText().toString());
                temperatureToFilterValue = Converters.parseStringToDouble(selectedTemperatureTo.getText().toString());
                weightFromFilterValue = Converters.parseStringToDouble(selectedWeightFrom.getText().toString());
                weightToFilterValue = Converters.parseStringToDouble(selectedWeightTo.getText().toString());

                if (dateFromFilterValue != null) {
                    preferenceManager.setDateFromPreference(dateFromFilterValue.getTime());
                }
                if (dateToFilterValue != null) {
                    preferenceManager.setDateToPreference(dateToFilterValue.getTime());
                }
                preferenceManager.setMinPressureFrom(minPressureFromFilterValue);
                preferenceManager.setMinPressureTo(minPressureToFilterValue);
                preferenceManager.setMaxPressureFrom(maxPressureFromFilterValue);
                preferenceManager.setMaxPressureTo(maxPressureToFilterValue);
                preferenceManager.setTemperatureFrom(temperatureFromFilterValue);
                preferenceManager.setTemperatureTo(temperatureToFilterValue);
                preferenceManager.setWeightFrom(weightFromFilterValue);
                preferenceManager.setWeightTo(weightToFilterValue);

                databaseManager.updateFilterList();
                getActivity().onBackPressed();
            }
        });

        resetButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager.resetFilterPreference();
                databaseManager.updateFilterList();

                getActivity().onBackPressed();
            }

        });

        cancelButtonFilter.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_records, null));


        return myview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date currentFilterFromDate = Converters.fromTimestamp(Long.MIN_VALUE);
        Date currentFilterToDate = Converters.fromTimestamp(Long.MAX_VALUE);
        try {
            currentFilterFromDate = preferenceManager.getDateFromPreference();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            currentFilterToDate = preferenceManager.getDateToPreference();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int currentFilterFromMinPressure = preferenceManager.getMinPressureFrom();
        int currentFilterToMinPressure = preferenceManager.getMinPressureTo();
        int currentFilterFromMaxPressure = preferenceManager.getMaxPressureFrom();
        int currentFilterToMaxPressure = preferenceManager.getMaxPressureTo();
        double currentFilterFromTemperature = preferenceManager.getTemperatureFrom();
        double currentFilterToTemperature = preferenceManager.getTemperatureTo();
        double currentFilterFromWeight = preferenceManager.getWeightFrom();
        double currentFilterToWeight = preferenceManager.getWeightTo();


        if (!Converters.areSameDay(currentFilterFromDate, Converters.fromTimestamp(Long.MIN_VALUE))) {
            selectedDataFrom.setText(Converters.printDate(currentFilterFromDate));
        }
        if (!Converters.areSameDay(currentFilterToDate, Converters.fromTimestamp(Long.MAX_VALUE))) {
            selectedDataTo.setText(Converters.printDate(currentFilterToDate));
        }
        if (currentFilterFromMinPressure !=  -1) {
            selectedMinPressureFrom.setText(String.valueOf(preferenceManager.getMinPressureFrom()));
        }
        if (currentFilterToMinPressure !=  -1) {
            selectedMinPressureTo.setText(String.valueOf(preferenceManager.getMinPressureTo()));
        }
        if (currentFilterFromMaxPressure != -1) {
            selectedMaxPressureFrom.setText(String.valueOf(preferenceManager.getMaxPressureFrom()));
        }
        if (currentFilterToMaxPressure != -1) {
            selectedMaxPressureTo.setText(String.valueOf(preferenceManager.getMaxPressureTo()));
        }
        if (currentFilterFromTemperature != -1) {
            selectedTemperatureFrom.setText(String.valueOf(preferenceManager.getTemperatureFrom()));
        }
        if (currentFilterToTemperature != -1) {
            selectedTemperatureTo.setText(String.valueOf(preferenceManager.getTemperatureTo()));
        }
        if (currentFilterFromWeight != -1) {
            selectedWeightFrom.setText(String.valueOf(preferenceManager.getWeightFrom()));
        }
        if (currentFilterToWeight != -1) {
            selectedWeightTo.setText(String.valueOf(preferenceManager.getWeightTo()));
        }
    }
}
