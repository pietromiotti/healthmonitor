package com.example.healthmonitor.ui;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.utils.Converters;
import com.example.healthmonitor.utils.ErrorHandler;
import com.example.healthmonitor.utils.NotificationHandler;
import com.example.healthmonitor.utils.PreferenceManager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;

import android.widget.EditText;
import android.widget.TimePicker;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;


import com.example.healthmonitor.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;


/*Setting Fragment */
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

    private EditTextPreference  intervalTime;
    private EditTextPreference  minPressureAverageLowerBound;
    private EditTextPreference  minPressureAverageUpperBound;
    private EditTextPreference  maxPressureAverageLowerBound;
    private EditTextPreference  maxPressureAverageUpperBound;
    private EditTextPreference  weightAverageLowerBound;
    private EditTextPreference  weightAverageUpperBound;
    private EditTextPreference  temperatureAverageLowerBound;
    private EditTextPreference  temperatureAverageUpperBound;

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

            /*Init the preference manager and the notification handler */
            this.preferenceManager = PreferenceManager.getPreferenceManagerNoContext();
            this.notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
            // Load the preferences from an XML resource
            setPreferencesFromResource(R.xml.fragment_settings, rootKey);

            /* Get all the references from the xml file */
            datePicker = findPreference("dateTimePicker");
            selectedHour = findPreference("datePickerValue");
            dailyNotification = findPreference("daily_notify");

            weightPriority = findPreference("weightPriority");
            temperaturePriority = findPreference("temperaturePriority");
            pressurePriority = findPreference("pressurePriority");

            intervalTime = findPreference("intervalTime");
            minPressureAverageLowerBound =  findPreference("minPressureAverageLowerBound");
            minPressureAverageUpperBound = findPreference("minPressureAverageUpperBound");
            maxPressureAverageLowerBound = findPreference("maxPressureAverageLowerBound");
            maxPressureAverageUpperBound = findPreference("maxPressureAverageUpperBound");
            weightAverageLowerBound = findPreference("weightAverageLowerBound");
            weightAverageUpperBound = findPreference("weightAverageUpperBound");
            temperatureAverageLowerBound = findPreference("temperatureAverageLowerBound");
            temperatureAverageUpperBound = findPreference("temperatureAverageUpperBound");


            minPressureAverageLowerBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
            minPressureAverageUpperBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            maxPressureAverageLowerBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            maxPressureAverageUpperBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            temperatureAverageUpperBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });

            temperatureAverageLowerBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });

            weightAverageUpperBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });

            weightAverageLowerBound.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });

            intervalTime.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });


            pressureCategory = findPreference("pressureCategory");
            weightCategory = findPreference("weightCategory");
            temperatureCategory = findPreference("temperatureCategory");



            /*Handling the "Daily Notification" switcher, if on/off*/
            dailyNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean switched = (Boolean) newValue;
                    preferenceManager.setDailyNotification(switched);
                    makeDatePickerVisible();

                    Date date = preferenceManager.getDailyNotificationHour();
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    if(switched){
                        notificationHandler.startAlarm(c);
                    }
                    else notificationHandler.cancelAlarm();

                    return true;
                }
            });

            /*Handling the Date Picker, which is visible only if the Daily Notification Switcher is ON*/
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

                            notificationHandler.startAlarm(newCalendar);
                            /*Editing the SharedPreference */
                            preferenceManager.setDailyNotificationHour(newCalendar.getTime());

                            SettingsFragment.selectedHour.setTitle(getResources().getString(R.string.datePickerTitle) + Converters.printDateHourAndMinutes(newCalendar.getTime()));
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle(getResources().getString(R.string.datePickerTitle_second));
                    mTimePicker.show();
                    return true;
                }
            });


            /*Handling weight priority List */
            weightPriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setWeightPriority(value);
                    makeWeightVisibile(value);
                    return false;
                }
            });

            /*Handling tempearture priority List */
            temperaturePriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setTemperaturePriority(value);
                    makeTemperatureVisible(value);
                    return false;
                }
            });

            /*Handling Pressure priority List */
            pressurePriority.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(newValue.toString());
                    preferenceManager.setPressurePriority(value);
                    makePressureVisibile(value);
                    return false;
                }
            });

            /*Handling the Interval Timer */
            intervalTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    double value = Double.parseDouble(newValue.toString());
                    if(Converters.isPositive(value) && value!=DatabaseManager.DEFAULT_NULL_VALUE){
                        if(Converters.isInt(value)){
                            preferenceManager.setIntervalMonitorTime((int) value);
                            intervalTime.setSummary("Attuale tempo di monitoraggio " + (int) value);
                            intervalTime.setDefaultValue((int) value);
                        }
                        else ErrorHandler.IntervalTimermustBeInteger(getActivity().getApplicationContext());
                    }
                    else ErrorHandler.mustBePositive(getActivity().getApplicationContext());

                    return false;
                }
            });

            /*Handling Min Pressure Average Lower Bound */
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

            /*Handling Min Pressure Average Upper Bound */
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

            /*Handling Max Pressure Average Lower Bound */
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

            /*Handling Max Pressure Average Upper Bound */
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

            /*Handling Weight Average Lower Bound */
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

            /*Handling Weight Average Upper Bound */
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

            /*Handling Temperature Average Lower Bound */
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
            /*Handling Temperature Average Upper Bound */
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

            /*Alla creazione del fragment, effettua il restore dei parametri del frament preselezionando tutte le preferenze*/
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
