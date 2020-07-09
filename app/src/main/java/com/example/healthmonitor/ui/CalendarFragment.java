package com.example.healthmonitor.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.healthmonitor.DialogRecord;
import com.example.healthmonitor.MainActivity;
import com.example.healthmonitor.R;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.MyRoomDatabase;
import com.example.healthmonitor.RoomDatabase.Record;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements DialogRecord.DialogListener {
   private DatabaseManager databaseManager;
   private CalendarView calendarView;
   public static Calendar selectedDate;

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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        final TextView text = view.findViewById(R.id.textView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int minutes = rightNow.get(Calendar.MINUTE);
                selectedDate = Calendar.getInstance();
                selectedDate.set(year,month,dayOfMonth,hour,minutes);
                String s = "Selected date " + dayOfMonth + "/" + month + "/" + year;
                text.setText(s);
            }
        });
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
