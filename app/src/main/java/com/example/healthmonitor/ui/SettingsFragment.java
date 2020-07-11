package com.example.healthmonitor.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.example.healthmonitor.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;


public class SettingsFragment extends PreferenceFragmentCompat  {
    MaterialButton selectHour;
    private static Preference selectedHour;
    private Preference datePicker;
    private Preference dailyNotification;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);


        datePicker = (Preference) findPreference("dateTimePicker");
        selectedHour = (Preference) findPreference("datePickerValue");
        dailyNotification = findPreference("daily_notify");



        dailyNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = (Boolean) newValue;
                if(switched){
                    datePicker.setVisible(true);
                    selectedHour.setVisible(true);
                }
                else{
                    datePicker.setVisible(false);
                    selectedHour.setVisible(false);
                }
                return true;
            }
        });

        final int[] hourselected = new int[1];
        final int[] minuteselected = new int[1];
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
                        hourselected[0] = selectedHour;
                        minuteselected[0] = selectedMinute;
                        SettingsFragment.selectedHour.setTitle("Ora della notifica giornaliera: " + hourselected[0] + ":" + minuteselected[0]);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                return true;
            }
        });



    }



}
