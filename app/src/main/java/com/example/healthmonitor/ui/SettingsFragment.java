package com.example.healthmonitor.ui;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.utils.Converters;
import com.example.healthmonitor.utils.ErrorHandler;
import com.example.healthmonitor.utils.NotificationHandler;
import com.example.healthmonitor.utils.PreferenceManager;

import android.app.DirectAction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.example.healthmonitor.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;


public class SettingsFragment extends PreferenceFragmentCompat  {


    private int PRIORITY_MONITOR_VALUE = PreferenceManager.DEFAULT_MONITOR_VALUE;
    MaterialButton selectHour;
    PreferenceManager preferenceManager;

    private static Preference selectedHour;
    private Preference datePicker;
    private Preference dailyNotification;
    private ListPreference weightPriority;
    private ListPreference temperaturePriority;
    private ListPreference pressurePriority;

    private Preference intervalTime;
    private Preference minPressureAverageLowerBound;
    private Preference minPressureAverageUpperBound;
    private Preference maxPressureAverageLowerBound;
    private Preference maxPressureAverageUpperBound;
    private Preference weightAverageLowerBound;
    private Preference weightAverageUpperBound;
    private Preference temperatureAverageLowerBound;
    private Preference temperatureAverageUpperBound;

    private PreferenceCategory pressureCategory;
    private PreferenceCategory weightCategory;
    private PreferenceCategory temperatureCategory;

    private NotificationHandler notificationHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            this.preferenceManager = PreferenceManager.getPreferenceManagerNoContext();
            this.notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
            // Load the preferences from an XML resource
            setPreferencesFromResource(R.xml.fragment_settings, rootKey);

            datePicker = findPreference("dateTimePicker");
            selectedHour = findPreference("datePickerValue");
            dailyNotification = findPreference("daily_notify");

            weightPriority = findPreference("weightPriority");
            temperaturePriority = findPreference("temperaturePriority");
            pressurePriority = findPreference("pressurePriority");

            intervalTime = findPreference("intervalTime");
            minPressureAverageLowerBound = findPreference("minPressureAverageLowerBound");
            minPressureAverageUpperBound = findPreference("minPressureAverageUpperBound");
            maxPressureAverageLowerBound = findPreference("maxPressureAverageLowerBound");
            maxPressureAverageUpperBound = findPreference("maxPressureAverageUpperBound");
            weightAverageLowerBound = findPreference("weightAverageLowerBound");
            weightAverageUpperBound = findPreference("weightAverageUpperBound");
            temperatureAverageLowerBound = findPreference("temperatureAverageLowerBound");
            temperatureAverageUpperBound = findPreference("temperatureAverageUpperBound");

            pressureCategory = findPreference("pressureCategory");
            weightCategory = findPreference("weightCategory");
            temperatureCategory = findPreference("temperatureCategory");



            dailyNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean switched = (Boolean) newValue;
                    preferenceManager.setDailyNotification(switched);
                    makeDatePickerVisible();

                    Date date = preferenceManager.getDailyNotificationHour();
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    Log.i("NOTIFICATION", "ALARM FROM PREFERENCE MANAGER" + Converters.printDate(c.getTime()));
                    if(switched){
                        notificationHandler.startAlarm(c);
                    }
                    else notificationHandler.cancelAlarm();

                    return true;
                }
            });

            datePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            Calendar newCalendar = Calendar.getInstance();
                            newCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            newCalendar.set(Calendar.MINUTE, selectedMinute);
                            Log.i("NOTIFICATION", "TIME" + newCalendar.getTime());
                            //newCalendar.set(0,0,0,selectedHour,selectedMinute,0);
                            notificationHandler.startAlarm(newCalendar);
                            preferenceManager.setDailyNotificationHour(newCalendar.getTime());
                            SettingsFragment.selectedHour.setTitle("Ora della notifica giornaliera: " + Converters.printDateHourAndMinutes(newCalendar.getTime()));
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Scegli l'ora");
                    mTimePicker.show();
                    return true;
                }
            });


            weightPriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setWeightPriority(value);
                    makeWeightVisibile(value);
                    if(value>= PRIORITY_MONITOR_VALUE) notificationHandler.triggerNotificationInfo();
                    return false;
                }
            });

            temperaturePriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setTemperaturePriority(value);
                    makeTemperatureVisible(value);
                    if(value>= PRIORITY_MONITOR_VALUE) notificationHandler.triggerNotificationInfo();
                    return false;
                }
            });

            pressurePriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setPressurePriority(value);
                    makePressureVisibile(value);
                    if(value>= PRIORITY_MONITOR_VALUE) notificationHandler.triggerNotificationInfo();
                    return false;
                }
            });

            intervalTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setIntervalMonitorTime((int) value);
                            intervalTime.setSummary("Attuale tempo di monitoraggio " + (int) value);
                        }
                        else ErrorHandler.IntervalTimermustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });

            minPressureAverageLowerBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setMinPressureAverageLowerBound((int)value);
                            minPressureAverageLowerBound.setSummary("Threshold attuale: " + (int) value);
                        }
                        else ErrorHandler.mustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });
            minPressureAverageUpperBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setMinPressureAverageUpperBound((int)value);
                            minPressureAverageUpperBound.setSummary("Threshold attuale: " + (int)value);
                        }
                        else ErrorHandler.mustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });
            maxPressureAverageLowerBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setMaxPressureAverageLowerBound((int)value);
                            maxPressureAverageLowerBound.setSummary("Threshold attuale: " + (int)value);

                        }
                        else ErrorHandler.mustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());
                    return false;
                }
            });

            maxPressureAverageUpperBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setMaxPressureAverageUpperBound((int)value);
                            maxPressureAverageUpperBound.setSummary("Threshold attuale: " +(int) value);

                        }
                        else ErrorHandler.mustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());


                    return false;
                }
            });

            weightAverageLowerBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        preferenceManager.setWeightAverageLowerBound(value);
                        weightAverageLowerBound.setSummary("Threshold attuale: " + value);
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());


                    return false;
                }
            });

            weightAverageUpperBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        preferenceManager.setWeightAverageUpperBound(value);
                        weightAverageUpperBound.setSummary("Threshold attuale: " + value);

                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });

            temperatureAverageLowerBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        preferenceManager.setTemperatureAverageLowerBound(value);
                        temperatureAverageLowerBound.setSummary("Threshold attuale: " + value);
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });;
            temperatureAverageUpperBound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        preferenceManager.setTemperatureAverageUpperBound(value);
                        temperatureAverageUpperBound.setSummary("Threshold attuale: " + value);
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });

            makeDatePickerVisible();
            makePressureVisibile(DatabaseManager.DEFAULT_NULL_VALUE);
            makeTemperatureVisible(DatabaseManager.DEFAULT_NULL_VALUE);
            makeWeightVisibile(DatabaseManager.DEFAULT_NULL_VALUE);
            intervalTime.setSummary("Attuale tempo di monitoraggio " + preferenceManager.getIntervalMonitorTime());

        }


        private void makeDatePickerVisible(){
            boolean visible = preferenceManager.getDailyNotification();
            dailyNotification.setDefaultValue(visible);
            if(visible){
                datePicker.setVisible(visible);
                selectedHour.setVisible(visible);
                Date date = preferenceManager.getDailyNotificationHour();
                selectedHour.setTitle("Ora della notifica giornaliera: " + Converters.printDateHourAndMinutes(date));
            }
            else{
                datePicker.setVisible(false);
                selectedHour.setVisible(false);
                notificationHandler.cancelAlarm();
            }
        }


    private void makePressureVisibile(int value){
        if(value==DatabaseManager.DEFAULT_NULL_VALUE) value = preferenceManager.getPressurePriority();
        pressurePriority.setValue(String.valueOf(value));
        if(value >= PRIORITY_MONITOR_VALUE){
            int maxLowerBound = preferenceManager.getMaxPressureAverageLowerBound();
            if(maxLowerBound != Integer.MIN_VALUE){
                maxPressureAverageLowerBound.setSummary("Threshold attuale: " + maxLowerBound);
            }
            int maxUpperBound = preferenceManager.getMaxPressureAverageUpperBound();
            if(maxUpperBound != Integer.MAX_VALUE){
                maxPressureAverageUpperBound.setSummary("Threshold attuale: " + maxUpperBound);
            }
            int minLowerBound = preferenceManager.getMinPressureAverageLowerBound();
            if(minLowerBound != Integer.MIN_VALUE){
                minPressureAverageLowerBound.setSummary("Threshold attuale: " + minLowerBound);
            }
            int minUpperBound = preferenceManager.getMinPressureAverageUpperBound();
            if(minUpperBound != Integer.MAX_VALUE){
                minPressureAverageUpperBound.setSummary("Threshold attuale: " + minUpperBound);
            }
            pressureCategory.setVisible(true);
        }
        else pressureCategory.setVisible(false);
    }

    private void makeWeightVisibile(int value){
        if(value==DatabaseManager.DEFAULT_NULL_VALUE) value = preferenceManager.getWeightPriority();
        weightPriority.setValue(String.valueOf(value));
        if(value >= PRIORITY_MONITOR_VALUE){
            double weightLowerBound = preferenceManager.getWeightAverageLowerBound();
            if(weightLowerBound != Double.MIN_VALUE){
                weightAverageLowerBound.setSummary("Threshold attuale: " + weightLowerBound);
            }
            double weightUpperBound= preferenceManager.getWeightAverageUpperBound();
            if(weightUpperBound != Double.MAX_VALUE){
                weightAverageUpperBound.setSummary("Threshold attuale: " + weightUpperBound);
            }
            weightCategory.setVisible(true);
        }
        else weightCategory.setVisible(false);
    }

    private void makeTemperatureVisible(int value){
        if (value==DatabaseManager.DEFAULT_NULL_VALUE) value = preferenceManager.getTemperaturePriority();
        temperaturePriority.setValue(String.valueOf(value));

        if(value >= PRIORITY_MONITOR_VALUE){
            double temperatureLowerBound = preferenceManager.getTemperatureAverageLowerBound();
            if(temperatureLowerBound != Double.MIN_VALUE){
                temperatureAverageLowerBound.setSummary("Threshold attuale: " + temperatureLowerBound);
            }
            double temperatureUpperBound = preferenceManager.getTemperatureAverageUpperBound();
            if(temperatureUpperBound != Double.MAX_VALUE){
                temperatureAverageUpperBound.setSummary("Threshold attuale: " + temperatureUpperBound);
            }
            temperatureCategory.setVisible(true);
        }
        else temperatureCategory.setVisible(false);
    }


}
