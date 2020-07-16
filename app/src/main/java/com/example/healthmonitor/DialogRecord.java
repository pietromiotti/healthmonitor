package com.example.healthmonitor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.Record;
import com.example.healthmonitor.ui.CalendarFragment;
import com.example.healthmonitor.utils.Converters;
import com.example.healthmonitor.utils.NotificationHandler;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class DialogRecord extends DialogFragment{
    private DatabaseManager databaseManager;
    private NotificationHandler notificationHandler;

    public DialogRecord() {}

    public DialogRecord(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    public interface DialogListener {
        public void dialogAddRecord(Record newRecord);
        public void dialogEditRecord(int position, int min_pressure, int max_pressure, double temperature, double weight, Date date);
    }

    DialogListener dialogListener;

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.databaseManager = DatabaseManager.getInstanceDBNOContext();



        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_record, null);
        String acceptButton = "Aggiungi";
        String title = "Aggiungi un record";
        String description = "Aggiungi un nuovo record e continua a monitorare i tuoi parametri!";

        final TextInputLayout minpressureEditText = view.findViewById(R.id.dialog_minPress);
        final TextInputLayout maxpressureEditText = view.findViewById(R.id.dialog_maxPress);
        final TextInputLayout temperatureEditText = view.findViewById(R.id.dialog_temperature);
        final TextInputLayout weightEditText = view.findViewById(R.id.dialog_weight);


        if (getArguments() != null){
            acceptButton = getArguments().getString("Accept");
            title = getArguments().getString("Title");
            description = getArguments().getString("Description");

            /*Pre-imposta il dialog con i valori del record */
            weightEditText.getEditText().setText(String.valueOf(getArguments().getDouble("weight")));
            maxpressureEditText.getEditText().setText(String.valueOf(getArguments().getInt("max_pressure")));
            minpressureEditText.getEditText().setText(String.valueOf(getArguments().getInt("min_pressure")));
            temperatureEditText.getEditText().setText(String.valueOf(getArguments().getDouble("temperature")));

        }
        builder.setView(view)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(acceptButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(getArguments()!=null){
                            int position = getArguments().getInt("Position");

                            /*Retrieve all data from the dialog */

                            Double weight = Converters.parseStringToDouble(weightEditText.getEditText().getText().toString());
                            Double temperature = Converters.parseStringToDouble(temperatureEditText.getEditText().getText().toString());
                            Integer minpressure = Converters.parseStringToInt(minpressureEditText.getEditText().getText().toString());
                            Integer maxpressure = Converters.parseStringToInt(maxpressureEditText.getEditText().getText().toString());

                            dialogListener.dialogEditRecord(position, minpressure, maxpressure, temperature, weight, null );

                        }
                        else {

                            Double weight = Converters.parseStringToDouble(weightEditText.getEditText().getText().toString());
                            Double temperature = Converters.parseStringToDouble(temperatureEditText.getEditText().getText().toString());
                            Integer minpressure = Converters.parseStringToInt(minpressureEditText.getEditText().getText().toString());
                            Integer maxpressure = Converters.parseStringToInt(maxpressureEditText.getEditText().getText().toString());

                            Date today = new Date();
                            if (CalendarFragment.selectedDate == null){
                                today = Calendar.getInstance().getTime();
                            }
                            else {
                                today = CalendarFragment.selectedDate.getTime();
                            }
                            Record record = databaseManager.initRecord(minpressure,maxpressure,temperature,weight, today, false);
                            dialogListener.dialogAddRecord(record);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);

        return builder.create();
    }

}