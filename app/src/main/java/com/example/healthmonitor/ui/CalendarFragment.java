package com.example.healthmonitor.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthmonitor.DialogRecord;
import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.MyRoomDatabase;
import com.example.healthmonitor.RoomDatabase.Record;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements DialogRecord.DialogListener {
   private DatabaseManager databaseManager;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.databaseManager = DatabaseManager.getInstanceDb(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }

    @Override
    public void onResume() {
        //btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_records, null));
        FloatingActionButton addRecord = getActivity().findViewById(R.id.addRecord);
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MaterialButton btn = getActivity().findViewById(R.id.changeFragment);
                //btn.setText("Finokkio");
                openAddDialog();
            }
        });
        super.onResume();
    }


    public void openAddDialog(){
        DialogRecord dialog = new DialogRecord(this.databaseManager);
        dialog.setDialogListener(this);
        dialog.show(getActivity().getSupportFragmentManager(), "Tag");
        dialog.setCancelable(false);

    }

    @Override
    public void dialogAddRecord(Record r) {
           this.databaseManager.addRecord(r);
        //TODO implement AddRecord from Database
    }

    @Override
    public void dialogEditRecord(int position, int min_pressure, int max_pressure, double temperature, double weight, Date date) {

    }


}
